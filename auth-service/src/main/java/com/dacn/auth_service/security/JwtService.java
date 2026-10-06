package com.dacn.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

/*
* Class này có vai trò xử lý các thao tác với JWT:
* Tạo token: generateToken()
 * Parse và xác thực token: parseToken()
 * Kiểm tra token có thuộc về user hiện tại: isTokenValid()
* */
@Service
public class JwtService {

    private final SecretKey signingKey; // Khóa bí mật dùng cho thuật toán ký JWT
    private final long jwtExpiration;

    public JwtService(
            @Value("${app.jwt.secret-base64}") String secretBase64,
            @Value("${app.jwt.expiration-ms:3600000}") long jwtExpiration
    ) {
        // Giải mã secret từ base64 thành mảng khóa byte
        byte[] keyBytes = Decoders.BASE64.decode(secretBase64);

        // Khóa byte phải dài tối thiểu 32 byte
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(
                    "JWT secret phải có tối thiểu 32 bytes"
            );
        }

        // Chuyển khóa byte thành khóa bí mật
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtExpiration = jwtExpiration;
    }

    // Tạo JWT
    public String generateToken(UserDetails userDetails) {

        Instant now = Instant.now();
        Instant expiration = now.plusMillis(jwtExpiration);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(signingKey)
                .compact();
    }

    // Parse và kiểm tra thời hạn của token
    public Claims parseToken(String token) {
        return Jwts.parser() //Tạo ParserBuilder
                .verifyWith(signingKey) // Cấu hình key để xác minh chữ ký
                .build() // Tạo JwtParser hoàn chỉnh
                .parseSignedClaims(token)
                /*
                * Parse JwtParser đã được build
                * Verify chữ ký
                * Validate JWT (bao gồm việc xác minh JWT còn hạn hay không)
                * */
                .getPayload(); // Lấy phần Payload của JWT  (Chính là Claims á)
    }

    // Kiểm tra tính hợp lệ của token
    // Token đã được kiểm tra thời hạn khi được parse -> Chỉ cần kiểm tra thêm subject có khớp với
    // username hiện tại không
    public boolean isTokenValid(
            Claims claims,
            UserDetails userDetails
    ) {
        String username = claims.getSubject();

        return username != null && username.equals(userDetails.getUsername());
    }

    public long getExpirationSeconds() {
        return jwtExpiration / 1000;
    }
}