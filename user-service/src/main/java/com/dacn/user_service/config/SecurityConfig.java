package com.dacn.user_service.config;

import com.dacn.user_service.security.JwtAuthenticationFilter;
import com.dacn.user_service.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
/** Cấu hình xác thực JWT và phân quyền truy cập các API. */
public class SecurityConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            ObjectMapper objectMapper
    ) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) -> {
                            response.setStatus(401);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            objectMapper.writeValue(response.getOutputStream(),
                                    new ApiResponse<>(false, "Bạn cần đăng nhập hoặc cung cấp access token hợp lệ", null));
                        })
                        .accessDeniedHandler((request, response, exception) -> {
                            response.setStatus(403);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            objectMapper.writeValue(response.getOutputStream(),
                                    new ApiResponse<>(false, "Bạn không có quyền thực hiện thao tác này", null));
                        }))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")

                        // Kiểm tra các đường dẫn tự phục vụ trước các đường dẫn dùng mã định danh chung.
                        .requestMatchers("/candidate/me", "/candidate/me/**").hasAuthority("CANDIDATE")
                        .requestMatchers(HttpMethod.POST, "/candidate").hasAuthority("CANDIDATE")
                        .requestMatchers(HttpMethod.PUT, "/candidate/me").hasAuthority("CANDIDATE")

                        // API đọc hồ sơ công khai vẫn yêu cầu tài khoản đã xác thực hợp lệ.
                        .requestMatchers(HttpMethod.GET, "/candidate/*", "/candidate/*/skills").authenticated()
                        .requestMatchers("/candidate/**").hasAuthority("CANDIDATE")

                        .requestMatchers("/recruiter/me", "/recruiter/me/**").hasAuthority("RECRUITER")
                        .requestMatchers(HttpMethod.POST, "/recruiter").hasAuthority("RECRUITER")
                        .requestMatchers(HttpMethod.GET, "/recruiter/*").authenticated()
                        .requestMatchers("/recruiter/**").hasAuthority("RECRUITER")

                        .requestMatchers("/skills/**", "/users/**").authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
