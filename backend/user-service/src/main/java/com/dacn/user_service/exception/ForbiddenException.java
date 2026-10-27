package com.dacn.user_service.exception;

/** Biểu thị người dùng đã xác thực không được phép thực hiện thao tác này. */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
