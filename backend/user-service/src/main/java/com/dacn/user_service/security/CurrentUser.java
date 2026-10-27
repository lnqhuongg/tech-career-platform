package com.dacn.user_service.security;

import java.util.UUID;

/**
 * Đại diện cho danh tính tài khoản đã được xác thực và trích xuất từ access token.
 * Đối tượng này chỉ chứa thông tin cần thiết để xử lý nghiệp vụ, không chứa mật khẩu hay token.
 */
public class CurrentUser {

    /** Mã tài khoản do Auth Service cấp và được xác minh từ JWT. */
    private final UUID accountId;

    /** Email của tài khoản đã xác thực, nếu JWT cung cấp claim này. */
    private final String email;

    /** Loại tài khoản nghiệp vụ, ví dụ CANDIDATE hoặc RECRUITER. */
    private final String accountType;

    /** Tạo đối tượng danh tính từ các claims đã được xác minh. */
    public CurrentUser(UUID accountId, String email, String accountType) {
        this.accountId = accountId;
        this.email = email;
        this.accountType = accountType;
    }

    public UUID getAccountId() { return accountId; }
    public String getEmail() { return email; }
    public String getAccountType() { return accountType; }
}
