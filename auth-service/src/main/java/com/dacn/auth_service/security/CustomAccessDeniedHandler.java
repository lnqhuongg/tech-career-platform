package com.dacn.auth_service.security;

import com.dacn.auth_service.dto.response.ApiResponse;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom AccessDeniedHandler dùng để xử lý trường hợp
 * user đã đăng nhập nhưng không có đủ quyền truy cập tài nguyên.
 * Ví dụ:
 * User đã có JWT hợp lệ có ROLE_USER nhưng lại gọi: admin/**
 * Khi đó Spring Security trả về HTTP 403 Forbidden.
 */
@Component
public class CustomAccessDeniedHandler
        implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        ApiResponse<Void> body = new ApiResponse<>(
                false,
                "Bạn không có quyền truy cập tài nguyên này",
                null
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        response.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );

        response.getWriter().write(
                objectMapper.writeValueAsString(body)
        );
    }
}