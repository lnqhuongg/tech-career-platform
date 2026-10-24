package com.dacn.user_service.controller;

import com.dacn.user_service.dto.request.RecruiterProfileRequest;
import com.dacn.user_service.dto.response.ApiResponse;
import com.dacn.user_service.dto.response.RecruiterResponse;
import com.dacn.user_service.security.CurrentUser;
import com.dacn.user_service.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/** Các API để nhà tuyển dụng tự quản lý hồ sơ của mình. */
@RestController
@RequestMapping("/recruiter")
public class RecruiterController {

    private final ProfileService profileService;

    public RecruiterController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RecruiterResponse>> create(
            @AuthenticationPrincipal CurrentUser user,
            @Valid @RequestBody RecruiterProfileRequest request
    ) {
        RecruiterResponse response = RecruiterResponse.from(
                profileService.createRecruiter(user, request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Tạo hồ sơ recruiter thành công", response));
    }

    @GetMapping("/me")
    public ApiResponse<RecruiterResponse> mine(@AuthenticationPrincipal CurrentUser user) {
        return new ApiResponse<>(true, "Lấy hồ sơ thành công",
                RecruiterResponse.from(profileService.myRecruiter(user)));
    }

    @PutMapping("/me")
    public ApiResponse<RecruiterResponse> update(
            @AuthenticationPrincipal CurrentUser user,
            @Valid @RequestBody RecruiterProfileRequest request
    ) {
        return new ApiResponse<>(true, "Cập nhật hồ sơ thành công",
                RecruiterResponse.from(profileService.updateRecruiter(user, request)));
    }

    @GetMapping("/{id}")
    public ApiResponse<RecruiterResponse> get(@PathVariable UUID id) {
        return new ApiResponse<>(true, "Lấy hồ sơ recruiter thành công",
                RecruiterResponse.from(profileService.recruiter(id)));
    }
}
