package com.dacn.auth_service.controller;

import com.dacn.auth_service.dto.request.LoginRequest;
import com.dacn.auth_service.dto.request.RegisterRequest;
import com.dacn.auth_service.dto.response.AuthResponse;
import com.dacn.auth_service.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
