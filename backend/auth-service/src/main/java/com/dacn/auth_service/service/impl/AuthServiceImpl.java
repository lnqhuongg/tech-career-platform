package com.dacn.auth_service.service.impl;

import com.dacn.auth_service.dto.request.*;
import com.dacn.auth_service.dto.response.AccountResponse;
import com.dacn.auth_service.dto.response.AuthResponse;
import com.dacn.auth_service.exception.EmailAlreadyExistsException;
import com.dacn.auth_service.model.RefreshToken;
import com.dacn.auth_service.model.UserAccount;
import com.dacn.auth_service.repository.UserAccountRepository;
import com.dacn.auth_service.security.JwtService;
import com.dacn.auth_service.service.IAuthService;

import com.dacn.auth_service.service.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
public class AuthServiceImpl implements IAuthService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthServiceImpl(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    /*
    * Luồng cơ bản của đăng ký:
    * 1. Nhận RegisterRequest
    * 2. Kiểm tra email đã tồn tại chưa
    * 3. Chuẩn hóa email
    * 4. Mã hóa password bằng BCrypt
    * 5. Gán AccountType
    * 7. Lưu tài khoản bằng Repository
    * 8. Trả về AuthResponse.
    * */
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (request.getAccountType() == null
                || request.getAccountType() == com.dacn.auth_service.model.AccountType.ADMIN) {
            throw new IllegalArgumentException(
                    "Không thể đăng ký tài khoản ADMIN thông qua API công khai"
            );
        }

        String email = normalizeEmail(request.getEmail());
        if (userAccountRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email đã được sử dụng");
        }

        UserAccount user = new UserAccount();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAccountType(request.getAccountType());

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
      * 7. Lưu tài khoản bằng Repository
     * 8. Trả về AuthResponse.
     * */
    @Override
    @Transactional
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
        String refreshToken = refreshTokenService.createRefreshToken(user);

        // Trả về thông tin đăng nhập
        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getAccountType(),
                "Đăng nhập thành công",
                accessToken,
                refreshToken,
                "Bearer",
                jwtService.getExpirationSeconds()
        );
    }


    /**
     * Xoay vòng Refresh Token trong một transaction.
     * Token cũ bị thu hồi khi token mới được tạo thành công.
     */
    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {

        // Xác minh token, kiểm tra trạng thái và khóa bản ghi.
        RefreshToken storedToken =
                refreshTokenService.validateRefreshTokenForUpdate(
                        request.getRefreshToken()
                );

        UserAccount user = storedToken.getUser();

        // Thu hồi token cũ trong cùng transaction.
        refreshTokenService.revoke(storedToken);

        // Phát hành Access Token mới dựa trên thông tin tài khoản hiện tại.
        String newAccessToken = jwtService.generateToken(user);

        // Tạo Refresh Token mới và lưu hash trong database.
        String newRefreshToken =
                refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getAccountType(),
                "Làm mới token thành công",
                newAccessToken,
                newRefreshToken,
                "Bearer",
                jwtService.getExpirationSeconds()
        );
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequest request) {
        refreshTokenService.revokeToken(request.getRefreshToken());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getMyAccount(String email) {
        UserAccount user = findUserByEmail(email);
        return new AccountResponse(user);
    }

    /**
     * Đổi mật khẩu sau khi xác minh mật khẩu hiện tại.
     * Các Refresh Token hiện có bị thu hồi để kết thúc các phiên khác.
     */
    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        UserAccount user = findUserByEmail(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Mật khẩu hiện tại không chính xác");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userAccountRepository.save(user);

        // Thu hồi toàn bộ refresh token cũ khi đổi mật khẩu để bảo mật
        refreshTokenService.revokeAllUserTokens(user);
    }

    /**
     * Cập nhật trạng thái tài khoản.
     * Khi tài khoản bị vô hiệu hóa, tất cả Refresh Token sẽ bị thu hồi.
     */
    @Override
    @Transactional
    public AccountResponse updateAccountStatus(UUID userId, boolean isActive) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản với ID: " + userId));

        user.setActive(isActive);
        UserAccount updated = userAccountRepository.save(user);

        if (!isActive) {
            refreshTokenService.revokeAllUserTokens(user);
        }
        return new AccountResponse(updated);
    }

    private UserAccount findUserByEmail(String email) {
        return userAccountRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
