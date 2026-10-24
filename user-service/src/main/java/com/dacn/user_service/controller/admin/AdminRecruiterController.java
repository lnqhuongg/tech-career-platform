package com.dacn.user_service.controller.admin;

import com.dacn.user_service.dto.request.VerificationRequest;
import com.dacn.user_service.dto.response.ApiResponse;
import com.dacn.user_service.dto.response.RecruiterResponse;
import com.dacn.user_service.service.AdminProfileService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/** Các API tìm kiếm, xem chi tiết và xác minh nhà tuyển dụng dành riêng cho ADMIN. */
@RestController
@RequestMapping("/admin/recruiter")
public class AdminRecruiterController {

    private final AdminProfileService profileService;

    public AdminRecruiterController(AdminProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ApiResponse<Page<RecruiterResponse>> list(
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<RecruiterResponse> response = profileService.recruiters(q, pageable)
                .map(RecruiterResponse::from);
        return new ApiResponse<>(true, "Lấy danh sách recruiter thành công", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<RecruiterResponse> get(@PathVariable UUID id) {
        return new ApiResponse<>(true, "Lấy hồ sơ recruiter thành công",
                RecruiterResponse.from(profileService.recruiterById(id)));
    }

    @PatchMapping("/{id}/verification")
    public ApiResponse<RecruiterResponse> verify(
            @PathVariable UUID id,
            @Valid @RequestBody VerificationRequest request
    ) {
        return new ApiResponse<>(true, "Cập nhật xác minh thành công",
                RecruiterResponse.from(
                        profileService.verifyRecruiter(id, request.getVerificationStatus())));
    }
}
