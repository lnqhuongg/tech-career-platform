package com.dacn.auth_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// Class này dành cho quản trị viên để vô hiệu hóa/ kích hoạt tài khoản
@Getter
@Setter
public class UpdateAccountStatusRequest {
    @NotNull(message = "Trạng thái isActive không được để trống")
    private Boolean isActive;
}