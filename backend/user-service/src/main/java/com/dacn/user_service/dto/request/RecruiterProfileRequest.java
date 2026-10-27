package com.dacn.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Dữ liệu đầu vào để tạo hoặc cập nhật hồ sơ nhà tuyển dụng.
 */
@Setter
@Getter
@NoArgsConstructor
public class RecruiterProfileRequest {
    @NotBlank @Size(max = 150)
    private String fullName;
    @NotNull
    private UUID companyId;
    @Size(max = 30)
    private String phone;

    public RecruiterProfileRequest(String fullName, UUID companyId, String phone) {
        this.fullName = fullName;
        this.companyId = companyId;
        this.phone = phone;
    }
}
