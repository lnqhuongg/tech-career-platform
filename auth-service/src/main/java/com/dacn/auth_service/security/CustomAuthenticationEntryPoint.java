package com.dacn.auth_service.security;

import com.dacn.auth_service.dto.response.ApiResponse;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom AuthenticationEntryPoint dùng để xử lý trường hợp
 * request chưa được xác thực nhưng lại truy cập endpoint
 * yêu cầu authentication.
 * Ví dụ: candidate/profile
 * nhưng không có JWT, hoặc JWT không hợp lệ JWT hết hạn
 * Khi đó Spring Security sẽ gọi commence()
 * và trả về HTTP 401.
 */
@Component
public class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        ApiResponse<Void> body = new ApiResponse<>(
                false,
                "Bạn chưa được xác thực hoặc token không hợp lệ",
                null
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(
                objectMapper.writeValueAsString(body)
        );
    }
}