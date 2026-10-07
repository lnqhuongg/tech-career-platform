package com.dacn.application_service.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ AppException.class })
    public ResponseEntity<String> handleExceptionA(Exception e) {
        return ResponseEntity.status(432).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e) {
        // e.printStackTrace();
        return ResponseEntity.status(500).body("Unknow error");
    }
}
