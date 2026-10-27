package com.dacn.auth_service.controller;

import com.dacn.auth_service.dto.request.UpdateAccountStatusRequest;
import com.dacn.auth_service.dto.response.AccountResponse;
import com.dacn.auth_service.dto.response.ApiResponse;
import com.dacn.auth_service.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// Class dành cho Role_Admin khóa/mở khóa tài khoản
@RestController
@RequestMapping("/admin/accounts")
public class AdminAccountController {

    private final IAuthService authService;

    public AdminAccountController(IAuthService authService) {
        this.authService = authService;
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<AccountResponse>> updateStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateAccountStatusRequest request
    ) {
        AccountResponse response = authService.updateAccountStatus(userId, request.getIsActive());
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Cập nhật trạng thái tài khoản thành công",
                response
        ));
    }
}