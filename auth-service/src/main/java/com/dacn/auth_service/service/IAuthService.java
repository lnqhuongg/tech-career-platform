package com.dacn.auth_service.service;

import com.dacn.auth_service.dto.request.LoginRequest;
import com.dacn.auth_service.dto.request.RegisterRequest;
import com.dacn.auth_service.dto.response.AuthResponse;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}