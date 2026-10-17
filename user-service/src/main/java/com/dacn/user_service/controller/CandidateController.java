package com.dacn.user_service.controller;

import com.dacn.user_service.dto.request.CandidateProfileRequest;
import com.dacn.user_service.dto.request.CandidateSkillRequest;
import com.dacn.user_service.dto.request.CandidateSkillUpdateRequest;
import com.dacn.user_service.dto.response.ApiResponse;
import com.dacn.user_service.dto.response.CandidateResponse;
import com.dacn.user_service.dto.response.CandidateSkillResponse;
import com.dacn.user_service.security.CurrentUser;
import com.dacn.user_service.service.ProfileService;
import com.dacn.user_service.service.CandidateSkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/** API tự quản lý hồ sơ/kỹ năng ứng viên và đọc hồ sơ ứng viên đã được xác thực. */
@RestController
@RequestMapping("/candidate")
public class CandidateController {

    private final ProfileService profileService;
    private final CandidateSkillService candidateSkillService;

    public CandidateController(ProfileService profileService, CandidateSkillService candidateSkillService) {
        this.profileService = profileService;
        this.candidateSkillService = candidateSkillService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CandidateResponse>> create(
            @AuthenticationPrincipal CurrentUser user,
            @Valid @RequestBody CandidateProfileRequest request
    ) {
        CandidateResponse response = CandidateResponse.from(
                profileService.createCandidate(user, request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Tạo hồ sơ ứng viên thành công", response));
    }

    @GetMapping("/me")
    public ApiResponse<CandidateResponse> mine(@AuthenticationPrincipal CurrentUser user) {
        return new ApiResponse<>(true, "Lấy hồ sơ thành công",
                CandidateResponse.from(profileService.myCandidate(user)));
    }

    @PutMapping("/me")
    public ApiResponse<CandidateResponse> update(
            @AuthenticationPrincipal CurrentUser user,
            @Valid @RequestBody CandidateProfileRequest request
    ) {
        return new ApiResponse<>(true, "Cập nhật hồ sơ thành công",
                CandidateResponse.from(profileService.updateCandidate(user, request)));
    }

    @GetMapping("/{id}")
    public ApiResponse<CandidateResponse> get(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID id
    ) {
        return new ApiResponse<>(true, "Lấy hồ sơ thành công",
                CandidateResponse.from(profileService.candidate(id)));
    }

    @GetMapping("/me/skills")
    public ApiResponse<List<CandidateSkillResponse>> mySkills(
            @AuthenticationPrincipal CurrentUser user
    ) {
        List<CandidateSkillResponse> response = candidateSkillService.mine(user).stream()
                .map(CandidateSkillResponse::from)
                .toList();
        return new ApiResponse<>(true, "Lấy kỹ năng thành công", response);
    }

    @PostMapping("/me/skills")
    public ResponseEntity<ApiResponse<CandidateSkillResponse>> addSkill(
            @AuthenticationPrincipal CurrentUser user,
            @Valid @RequestBody CandidateSkillRequest request
    ) {
        CandidateSkillResponse response = CandidateSkillResponse.from(
                candidateSkillService.add(user, request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Thêm kỹ năng thành công", response));
    }

    @PutMapping("/me/skills/{id}")
    public ApiResponse<CandidateSkillResponse> updateSkill(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID id,
            @Valid @RequestBody CandidateSkillUpdateRequest request
    ) {
        return new ApiResponse<>(true, "Cập nhật kỹ năng thành công",
                CandidateSkillResponse.from(candidateSkillService.update(user, id, request)));
    }

    @DeleteMapping("/me/skills/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkill(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID id
    ) {
        candidateSkillService.delete(user, id);
    }

    @GetMapping("/{id}/skills")
    public ApiResponse<List<CandidateSkillResponse>> skills(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID id
    ) {
        List<CandidateSkillResponse> response = candidateSkillService.byCandidate(id).stream()
                .map(CandidateSkillResponse::from)
                .toList();
        return new ApiResponse<>(true, "Lấy kỹ năng thành công", response);
    }
}
