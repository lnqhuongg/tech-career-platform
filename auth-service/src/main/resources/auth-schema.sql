-- =====================================================
-- 1. USER ACCOUNTS
-- =====================================================

CREATE TABLE user_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    account_type VARCHAR(30) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_account_type
        CHECK (
            account_type IN ('CANDIDATE', 'RECRUITER', 'ADMIN')
        )
);

-- =====================================================
-- 2. INDEXES
-- =====================================================

CREATE UNIQUE INDEX uq_user_accounts_email_ci
    ON user_accounts (LOWER(email));

CREATE INDEX idx_user_accounts_account_type
    ON user_accounts(account_type);


-- =====================================================
-- 3. REFRESH TOKENS (Phục vụ Refresh Token & Logout)
-- =====================================================

CREATE TABLE refresh_tokens (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                user_id UUID NOT NULL
                                    REFERENCES user_accounts(id) ON DELETE CASCADE,
                                token_hash VARCHAR(64) NOT NULL UNIQUE,
                                expires_at TIMESTAMP NOT NULL,
                                revoked BOOLEAN NOT NULL DEFAULT FALSE,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_refresh_tokens_user_id
    ON refresh_tokens(user_id);