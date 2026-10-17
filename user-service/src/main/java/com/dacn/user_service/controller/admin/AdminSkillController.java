package com.dacn.user_service.controller.admin;

import com.dacn.user_service.dto.request.SkillRequest;
import com.dacn.user_service.dto.response.ApiResponse;
import com.dacn.user_service.dto.response.SkillResponse;
import com.dacn.user_service.service.AdminSkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/** Các thao tác dành riêng cho ADMIN để quản lý danh mục kỹ năng chung. */
@RestController
@RequestMapping("/admin/skills")
public class AdminSkillController {

    private final AdminSkillService skillService;

    public AdminSkillController(AdminSkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SkillResponse>> create(
            @Valid @RequestBody SkillRequest request
    ) {
        SkillResponse response = SkillResponse.from(skillService.create(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Tạo kỹ năng thành công", response));
    }

    @PutMapping("/{id}")
    public ApiResponse<SkillResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody SkillRequest request
    ) {
        return new ApiResponse<>(true, "Cập nhật kỹ năng thành công",
                SkillResponse.from(skillService.update(id, request)));
    }
}
