package com.dacn.auth_service.exception;

/**
 * Exception nghiệp vụ được sử dụng khi email đăng ký đã tồn tại trong hệ thống.
 * Đây là Business Exception vì tình huống này không phải lỗi kỹ thuật của database,
 * mà là một trường hợp nghiệp vụ mà hệ thống cần xử lý và trả về HTTP 409 CONFLICT.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}