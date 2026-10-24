package com.dacn.user_service.controller;

import com.dacn.user_service.dto.response.ApiResponse;
import com.dacn.user_service.dto.response.SkillResponse;
import com.dacn.user_service.service.SkillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/** API tra cứu danh mục kỹ năng đang hoạt động dành cho các tài khoản đã xác thực. */
// Controller này dùng chung cho cả 3 accountType, tức là ai cũng xem được skills
// Nhưng chỉ có admin mới có quyền thêm sửa xóa ...
@RestController
@RequestMapping("/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ApiResponse<Page<SkillResponse>> list(
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "name") Pageable pageable
    ) {
        Page<SkillResponse> response = skillService.list(q, pageable).map(SkillResponse::from);
        return new ApiResponse<>(true, "Lấy danh sách kỹ năng thành công", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<SkillResponse> get(@PathVariable UUID id) {
        return new ApiResponse<>(true, "Lấy kỹ năng thành công",
                SkillResponse.from(skillService.getActive(id)));
    }
}
