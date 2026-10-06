package com.dacn.auth_service.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.jsonwebtoken.JwtException;

import java.io.IOException;

/*
 * Class này chịu trách nhiệm xác thực người dùng thông qua JWT trong các HTTP request gửi đến Auth Service.
 * Class kế thừa OncePerRequestFilter, giúp filter được thực thi một lần trong mỗi request dispatch thông thường.
 * Các nhiệm vụ chính:
 *  + Đọc JWT từ Authorization header.
 *  + Kiểm tra và loại bỏ tiền tố "Bearer ".
 *  + Trích xuất email (subject) từ JWT.
 *  + Tải thông tin tài khoản hiện tại từ database.
 *  + Xác minh JWT và trạng thái tài khoản.
 *  + Tạo Authentication chứa thông tin người dùng và authorities.
 *  + Lưu Authentication vào SecurityContextHolder.
 *  + Cho request tiếp tục đi qua Security Filter Chain.
 *
 * Lưu ý:
 * Filter này xác thực danh tính người dùng.
 * Việc kiểm tra người dùng có quyền truy cập endpoint hay không sẽ được Spring Security thực hiện ở bước authorization.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Lấy JWT từ Authorization header
        final String authHeader = request.getHeader("Authorization");

        // Nếu không có Bearer Token thì tiếp tục chuỗi filter
        // request login sẽ không chứa Bearer Token => Tiếp tục chuỗi filter nên sẽ chuyển sang UsernamePasswordAuthenticationFilter
        // => Bay thẳng tới bước kiểm tra tài khoản mật khẩu
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Loại bỏ tiền tố "Bearer " để lấy JWT
        final String jwt = authHeader.substring(7);

        try {
            // Trích xuất email từ JWT
            // Bước này đã parse => nếu hết hạn thì sẽ ném exception
            Claims claims = jwtService.parseToken(jwt);
            String userEmail = claims.getSubject();

            // Chỉ xác thực nếu SecurityContext chưa có Authentication
            if (userEmail != null
                    && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {
                // Tìm tài khoản theo email
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // Kiểm tra token có thuộc về user và account hiện tại còn hợp lệ khng
                if (jwtService.isTokenValid(claims, userDetails)
                        && userDetails.isEnabled()
                        && userDetails.isAccountNonExpired()
                        && userDetails.isAccountNonLocked()
                        && userDetails.isCredentialsNonExpired()) {

                    // Tạo Authentication chứa thông tin và authorities
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // Gắn thông tin request vào Authentication
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // Lưu Authentication vào SecurityContext => đánh dấu request đã được xác thực
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
            SecurityContextHolder.clearContext();
        }
        /*
        * Cho phép request và response tiếp tục đi qua các filter còn lại trong Security Filter Chain.
        * Cụ thể ở đây là UsernamePasswordAuthenticationFilte trong class SecurityConfig
        * Nếu JWT hợp lệ, Authentication đã được thiết lập trước đó
        * sẽ được Spring Security sử dụng trong quá trình phân quyền.
        * */
        filterChain.doFilter(request, response);
    }
}