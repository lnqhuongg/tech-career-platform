package com.dacn.auth_service.controller;

import com.dacn.auth_service.dto.request.LoginRequest;
import com.dacn.auth_service.dto.request.RegisterRequest;
import com.dacn.auth_service.dto.response.ApiResponse;
import com.dacn.auth_service.dto.response.AuthResponse;
import com.dacn.auth_service.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
