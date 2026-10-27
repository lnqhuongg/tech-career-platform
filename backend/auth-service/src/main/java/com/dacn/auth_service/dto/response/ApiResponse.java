package com.dacn.auth_service.dto.response;

/**
 * DTO chuẩn hóa cấu trúc response trả về từ API.
 * @param <T> kiểu dữ liệu của phần data.
 * Ví dụ response:
 * {
 *     "success": true,
 *     "message": "Đăng nhập thành công",
 *     "data": {
 *         "accessToken": "...",
 *         "tokenType": "Bearer",
 *         "expiresInSeconds": 3600
 *     }
 * }
 */
public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
}