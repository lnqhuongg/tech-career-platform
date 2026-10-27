package com.dacn.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dữ liệu tạo mới hoặc cập nhật kỹ năng trong danh mục chung.
 */
@Setter
@Getter
@NoArgsConstructor
public class SkillRequest {
    @NotBlank @Size(max = 100)
    private String name;
    @Size(max = 2000)
    private String description;
    private Boolean active;

    public SkillRequest(String name, String description, Boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }
}
