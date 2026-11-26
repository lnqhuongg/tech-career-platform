package com.dacn.resume_service.controller;

import com.dacn.resume_service.client.AiServiceClient;
import com.dacn.resume_service.dto.ai.AiChatRequest;
import com.dacn.resume_service.dto.ai.AiChatResponse;
import com.dacn.resume_service.dto.request.SaveParsedDataRequest;
import com.dacn.resume_service.dto.request.UpdateResumeRequest;
import com.dacn.resume_service.dto.response.ApiResponse;
import com.dacn.resume_service.dto.response.ResumeResponse;
import com.dacn.resume_service.security.CurrentUser;
import com.dacn.resume_service.service.IResumeService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resumes")
public class ResumeController {

    private final IResumeService resumeService;
    private final AiServiceClient aiServiceClient;

    public ResumeController(IResumeService resumeService, AiServiceClient aiServiceClient) {
        this.resumeService = resumeService;
        this.aiServiceClient = aiServiceClient;
    }

    /**
     * 1. ỨNG VIÊN TẢI LÊN FILE CV (PDF)
     * Hệ thống lưu file, bóc tách text và sinh bản nháp DRAFT cho ứng viên kiểm tra.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResumeResponse>> upload(
            @AuthenticationPrincipal CurrentUser user,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title
    ) {
        ResumeResponse response = resumeService.uploadResume(user, file, title);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Tải lên và bóc tách CV thành công", response));
    }

    /**
     * 2. LẤY TẤT CẢ CV CỦA ỨNG VIÊN ĐANG ĐĂNG NHẬP
     */
    @GetMapping("/me")
    public ApiResponse<List<ResumeResponse>> getMyResumes(
            @AuthenticationPrincipal CurrentUser user
    ) {
        List<ResumeResponse> list = resumeService.getMyResumes(user);
        return new ApiResponse<>(true, "Lấy danh sách CV thành công", list);
    }

    /**
     * 3. LẤY CHI TIẾT MỘT CV (KÈM DỮ LIỆU CẤU TRÚC ĐÃ BÓC TÁCH)
     */
    @GetMapping("/{id}")
    public ApiResponse<ResumeResponse> getById(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID id
    ) {
        ResumeResponse response = resumeService.getResumeById(user, id);
        return new ApiResponse<>(true, "Lấy thông tin CV thành công", response);
    }

    /**
     * 4. CẬP NHẬT TIÊU ĐỀ CV HOẶC ĐẶT LÀM MẶC ĐỊNH
     */
    @PutMapping("/{id}")
    public ApiResponse<ResumeResponse> update(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateResumeRequest request
    ) {
        ResumeResponse response = resumeService.updateResume(user, id, request);
        return new ApiResponse<>(true, "Cập nhật CV thành công", response);
    }

    /**
     * 5. HUMAN-IN-THE-LOOP: ỨNG VIÊN XEM, CHỈNH SỬA VÀ LƯU DỮ LIỆU BÓC TÁCH
     * Sau khi xác nhận lưu, trạng thái chuyển thành COMPLETED (sẵn sàng nộp đơn).
     */
    @PutMapping("/{id}/parsed-data")
    public ApiResponse<ResumeResponse> saveParsedData(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID id,
            @RequestBody SaveParsedDataRequest request
    ) {
        ResumeResponse response = resumeService.saveParsedData(user, id, request);
        return new ApiResponse<>(true, "Xác nhận và lưu dữ liệu bóc tách CV thành công", response);
    }

    /**
     * 6. ĐẶT LÀM CV MẶC ĐỊNH
     */
    @PatchMapping("/{id}/default")
    public ApiResponse<ResumeResponse> setDefault(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID id
    ) {
        ResumeResponse response = resumeService.setDefault(user, id);
        return new ApiResponse<>(true, "Đặt làm CV mặc định thành công", response);
    }

    /**
     * 7. XÓA MỀM CV
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID id
    ) {
        resumeService.deleteResume(user, id);
        return new ApiResponse<>(true, "Xóa CV thành công", null);
    }

    /**
     * 8. TẢI FILE PDF VỀ MÁY (DOWNLOAD ATTACHMENT)
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID id
    ) {
        Resource resource = resumeService.loadFileResource(id, user);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * 9. XEM FILE PDF TRỰC TIẾP TRÊN TRÌNH DUYỆT (INLINE PREVIEW)
     */
    @GetMapping("/{id}/view")
    public ResponseEntity<Resource> viewFile(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID id
    ) {
        Resource resource = resumeService.loadFileResource(id, user);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * 10. LẤY VĂN BẢN THÔ (DÀNH CHO DỊCH VỤ AI ĐỐI SÁNH / EMBEDDING)
     */
    @GetMapping("/{id}/raw-text")
    public ApiResponse<String> getRawText(
            @PathVariable UUID id
    ) {
        String rawText = resumeService.getRawText(id);
        return new ApiResponse<>(true, "Lấy văn bản thô CV thành công", rawText);
    }

    /**
     * 11. TRỢ LÝ AI TƯ VẤN HƯỚNG NGHIỆP (CAREER COACH CHATBOT)
     */
    @PostMapping("/ai/chat")
    public ApiResponse<AiChatResponse> chatWithAi(
            @AuthenticationPrincipal CurrentUser user,
            @RequestBody AiChatRequest request
    ) {
        AiChatResponse response = aiServiceClient.chat(request);
        return new ApiResponse<>(true, "Tư vấn hướng nghiệp AI thành công", response);
    }

    /**
     * 12. PHÂN TÍCH VÀ ĐÁNH GIÁ CHẤT LƯỢNG CV BẰNG AI (UC13)
     */
    @GetMapping("/{id}/analysis")
    public ApiResponse<com.dacn.resume_service.dto.ai.AiCvAnalysisResponse> analyzeCv(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID id
    ) {
        com.dacn.resume_service.dto.ai.AiCvAnalysisResponse response = resumeService.analyzeResumeQuality(user, id);
        return new ApiResponse<>(true, "Phân tích và đánh giá chất lượng CV thành công", response);
    }
}
