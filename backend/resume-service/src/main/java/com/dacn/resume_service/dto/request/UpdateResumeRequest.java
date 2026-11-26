package com.dacn.resume_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResumeRequest {

    @NotBlank(message = "Tiêu đề CV không được để trống")
    private String title;

    private Boolean isDefault;
}
