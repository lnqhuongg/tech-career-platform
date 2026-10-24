package com.dacn.auth_service.controller;

// File này để tui test JWT Authentication trong Postman thui nha
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/protected")
    public Map<String, String> protectedEndpoint() {
        return Map.of(
                "message", "JWT Authentication thành công!",
                "status", "Authenticated"
        );
    }
}
