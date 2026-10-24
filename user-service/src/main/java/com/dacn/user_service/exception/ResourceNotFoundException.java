package com.dacn.user_service.exception;
/** Ngoại lệ dùng khi không tìm thấy tài nguyên được yêu cầu. */
public class ResourceNotFoundException extends RuntimeException { public ResourceNotFoundException(String message) { super(message); } }
