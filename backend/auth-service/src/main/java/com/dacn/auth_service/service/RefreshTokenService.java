
package com.dacn.auth_service.service;

import com.dacn.auth_service.model.RefreshToken;
import com.dacn.auth_service.model.UserAccount;
import com.dacn.auth_service.repository.RefreshTokenRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

/**
 * Quản lý việc tạo, xác minh và thu hồi Refresh Token.
 * Token nguyên bản chỉ được trả cho client; database lưu SHA-256 hash.
 */
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshExpirationDays;

    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${app.jwt.refresh-expiration-days:7}")
            long refreshExpirationDays
    ) {
        if (refreshExpirationDays <= 0) {
            throw new IllegalArgumentException(
                    "Thời hạn Refresh Token phải lớn hơn 0"
            );
        }

        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshExpirationDays = refreshExpirationDays;
    }

    /**
     * Sinh token ngẫu nhiên 256-bit và lưu hash vào database.
     * Nếu được gọi từ AuthServiceImpl, thao tác sẽ tham gia transaction hiện tại.
     */
    @Transactional
    public String createRefreshToken(UserAccount user) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setTokenHash(hashToken(rawToken));
        token.setExpiresAt(
                LocalDateTime.now().plusDays(refreshExpirationDays)
        );

        refreshTokenRepository.save(token);

        return rawToken;
    }

    /**
     * Tìm và khóa token, sau đó kiểm tra thời hạn, trạng thái thu hồi và trạng thái tài khoản.
     */
    @Transactional
    public RefreshToken validateRefreshTokenForUpdate(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw new BadCredentialsException(
                    "Refresh Token không hợp lệ"
            );
        }

        RefreshToken token = refreshTokenRepository
                .findByTokenHashForUpdate(hashToken(rawToken))
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Refresh Token không hợp lệ"
                        )
                );

        if (token.isRevoked() || token.isExpired()) {
            throw new BadCredentialsException(
                    "Refresh Token đã hết hạn hoặc bị thu hồi"
            );
        }

        UserAccount user = token.getUser();

        if (!user.isActive()
                || !user.isEnabled()
                || !user.isAccountNonExpired()
                || !user.isAccountNonLocked()
                || !user.isCredentialsNonExpired()) {
            throw new BadCredentialsException(
                    "Tài khoản không còn hợp lệ"
            );
        }

        return token;
    }

    /** Thu hồi token đã được lấy trong transaction hiện tại. */
    @Transactional
    public void revoke(RefreshToken token) {
        token.setRevoked(true);
    }

    /**
     * Thu hồi token theo giá trị client gửi lên.
     * Không tìm thấy token cũng không gây lỗi, giúp logout có tính idempotent.
     */
    @Transactional
    public void revokeToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }

        refreshTokenRepository
                .findByTokenHashForUpdate(hashToken(rawToken))
                .ifPresent(token -> token.setRevoked(true));
    }

    /** Thu hồi mọi phiên Refresh Token của một tài khoản. */
    @Transactional
    public void revokeAllUserTokens(UserAccount user) {
        refreshTokenRepository.revokeAllUserTokens(user);
    }

    /** Chuyển token nguyên bản thành SHA-256 hash để lưu trong database. */
    private String hashToken(String token) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(
                    "Không thể khởi tạo SHA-256", ex
            );
        }
    }
}