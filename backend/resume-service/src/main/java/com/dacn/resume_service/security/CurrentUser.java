package com.dacn.resume_service.security;

import java.util.UUID;

/**
 * Đại diện cho danh tính tài khoản đã được xác thực và trích xuất từ access token JWT.
 */
public class CurrentUser {

    private final UUID accountId;
    private final String email;
    private final String accountType;

    public CurrentUser(UUID accountId, String email, String accountType) {
        this.accountId = accountId;
        this.email = email;
        this.accountType = accountType;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getEmail() {
        return email;
    }

    public String getAccountType() {
        return accountType;
    }
}
