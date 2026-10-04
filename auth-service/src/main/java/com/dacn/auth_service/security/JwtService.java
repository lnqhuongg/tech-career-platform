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
import java.util.function.Function;

/*
* Class này có vai trò xử lý các thao tác với JWT:
* Tạo token: generateToken()
* Đọc thông tin từ token: tên tài khoản (extractUsername())
* Kiểm tra tính hợp lệ của token
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

    // ======================================================
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    /*
    * Ở đây 3 th extract này lồng nhau
    * extractUsername -> extractClaim -> extractAllClaims
    * Đại khái là:
    * + extractAllClaims Parse JWT, xác minh chữ ký và lấy payload chứa tất cả claims
    * + Sau đó extractClaims sẽ lấy ra claims cụ thể mà mình muốn, thông qua param claimsResolver
    * + extractUsername là hàm tái sử dụng extractClaim, cụ thể lấy ra subject của payload (subject chính là username luôn)
    * => Có thể tái sử dụng extractClaim để lấy các claim khác (extractIssuedAt chẳng hạn)
    * */
    //==========================================================

    // Kiểm tra tính hợp lệ của token
    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {
        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // Kiểm tra token hết hạn chưa, đang là hàm tính năng cho hàm kiểm tra tính hợp lệ
    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(
                token,
                Claims::getExpiration
        );

        return !expiration.after(new Date());
    }

    public long getExpirationSeconds() {
        return jwtExpiration / 1000;
    }
}