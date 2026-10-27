
package com.dacn.auth_service.repository;

import com.dacn.auth_service.model.RefreshToken;
import com.dacn.auth_service.model.UserAccount;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * Truy vấn và cập nhật Refresh Token trong database.
 */
public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, UUID> {

    /**
     * Khóa bản ghi khi refresh để tránh hai request cùng sử dụng một token.
     * Phải được gọi trong transaction.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT r
        FROM RefreshToken r
        JOIN FETCH r.user
        WHERE r.tokenHash = :tokenHash
        """)
    Optional<RefreshToken> findByTokenHashForUpdate(
            @Param("tokenHash") String tokenHash
    );

    /**
     * Thu hồi tất cả Refresh Token chưa bị thu hồi của một tài khoản.
     */
    @Modifying
    @Query("""
        UPDATE RefreshToken r
        SET r.revoked = true
        WHERE r.user = :user
          AND r.revoked = false
        """)
    int revokeAllUserTokens(@Param("user") UserAccount user);
}