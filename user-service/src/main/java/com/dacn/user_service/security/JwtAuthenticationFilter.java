package com.dacn.user_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Validates access JWTs issued by Auth Service and populates Spring Security's
 * SecurityContext. This filter never accepts refresh tokens as access tokens.
 */
@Component
/** Bộ lọc xác minh access token và thiết lập thông tin xác thực cho request. */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ACCESS_TOKEN_TYPE = "access";

    private final SecretKey signingKey;

    public JwtAuthenticationFilter(@Value("${app.jwt.secret-base64}") String secretBase64) {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secretBase64);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("app.jwt.secret-base64 phải là chuỗi Base64 hợp lệ", exception);
        }

        // Keys.hmacShaKeyFor từ chối khóa ngắn hơn độ dài tối thiểu mà thuật toán JWT yêu cầu.
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null
                || !authorizationHeader.startsWith(BEARER_PREFIX)
                || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        if (token.isEmpty()) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if (!ACCESS_TOKEN_TYPE.equals(claims.get("token_type", String.class))) {
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }

            String userIdClaim = claims.get("userId", String.class);
            String accountType = claims.get("accountType", String.class);
            String role = claims.get("role", String.class);
            String email = claims.getSubject();

            if (userIdClaim == null || userIdClaim.isBlank()
                    || accountType == null || accountType.isBlank()
                    || email == null || email.isBlank()) {
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }

            UUID accountId = UUID.fromString(userIdClaim);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(accountType));

            // Giữ nguyên tên quyền; SecurityConfig kiểm tra bằng hasAuthority("ADMIN").
            if (role != null && !role.isBlank()) {
                authorities.add(new SimpleGrantedAuthority(role));
                // Chấp nhận claim quyền ở dạng ADMIN hoặc dạng theo Spring là ROLE_ADMIN.
                if (role.startsWith("ROLE_")) {
                    authorities.add(new SimpleGrantedAuthority(role.substring("ROLE_".length())));
                } else {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
            }

            var authentication = new UsernamePasswordAuthenticationToken(
                    new CurrentUser(accountId, email, accountType),
                    null,
                    authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException | IllegalArgumentException exception) {
            // Không tiết lộ chi tiết lỗi phân tích token cho phía gọi. Các endpoint được bảo vệ
            // sẽ bị từ chối bởi AuthenticationEntryPoint đã cấu hình và trả về 401.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
