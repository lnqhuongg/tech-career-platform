package com.dacn.resume_service.service.impl;

import com.dacn.resume_service.client.AiServiceClient;
import com.dacn.resume_service.dto.ai.AiParseResumeResponse;
import com.dacn.resume_service.dto.request.SaveParsedDataRequest;
import com.dacn.resume_service.dto.request.UpdateResumeRequest;
import com.dacn.resume_service.dto.response.ResumeParsedDataResponse;
import com.dacn.resume_service.dto.response.ResumeResponse;
import com.dacn.resume_service.exception.BadRequestException;
import com.dacn.resume_service.exception.ForbiddenException;
import com.dacn.resume_service.exception.ResourceNotFoundException;
import com.dacn.resume_service.model.Resume;
import com.dacn.resume_service.model.ResumeParsedData;
import com.dacn.resume_service.repository.ResumeParsedDataRepository;
import com.dacn.resume_service.repository.ResumeRepository;
import com.dacn.resume_service.security.CurrentUser;
import com.dacn.resume_service.service.FileStorageService;
import com.dacn.resume_service.service.IResumeService;
import com.dacn.resume_service.service.PdfExtractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ResumeServiceImpl implements IResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeServiceImpl.class);

    private final ResumeRepository resumeRepository;
    private final ResumeParsedDataRepository parsedDataRepository;
    private final FileStorageService fileStorageService;
    private final PdfExtractionService pdfExtractionService;
    private final AiServiceClient aiServiceClient;

    public ResumeServiceImpl(
            ResumeRepository resumeRepository,
            ResumeParsedDataRepository parsedDataRepository,
            FileStorageService fileStorageService,
            PdfExtractionService pdfExtractionService,
            AiServiceClient aiServiceClient
    ) {
        this.resumeRepository = resumeRepository;
        this.parsedDataRepository = parsedDataRepository;
        this.fileStorageService = fileStorageService;
        this.pdfExtractionService = pdfExtractionService;
        this.aiServiceClient = aiServiceClient;
    }

    @Override
    @Transactional
    public ResumeResponse uploadResume(CurrentUser user, MultipartFile file, String title) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File tải lên không được để trống");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (!originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new BadRequestException("Hệ thống hiện tại chỉ chấp nhận định dạng file PDF");
        }

        if (title == null || title.isBlank()) {
            title = originalFilename.replaceAll("(?i)\\.pdf$", "");
        }

        // 1. Lưu file vật lý
        String storedFileName = fileStorageService.storeFile(file, user.getAccountId());
        String fileUrl = "/resumes/download/" + storedFileName;

        // 2. Trích xuất text từ PDF
        String rawText = "";
        String status = "DRAFT";
        try {
            rawText = pdfExtractionService.extractTextFromPdf(file);
        } catch (Exception ex) {
            log.error("Lỗi khi đọc text từ PDF: {}", ex.getMessage());
            status = "FAILED";
        }

        // 3. Lưu bảng resumes (lưu thông tin file và raw_text)
        Resume resume = Resume.builder()
                .userId(user.getAccountId())
                .fileName(originalFilename)
                .fileSize(file.getSize())
                .fileType("application/pdf")
                .fileUrl(fileUrl)
                .rawText(rawText)
                .build();

        resume = resumeRepository.save(resume);

        // 4. Kiểm tra xem ứng viên đã có CV nào trong resume_parsed_data chưa;
        // nếu là CV đầu tiên thì tự động đặt làm mặc định
        List<ResumeParsedData> existing = parsedDataRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(user.getAccountId());
        boolean isDefault = existing.isEmpty();

        // 5. Bóc tách thông tin bằng AI Microservice (hoặc Regex fallback)
        SaveParsedDataRequest draft = null;
        try {
            AiParseResumeResponse aiResponse = aiServiceClient.parseResume(rawText);
            if (aiResponse != null && (aiResponse.getExtractedSkills() != null || aiResponse.getCandidateName() != null)) {
                draft = SaveParsedDataRequest.builder()
                        .candidateName(aiResponse.getCandidateName())
                        .targetRole(aiResponse.getTargetRole())
                        .phone(aiResponse.getPhone())
                        .email(aiResponse.getEmail())
                        .summary(aiResponse.getSummary())
                        .totalExperienceMonths(aiResponse.getTotalExperienceMonths() != null ? aiResponse.getTotalExperienceMonths() : 0)
                        .extractedSkills(aiResponse.getExtractedSkills())
                        .educations(aiResponse.getEducations())
                        .experiences(aiResponse.getExperiences())
                        .certifications(aiResponse.getCertifications())
                        .build();
                log.info("Bóc tách CV bằng AI Service thành công cho ứng viên: {}", draft.getCandidateName());
            }
        } catch (Exception ex) {
            log.warn("Gọi AI Service trích xuất CV thất bại, kích hoạt Fallback nội bộ: {}", ex.getMessage());
        }

        if (draft == null) {
            draft = pdfExtractionService.generateDraftParsedData(rawText);
        }

        // 6. Lưu bảng resume_parsed_data (Hồ sơ CV chính thức dùng để nộp việc)
        ResumeParsedData parsedData = ResumeParsedData.builder()
                .userId(user.getAccountId())
                .title(title)
                .resumeId(resume.getId())
                .candidateName(draft.getCandidateName())
                .targetRole(draft.getTargetRole())
                .phone(draft.getPhone())
                .email(draft.getEmail())
                .summary(draft.getSummary())
                .totalExperienceMonths(draft.getTotalExperienceMonths())
                .parsingStatus(status)
                .isDefault(isDefault)
                .isDeleted(false)
                .extractedSkills(draft.getExtractedSkills())
                .educations(draft.getEducations())
                .experiences(draft.getExperiences())
                .certifications(draft.getCertifications())
                .updatedByUser(false)
                .build();

        parsedData = parsedDataRepository.save(parsedData);

        return mapToResponse(parsedData, resume);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeResponse> getMyResumes(CurrentUser user) {
        // Truy vấn danh sách CV từ bảng resume_parsed_data (bảng hồ sơ CV)
        List<ResumeParsedData> list = parsedDataRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(user.getAccountId());
        return list.stream()
                .map(data -> {
                    Resume resume = data.getResumeId() != null
                            ? resumeRepository.findById(data.getResumeId()).orElse(null)
                            : null;
                    return mapToResponse(data, resume);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeResponse getResumeById(CurrentUser user, UUID resumeId) {
        ResumeParsedData data = parsedDataRepository.findByIdAndIsDeletedFalse(resumeId)
                .or(() -> parsedDataRepository.findByResumeIdAndIsDeletedFalse(resumeId))
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CV với mã: " + resumeId));

        if (!data.getUserId().equals(user.getAccountId())
                && !"RECRUITER".equals(user.getAccountType())
                && !"ADMIN".equals(user.getAccountType())) {
            throw new ForbiddenException("Bạn không có quyền truy cập vào CV này");
        }

        Resume resume = data.getResumeId() != null
                ? resumeRepository.findById(data.getResumeId()).orElse(null)
                : null;

        return mapToResponse(data, resume);
    }

    @Override
    @Transactional
    public ResumeResponse updateResume(CurrentUser user, UUID resumeId, UpdateResumeRequest request) {
        ResumeParsedData data = getOwnedParsedData(user, resumeId);

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            data.setTitle(request.getTitle());
        }

        if (Boolean.TRUE.equals(request.getIsDefault()) && !Boolean.TRUE.equals(data.getIsDefault())) {
            parsedDataRepository.clearOtherDefaults(user.getAccountId(), data.getId());
            data.setIsDefault(true);
        }

        data = parsedDataRepository.save(data);

        Resume resume = data.getResumeId() != null
                ? resumeRepository.findById(data.getResumeId()).orElse(null)
                : null;

        return mapToResponse(data, resume);
    }

    @Override
    @Transactional
    public ResumeResponse saveParsedData(CurrentUser user, UUID resumeId, SaveParsedDataRequest request) {
        ResumeParsedData data = getOwnedParsedData(user, resumeId);

        // Cập nhật các thông tin do ứng viên chỉnh sửa (Human-in-the-loop)
        data.setCandidateName(request.getCandidateName());
        data.setTargetRole(request.getTargetRole());
        data.setPhone(request.getPhone());
        data.setEmail(request.getEmail());
        data.setSummary(request.getSummary());
        data.setTotalExperienceMonths(request.getTotalExperienceMonths());
        data.setExtractedSkills(request.getExtractedSkills());
        data.setEducations(request.getEducations());
        data.setExperiences(request.getExperiences());
        data.setCertifications(request.getCertifications());
        data.setUpdatedByUser(true); // Đánh dấu ứng viên đã xác nhận
        data.setParsingStatus("COMPLETED"); // Chuyển trạng thái sang COMPLETED (sẵn sàng nộp đơn)

        data = parsedDataRepository.save(data);

        Resume resume = data.getResumeId() != null
                ? resumeRepository.findById(data.getResumeId()).orElse(null)
                : null;

        return mapToResponse(data, resume);
    }

    @Override
    @Transactional
    public ResumeResponse setDefault(CurrentUser user, UUID resumeId) {
        ResumeParsedData data = getOwnedParsedData(user, resumeId);
        parsedDataRepository.clearOtherDefaults(user.getAccountId(), data.getId());
        data.setIsDefault(true);
        data = parsedDataRepository.save(data);

        Resume resume = data.getResumeId() != null
                ? resumeRepository.findById(data.getResumeId()).orElse(null)
                : null;

        return mapToResponse(data, resume);
    }

    @Override
    @Transactional
    public void deleteResume(CurrentUser user, UUID resumeId) {
        // Xóa mềm trên bảng resume_parsed_data để bảo toàn lịch sử đơn nộp
        ResumeParsedData data = getOwnedParsedData(user, resumeId);
        data.setIsDeleted(true);
        parsedDataRepository.save(data);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource loadFileResource(UUID resumeId, CurrentUser user) {
        // Tìm theo ResumeParsedData trước để lấy resumeId của file PDF gốc
        ResumeParsedData parsedData = parsedDataRepository.findByIdAndIsDeletedFalse(resumeId)
                .or(() -> parsedDataRepository.findByResumeId(resumeId))
                .orElse(null);

        UUID actualFileResumeId = (parsedData != null && parsedData.getResumeId() != null)
                ? parsedData.getResumeId()
                : resumeId;

        Resume resume = resumeRepository.findById(actualFileResumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy file CV đính kèm: " + resumeId));

        if (user != null
                && !resume.getUserId().equals(user.getAccountId())
                && !"RECRUITER".equals(user.getAccountType())
                && !"ADMIN".equals(user.getAccountType())) {
            throw new ForbiddenException("Không có quyền tải hoặc xem file CV này");
        }

        String fileUrl = resume.getFileUrl();
        if (fileUrl == null) {
            throw new BadRequestException("File này không có đường dẫn lưu trữ");
        }
        String storedName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        return fileStorageService.loadFileAsResource(storedName);
    }

    @Override
    @Transactional(readOnly = true)
    public String getRawText(UUID resumeId) {
        ResumeParsedData parsedData = parsedDataRepository.findByIdAndIsDeletedFalse(resumeId)
                .or(() -> parsedDataRepository.findByResumeId(resumeId))
                .orElse(null);

        if (parsedData != null && parsedData.getResumeId() != null) {
            return resumeRepository.findById(parsedData.getResumeId())
                    .map(Resume::getRawText)
                    .orElse("");
        }

        return resumeRepository.findById(resumeId)
                .map(Resume::getRawText)
                .orElse("");
    }

    @Override
    @Transactional(readOnly = true)
    public com.dacn.resume_service.dto.ai.AiCvAnalysisResponse analyzeResumeQuality(CurrentUser user, UUID resumeId) {
        ResumeParsedData parsedData = getOwnedParsedData(user, resumeId);
        String rawText = getRawText(resumeId);
        if (rawText.isBlank()) {
            rawText = (parsedData.getSummary() != null ? parsedData.getSummary() : "") + " "
                    + (parsedData.getExtractedSkills() != null ? String.join(", ", parsedData.getExtractedSkills()) : "");
        }

        com.dacn.resume_service.dto.ai.AiCvAnalysisRequest aiRequest = com.dacn.resume_service.dto.ai.AiCvAnalysisRequest.builder()
                .rawText(rawText)
                .targetRole(parsedData.getTargetRole())
                .build();

        return aiServiceClient.analyzeCv(aiRequest);
    }

    private ResumeParsedData getOwnedParsedData(CurrentUser user, UUID resumeId) {
        ResumeParsedData data = parsedDataRepository.findByIdAndIsDeletedFalse(resumeId)
                .or(() -> parsedDataRepository.findByResumeIdAndIsDeletedFalse(resumeId))
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CV với mã: " + resumeId));

        if (!data.getUserId().equals(user.getAccountId())) {
            throw new ForbiddenException("Bạn không có quyền thao tác trên CV này");
        }
        return data;
    }

    private ResumeResponse mapToResponse(ResumeParsedData data, Resume resume) {
        ResumeParsedDataResponse dataResponse = null;
        if (data != null) {
            dataResponse = ResumeParsedDataResponse.builder()
                    .id(data.getId())
                    .resumeId(data.getResumeId())
                    .candidateName(data.getCandidateName())
                    .targetRole(data.getTargetRole())
                    .phone(data.getPhone())
                    .email(data.getEmail())
                    .summary(data.getSummary())
                    .totalExperienceMonths(data.getTotalExperienceMonths())
                    .extractedSkills(data.getExtractedSkills())
                    .educations(data.getEducations())
                    .experiences(data.getExperiences())
                    .certifications(data.getCertifications())
                    .updatedByUser(data.getUpdatedByUser())
                    .parsedAt(data.getCreatedAt())
                    .updatedAt(data.getUpdatedAt())
                    .build();
        }

        String fileName = resume != null ? resume.getFileName() : null;
        Long fileSize = resume != null ? resume.getFileSize() : null;
        String fileType = resume != null ? resume.getFileType() : null;
        String fileUrl = resume != null ? resume.getFileUrl() : null;

        return ResumeResponse.builder()
                .id(data != null ? data.getId() : (resume != null ? resume.getId() : null))
                .resumeId(resume != null ? resume.getId() : (data != null ? data.getResumeId() : null))
                .userId(data != null ? data.getUserId() : (resume != null ? resume.getUserId() : null))
                .title(data != null ? data.getTitle() : (fileName != null ? fileName : "Hồ sơ CV"))
                .fileName(fileName)
                .fileSize(fileSize)
                .fileType(fileType)
                .fileUrl(fileUrl)
                .parsingStatus(data != null ? data.getParsingStatus() : "DRAFT")
                .isDefault(data != null ? data.getIsDefault() : false)
                .isDeleted(data != null ? data.getIsDeleted() : false)
                .createdAt(data != null ? data.getCreatedAt() : (resume != null ? resume.getCreatedAt() : null))
                .updatedAt(data != null ? data.getUpdatedAt() : null)
                .parsedData(dataResponse)
                .build();
    }
}
