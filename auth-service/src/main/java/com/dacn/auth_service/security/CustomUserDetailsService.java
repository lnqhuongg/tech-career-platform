package com.dacn.auth_service.security;

import com.dacn.auth_service.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 * Đây là class đứng giữa làm cầu nối cho database và Spring Security
 * Chịu trách nhiệm tìm kiếm và cung cấp thông tin account từ database cho Spring Security để thực hiện xác thực
 * Spring Security có cơ chế xác thực riêng, nhưng nó không tự biết cách tìm tài khoản trong db, cụ thể là bảng user_accounts
 * Vì vậy, CustomUserDetailsService cung cấp cách tìm tài khoản theo email.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public CustomUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    // Spring Security sẽ gọi hàm này và truyền email người dùng nhập vào đây
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String normalizedEmail = email.trim().toLowerCase(java.util.Locale.ROOT);
        return userAccountRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));
    }

}