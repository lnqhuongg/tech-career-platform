package com.dacn.resume_service.service.impl;

import com.dacn.resume_service.client.AiServiceClient;
import com.dacn.resume_service.dto.ai.AiJobMatchRequest;
import com.dacn.resume_service.dto.ai.AiJobMatchResponse;
import com.dacn.resume_service.dto.request.EvaluateJobRequest;
import com.dacn.resume_service.dto.response.JobMatchEvaluationResponse;
import com.dacn.resume_service.exception.ForbiddenException;
import com.dacn.resume_service.exception.ResourceNotFoundException;
import com.dacn.resume_service.model.Resume;
import com.dacn.resume_service.model.ResumeParsedData;
import com.dacn.resume_service.repository.ResumeParsedDataRepository;
import com.dacn.resume_service.repository.ResumeRepository;
import com.dacn.resume_service.security.CurrentUser;
import com.dacn.resume_service.service.IJobMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JobMatchServiceImpl implements IJobMatchService {

    private static final Logger log = LoggerFactory.getLogger(JobMatchServiceImpl.class);

    private final ResumeRepository resumeRepository;
    private final ResumeParsedDataRepository parsedDataRepository;
    private final AiServiceClient aiServiceClient;

    /**
     * Cache trong bộ nhớ (In-Memory Cache):
     * Key: resumeId:jobId:cvUpdatedAtEpoch:contentHash
     * Giúp:
     * 1. Lần hỏi cùng 1 CV & Job sẽ trả về kết quả giống nhau 100% ngay lập tức (0ms).
     * 2. Khi ứng viên sửa CV (updatedAt thay đổi), key tự động hết hạn/miss cache và tính toán lại.
     * 3. Không cần lưu bảng job_match_evaluations trong DB, loại bỏ nguy cơ dữ liệu cũ / lệch đồng bộ.
     */
    private record CacheEntry(JobMatchEvaluationResponse response, long timestamp) {}
    private final Map<String, CacheEntry> evaluationCache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL_MILLIS = 30 * 60 * 1000L; // 30 phút

    public JobMatchServiceImpl(
            ResumeRepository resumeRepository,
            ResumeParsedDataRepository parsedDataRepository,
            AiServiceClient aiServiceClient
    ) {
        this.resumeRepository = resumeRepository;
        this.parsedDataRepository = parsedDataRepository;
        this.aiServiceClient = aiServiceClient;
    }

    @Override
    public JobMatchEvaluationResponse evaluateMatch(CurrentUser user, UUID resumeId, UUID jobId, EvaluateJobRequest request) {
        // 1. Kiểm tra quyền sở hữu CV dựa trên bảng resume_parsed_data (CV nộp đơn)
        ResumeParsedData parsedData = parsedDataRepository.findByIdAndIsDeletedFalse(resumeId)
                .or(() -> parsedDataRepository.findByResumeIdAndIsDeletedFalse(resumeId))
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CV: " + resumeId));

        if (!parsedData.getUserId().equals(user.getAccountId())) {
            throw new ForbiddenException("Bạn không có quyền thao tác trên CV này");
        }

        // 2. Kiểm tra bộ nhớ đệm (Cache) để trả về ngay nếu CV và Job không thay đổi
        String cacheKey = buildCacheKey(parsedData.getId(), jobId, parsedData.getUpdatedAt(), request);
        CacheEntry cached = evaluationCache.get(cacheKey);
        if (cached != null && (System.currentTimeMillis() - cached.timestamp()) < CACHE_TTL_MILLIS) {
            log.info("Job Match Cache HIT cho CV: {} và Job: {}", parsedData.getId(), jobId);
            return cached.response();
        }

        // 3. Chuẩn bị danh sách kỹ năng ứng viên (chuẩn hóa và sắp xếp để đảm bảo tính nhất quán)
        List<String> candidateSkills = parsedData.getExtractedSkills() != null
                ? parsedData.getExtractedSkills().stream().filter(Objects::nonNull).map(String::trim).distinct().sorted().toList()
                : Collections.emptyList();

        List<String> requiredSkills = (request != null && request.getRequiredSkills() != null)
                ? request.getRequiredSkills().stream().filter(Objects::nonNull).map(String::trim).distinct().sorted().toList()
                : Collections.emptyList();

        // Lấy văn bản thô CV (nếu có file PDF liên kết) hoặc sinh tóm tắt từ dữ liệu CV
        String rawText = "";
        if (parsedData.getResumeId() != null) {
            rawText = resumeRepository.findById(parsedData.getResumeId())
                    .map(Resume::getRawText)
                    .orElse("");
        }
        if (rawText.isBlank()) {
            rawText = (parsedData.getSummary() != null ? parsedData.getSummary() : "") + " "
                    + String.join(", ", candidateSkills);
        }

        BigDecimal score;
        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();
        String suggestions;

        // 4. Ưu tiên gọi AI Service (Sentence-BERT Semantic Matching + Skill Gap + LLM Recommendations)
        AiJobMatchResponse aiResponse = null;
        try {
            AiJobMatchRequest aiRequest = AiJobMatchRequest.builder()
                    .resumeId(parsedData.getId().toString())
                    .resumeText(rawText)
                    .candidateSkills(candidateSkills)
                    .jobId(jobId != null ? jobId.toString() : null)
                    .jobTitle(request != null ? request.getJobTitle() : "")
                    .jobDescription(request != null ? request.getJobDescription() : "")
                    .requiredSkills(requiredSkills)
                    .build();

            aiResponse = aiServiceClient.matchJob(aiRequest);
        } catch (Exception ex) {
            log.warn("Gọi AI Service đối sánh việc làm thất bại: {}. Kích hoạt thuật toán dự phòng.", ex.getMessage());
        }

        if (aiResponse != null && aiResponse.getMatchScore() != null) {
            score = aiResponse.getMatchScore();
            matchedSkills = aiResponse.getMatchedSkills() != null ? aiResponse.getMatchedSkills() : Collections.emptyList();
            missingSkills = aiResponse.getMissingSkills() != null ? aiResponse.getMissingSkills() : Collections.emptyList();
            suggestions = aiResponse.getAiSuggestions();
            log.info("Đối sánh CV ({}) với Job ({}) thành công: {}%", parsedData.getId(), jobId, score);
        } else {
            // 5. Cơ chế Fallback: Thuật toán đối sánh tập hợp nội bộ
            for (String reqSkill : requiredSkills) {
                String norm = reqSkill.trim().toLowerCase();
                boolean isMatched = false;
                for (String cSkill : candidateSkills) {
                    if (cSkill.trim().equalsIgnoreCase(norm) || cSkill.toLowerCase().contains(norm) || norm.contains(cSkill.toLowerCase())) {
                        isMatched = true;
                        matchedSkills.add(reqSkill);
                        break;
                    }
                }
                if (!isMatched) {
                    missingSkills.add(reqSkill);
                }
            }

            if (requiredSkills.isEmpty()) {
                score = new BigDecimal("100.00");
            } else {
                double percent = ((double) matchedSkills.size() / requiredSkills.size()) * 100.0;
                score = BigDecimal.valueOf(percent).setScale(2, RoundingMode.HALF_UP);
            }

            String jobTitle = (request != null && request.getJobTitle() != null && !request.getJobTitle().isBlank())
                    ? request.getJobTitle()
                    : "vị trí ứng tuyển";

            if (missingSkills.isEmpty()) {
                suggestions = String.format("Tuyệt vời! Hồ sơ của bạn đáp ứng đầy đủ tất cả kỹ năng yêu cầu cho %s. Hãy tự tin nộp hồ sơ!", jobTitle);
            } else {
                suggestions = String.format("Hồ sơ của bạn phù hợp %s%% cho %s. Để gia tăng cơ hội trúng tuyển, bạn nên trau dồi thêm các kỹ năng còn thiếu: %s.",
                        score, jobTitle, String.join(", ", missingSkills));
            }
        }

        LocalDateTime now = LocalDateTime.now();
        JobMatchEvaluationResponse response = JobMatchEvaluationResponse.builder()
                .id(UUID.randomUUID())
                .resumeId(parsedData.getId())
                .jobId(jobId)
                .matchScore(score)
                .matchedSkills(matchedSkills)
                .missingSkills(missingSkills)
                .aiSuggestions(suggestions)
                .evaluatedAt(now)
                .updatedAt(now)
                .build();

        // 6. Lưu vào bộ nhớ đệm Cache
        evaluationCache.put(cacheKey, new CacheEntry(response, System.currentTimeMillis()));

        return response;
    }

    @Override
    public JobMatchEvaluationResponse getEvaluation(CurrentUser user, UUID resumeId, UUID jobId) {
        ResumeParsedData parsedData = parsedDataRepository.findByIdAndIsDeletedFalse(resumeId)
                .or(() -> parsedDataRepository.findByResumeIdAndIsDeletedFalse(resumeId))
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CV: " + resumeId));

        if (!parsedData.getUserId().equals(user.getAccountId())
                && !"RECRUITER".equals(user.getAccountType())
                && !"ADMIN".equals(user.getAccountType())) {
            throw new ForbiddenException("Bạn không có quyền xem kết quả đánh giá của CV này");
        }

        // Tìm trong cache cho cặp (resumeId, jobId)
        String prefix = parsedData.getId() + ":" + jobId + ":";
        for (Map.Entry<String, CacheEntry> entry : evaluationCache.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                if ((System.currentTimeMillis() - entry.getValue().timestamp()) < CACHE_TTL_MILLIS) {
                    return entry.getValue().response();
                }
            }
        }

        // Nếu chưa có trong cache, tính toán nhanh on-the-fly
        return evaluateMatch(user, resumeId, jobId, null);
    }

    @Override
    public List<JobMatchEvaluationResponse> getEvaluationsByResume(CurrentUser user, UUID resumeId) {
        ResumeParsedData parsedData = parsedDataRepository.findByIdAndIsDeletedFalse(resumeId)
                .or(() -> parsedDataRepository.findByResumeIdAndIsDeletedFalse(resumeId))
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CV: " + resumeId));

        if (!parsedData.getUserId().equals(user.getAccountId())
                && !"RECRUITER".equals(user.getAccountType())
                && !"ADMIN".equals(user.getAccountType())) {
            throw new ForbiddenException("Bạn không có quyền xem danh sách đánh giá của CV này");
        }

        String prefix = parsedData.getId() + ":";
        List<JobMatchEvaluationResponse> results = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (Map.Entry<String, CacheEntry> entry : evaluationCache.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                if ((now - entry.getValue().timestamp()) < CACHE_TTL_MILLIS) {
                    results.add(entry.getValue().response());
                }
            }
        }

        return results;
    }

    private String buildCacheKey(UUID resumeId, UUID jobId, LocalDateTime updatedAt, EvaluateJobRequest request) {
        String title = (request != null && request.getJobTitle() != null) ? request.getJobTitle().trim().toLowerCase() : "";
        String desc = (request != null && request.getJobDescription() != null) ? request.getJobDescription().trim() : "";
        List<String> skills = (request != null && request.getRequiredSkills() != null)
                ? request.getRequiredSkills().stream().sorted().toList()
                : Collections.emptyList();
        int requestHash = Objects.hash(title, desc, skills);
        long updatedEpoch = updatedAt != null ? updatedAt.toEpochSecond(ZoneOffset.UTC) : 0L;
        return resumeId + ":" + jobId + ":" + updatedEpoch + ":" + requestHash;
    }
}
