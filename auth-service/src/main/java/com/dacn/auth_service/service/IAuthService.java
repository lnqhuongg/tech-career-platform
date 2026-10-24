package com.dacn.auth_service.service;

import com.dacn.auth_service.dto.request.*;
import com.dacn.auth_service.dto.response.AccountResponse;
import com.dacn.auth_service.dto.response.AuthResponse;

import java.util.UUID;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(RefreshTokenRequest request);
    AccountResponse getMyAccount(String email);
    void changePassword(String email, ChangePasswordRequest request);
    AccountResponse updateAccountStatus(UUID userId, boolean isActive);
}