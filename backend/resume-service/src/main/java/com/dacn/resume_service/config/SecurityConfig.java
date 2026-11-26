package com.dacn.resume_service.config;

import com.dacn.resume_service.dto.response.ApiResponse;
import com.dacn.resume_service.security.JwtAuthenticationFilter;
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
public class SecurityConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
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
                        // Tải file xuống / xem trực tiếp
                        .requestMatchers(HttpMethod.GET, "/resumes/*/download").permitAll()
                        .requestMatchers(HttpMethod.GET, "/resumes/*/view").permitAll()

                        // Các API cá nhân của ứng viên (me)
                        .requestMatchers("/resumes/me", "/resumes/me/**").hasAuthority("CANDIDATE")
                        .requestMatchers(HttpMethod.POST, "/resumes/upload").hasAuthority("CANDIDATE")
                        .requestMatchers(HttpMethod.PUT, "/resumes/*/parsed-data").hasAuthority("CANDIDATE")
                        .requestMatchers(HttpMethod.PATCH, "/resumes/*/default").hasAuthority("CANDIDATE")
                        .requestMatchers(HttpMethod.DELETE, "/resumes/*").hasAuthority("CANDIDATE")

                        // Đánh giá mức độ phù hợp và Skill Gap
                        .requestMatchers(HttpMethod.POST, "/resumes/*/evaluations/*").hasAuthority("CANDIDATE")
                        .requestMatchers(HttpMethod.GET, "/resumes/*/evaluations", "/resumes/*/evaluations/*").authenticated()

                        // Xem chi tiết hoặc trích xuất raw text
                        .requestMatchers(HttpMethod.GET, "/resumes/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/resumes/*/raw-text").authenticated()

                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
