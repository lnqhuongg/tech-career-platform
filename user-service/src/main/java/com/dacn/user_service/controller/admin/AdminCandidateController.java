package com.dacn.user_service.controller.admin;

import com.dacn.user_service.dto.response.ApiResponse;
import com.dacn.user_service.dto.response.CandidateResponse;
import com.dacn.user_service.service.AdminProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/** Các API quản trị hồ sơ ứng viên, chỉ dành cho ADMIN. */
@RestController
@RequestMapping("/admin/candidate")
public class AdminCandidateController {

    private final AdminProfileService profileService;

    public AdminCandidateController(AdminProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ApiResponse<Page<CandidateResponse>> list(
            @RequestParam(required = false) String location,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<CandidateResponse> response = profileService
                .candidates(location, pageable)
                .map(CandidateResponse::from);
        return new ApiResponse<>(true, "Lấy danh sách ứng viên thành công", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<CandidateResponse> get(@PathVariable UUID id) {
        return new ApiResponse<>(true, "Lấy hồ sơ ứng viên thành công",
                CandidateResponse.from(profileService.findCandidateById(id)));
    }
}
