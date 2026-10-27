package com.dacn.user_service.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Chuẩn hóa cấu trúc phản hồi của API.
 *
 * @param <T> kiểu dữ liệu được trả về trong trường data
 */
@Setter
@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
