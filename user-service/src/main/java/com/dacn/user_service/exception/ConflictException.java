package com.dacn.user_service.exception;
/** Ngoại lệ nghiệp vụ dùng khi dữ liệu xung đột với trạng thái hiện tại. */
public class ConflictException extends RuntimeException { public ConflictException(String message) { super(message); } }
