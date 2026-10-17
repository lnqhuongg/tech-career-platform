package com.dacn.auth_service.controller;

import com.dacn.auth_service.dto.request.*;
import com.dacn.auth_service.dto.response.AccountResponse;
import com.dacn.auth_service.dto.response.ApiResponse;
import com.dacn.auth_service.dto.response.AuthResponse;
import com.dacn.auth_service.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        AuthResponse response = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                new ApiResponse<>(
                        true,
                        "Đăng ký thành công",
                        response
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Đăng nhập thành công",
                        response
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Làm mới token thành công", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đăng xuất thành công", null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AccountResponse>> getMyAccount(Authentication authentication) {
        AccountResponse response = authService.getMyAccount(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy thông tin tài khoản thành công", response));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        authService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đổi mật khẩu thành công", null));
    }
}
