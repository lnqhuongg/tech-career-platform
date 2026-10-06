package com.dacn.auth_service.config;

import com.dacn.auth_service.security.CustomAccessDeniedHandler;
import com.dacn.auth_service.security.CustomAuthenticationEntryPoint;
import com.dacn.auth_service.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/*
* Class này chịu trách nhiệm khai báo và tùy chỉnh các cấu hình liên quan đến Spring Security
* Cụ thể là nó chủ yếu khai báo và quy định cách mà Spring Security sẽ phải hoạt động trong Service của mình:
* + Quy định cách xử lý các HTTP request qua hệ thống bảo mật (securityFilterChain), bao gồm các quy tắc xác thực và phân quyền truy cập endpoint.
* + Khai báo @Bean AuthenticationManager vào Spring Application Context để các class khác có thể inject
* + Khai báo PasswordEncoder sử dụng thuật toán Bcrypt để mã hóa password
* */
@Configuration
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    public SecurityConfig(
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cung cấp AuthenticationManager để AuthServiceImpl sử dụng khi login
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) {
        return configuration.getAuthenticationManager();
    }

    /*
     * SecurityFilterChain là chuỗi các Filter do Spring Security quản lý,
     * chịu trách nhiệm xử lý và bảo vệ HTTP request trước khi request được chuyển đến Controller.
     * SecurityConfig sử dụng HttpSecurity để khai báo các quy tắc xác thực, phân quyền và đăng ký các Filter cần thiết.
     * Khi request đi qua SecurityFilterChain, các Filter sẽ thực hiện nhiệm vụ tương ứng như xác thực JWT, kiểm tra quyền truy cập
     * và xử lý các trường hợp chưa được xác thực hoặc không đủ quyền.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {

        http
                // Tắt CSRF cho API sử dụng cơ chế Bearer Token stateless
                .csrf(csrf -> csrf.disable())
                // Không sử dụng HTTP Session để lưu trạng thái đăng nhập
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        // Cho phép đăng ký và đăng nhập không cần JWT
                        .requestMatchers(
                                "/auth/register",
                                "/auth/login"
                        ).permitAll()
                        // Chỉ ROLE_ADMIN được truy cập API quản trị
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Chỉ CANDIDATE được truy cập API ứng viên
                        .requestMatchers("/candidate/**").hasAuthority("CANDIDATE")
                        // Chỉ RECRUITER được truy cập API nhà tuyển dụng
                        .requestMatchers("/recruiter/**").hasAuthority("RECRUITER")
                        // Các API còn lại yêu cầu xác thực
                        .anyRequest().authenticated()
                )
                // Đặt JwrAuthenticationFilter vào SecurityFilterChain và thực hiện trước UsernamePasswordAuthenticationFilter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                customAuthenticationEntryPoint
                        )
                        .accessDeniedHandler(
                                customAccessDeniedHandler
                        )
                );

        return http.build();
    }
}