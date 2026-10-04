-- =====================================================
-- AI-POWERED CAREER PLATFORM
-- AUTH SERVICE DATABASE SCHEMA
-- PostgreSQL
-- =====================================================


-- =====================================================
-- 1. USER ACCOUNTS
-- Lưu thông tin tài khoản người dùng
-- =====================================================

CREATE TABLE user_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    account_type VARCHAR(30) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Chỉ cho phép các loại tài khoản hợp lệ
    CONSTRAINT chk_account_type
        CHECK (
            account_type IN (
                'CANDIDATE',
                'RECRUITER'
            )
        )
);

-- =====================================================
-- 2. USER ROLES
-- Lưu các quyền được cấp cho tài khoản
-- =====================================================

CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Một user không thể có cùng một role hai lần
    PRIMARY KEY (user_id, role_name),

    -- Xóa tài khoản thì xóa các quyền liên quan
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
        REFERENCES user_accounts(id)
        ON DELETE CASCADE,

    -- Giới hạn các role hợp lệ
    CONSTRAINT chk_role_name
        CHECK (
            role_name IN (
                'ROLE_USER',
                'ROLE_ADMIN',
                'ROLE_MODERATOR'
            )
        )
);


-- =====================================================
-- 3. INDEXES
-- Hỗ trợ truy vấn
-- =====================================================

CREATE UNIQUE INDEX uq_user_accounts_email_ci
    ON user_accounts (LOWER(email));

CREATE INDEX idx_user_accounts_account_type
    ON user_accounts(account_type);

CREATE INDEX idx_user_roles_role_name
    ON user_roles(role_name);


-- =====================================================
-- 4. INITIAL DATA
-- =====================================================

-- Không cần INSERT bảng roles vì Role là Java Enum.

-- Tài khoản mới sẽ được AuthService tự động gán ROLE_USER.
-- Không tạo sẵn tài khoản ADMIN trong schema.
