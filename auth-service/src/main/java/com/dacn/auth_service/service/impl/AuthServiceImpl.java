package com.dacn.auth_service.service.impl;

import com.dacn.auth_service.dto.request.LoginRequest;
import com.dacn.auth_service.dto.request.RegisterRequest;
import com.dacn.auth_service.dto.response.AuthResponse;
import com.dacn.auth_service.model.Role;
import com.dacn.auth_service.model.UserAccount;
import com.dacn.auth_service.repository.UserAccountRepository;
import com.dacn.auth_service.security.JwtService;
import com.dacn.auth_service.service.IAuthService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthServiceImpl implements IAuthService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /*
    * Luồng cơ bản của đăng ký:
    * 1. Nhận RegisterRequest
    * 2. Kiểm tra email đã tồn tại chưa
    * 3. Chuẩn hóa email
    * 4. Mã hóa password bằng BCrypt
    * 5. Gán AccountType
    * 6. Gán role mặc định ROLE_USER (muốn đổi để test các tính năng khác thì zô db đổi nha)
    * 7. Lưu tài khoản bằng Repository
    * 8. Trả về AuthResponse.
    * */
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (userAccountRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }

        UserAccount user = new UserAccount();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAccountType(request.getAccountType());
        user.getRoles().add(Role.ROLE_USER);

        UserAccount saved = userAccountRepository.save(user);
        return new AuthResponse(saved.getId(), saved.getEmail(), saved.getAccountType(),
                "Đăng ký tài khoản thành công");
    }

    /*
     * Luồng cơ bản của đăng nhập:
     * 1. Nhận LoginRequest
     * 2. Xác thực tài khoản và mật khẩu
     * 3. Chuẩn hóa email
     * 4. Mã hóa password bằng BCrypt
     * 5. Gán AccountType
     * 6. Gán role mặc định ROLE_USER (muốn đổi để test các tính năng khác thì zô db đổi nha)
     * 7. Lưu tài khoản bằng Repository
     * 8. Trả về AuthResponse.
     * */
    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());

        // Yêu cầu AuthenticationManager xác thực tài khoản và mật khẩu
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                email,
                                request.getPassword()
                        )
                );

        // Lấy tài khoản đã được xác thực
        UserAccount user = userAccountRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Không tìm thấy tài khoản"
                        )
                );

        // Phát hành JWT
        String accessToken = jwtService.generateToken(user);

        // Trả về thông tin đăng nhập
        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getAccountType(),
                "Đăng nhập thành công",
                accessToken,
                "Bearer",
                jwtService.getExpirationSeconds()
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
