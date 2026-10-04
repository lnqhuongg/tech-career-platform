package com.dacn.auth_service.dto.response;

import com.dacn.auth_service.model.AccountType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AuthResponse {

    private final UUID userId;
    private final String email;
    private final AccountType accountType;
    private final String message;

    private final String accessToken;
    private final String tokenType;
    private final long expiresInSeconds;

    // Constructor dành cho Register
    public AuthResponse(
            UUID userId,
            String email,
            AccountType accountType,
            String message
    ) {
        this(
                userId,
                email,
                accountType,
                message,
                null,
                null,
                0
        );
    }

    // Constructor dành cho Login
    public AuthResponse(
            UUID userId,
            String email,
            AccountType accountType,
            String message,
            String accessToken,
            String tokenType,
            long expiresInSeconds
    ) {
        this.userId = userId;
        this.email = email;
        this.accountType = accountType;
        this.message = message;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresInSeconds = expiresInSeconds;
    }
}