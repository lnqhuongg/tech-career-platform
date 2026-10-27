package com.dacn.auth_service.dto.response;

import com.dacn.auth_service.model.AccountType;
import com.dacn.auth_service.model.UserAccount;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class AccountResponse {
    private final UUID id;
    private final String email;
    private final AccountType accountType;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public AccountResponse(UserAccount user) {
        this.id = user.getId();
        this.email = user.getUsername();
        this.accountType = user.getAccountType();
        this.isActive = user.isActive();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}