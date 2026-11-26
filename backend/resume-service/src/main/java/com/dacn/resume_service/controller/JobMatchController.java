package com.dacn.resume_service.controller;

import com.dacn.resume_service.dto.request.EvaluateJobRequest;
import com.dacn.resume_service.dto.response.ApiResponse;
import com.dacn.resume_service.dto.response.JobMatchEvaluationResponse;
import com.dacn.resume_service.security.CurrentUser;
import com.dacn.resume_service.service.IJobMatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resumes/{resumeId}/evaluations")
public class JobMatchController {

    private final IJobMatchService jobMatchService;

    public JobMatchController(IJobMatchService jobMatchService) {
        this.jobMatchService = jobMatchService;
    }

    /**
     * 1. ĐỐI SÁNH CV VỚI JOB VÀ PHÂN TÍCH KHOẢNG CÁCH KỸ NĂNG (SKILL GAP)
     * Tính điểm % phù hợp, chỉ ra kỹ năng đã đáp ứng và kỹ năng còn thiếu, lưu kết quả.
     */
    @PostMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobMatchEvaluationResponse>> evaluateMatch(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID resumeId,
            @PathVariable UUID jobId,
            @RequestBody(required = false) EvaluateJobRequest request
    ) {
        JobMatchEvaluationResponse response = jobMatchService.evaluateMatch(user, resumeId, jobId, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Phân tích độ phù hợp và khoảng cách kỹ năng thành công", response));
    }

    /**
     * 2. XEM LẠI KẾT QUẢ ĐÁNH GIÁ ĐÃ LƯU CỦA MỘT JOB
     */
    @GetMapping("/{jobId}")
    public ApiResponse<JobMatchEvaluationResponse> getEvaluation(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID resumeId,
            @PathVariable UUID jobId
    ) {
        JobMatchEvaluationResponse response = jobMatchService.getEvaluation(user, resumeId, jobId);
        return new ApiResponse<>(true, "Lấy kết quả đánh giá thành công", response);
    }

    /**
     * 3. LẤY TOÀN BỘ DANH SÁCH CÁC JOB ĐÃ ĐƯỢC ĐỐI SÁNH VỚI CV NÀY
     */
    @GetMapping
    public ApiResponse<List<JobMatchEvaluationResponse>> getEvaluationsByResume(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID resumeId
    ) {
        List<JobMatchEvaluationResponse> list = jobMatchService.getEvaluationsByResume(user, resumeId);
        return new ApiResponse<>(true, "Lấy danh sách đánh giá công việc của CV thành công", list);
    }
}
