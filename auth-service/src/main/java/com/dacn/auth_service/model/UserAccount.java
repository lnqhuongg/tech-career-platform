package com.dacn.auth_service.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_accounts")
@Getter
@Setter
public class UserAccount implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private Set<Role> roles = new HashSet<>();

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Spring Security

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        // Quyền theo loại tài khoản
        authorities.add(
                new SimpleGrantedAuthority(accountType.name())
        );

        // Quyền bổ vai trò (User, Admin, Moderator, ...)
        roles.forEach(role ->
                authorities.add(
                        new SimpleGrantedAuthority(role.name())
                )
        );

        return authorities;
    }

    // Vì UserAccount implement UserDetails nên ở đây override phương thức getUsername() trong interface chứ không dùng getEmail nho
    @Override
    public String getUsername() {
        return email;
    }

    // Mấy phương thức ở dưới này cũng z, đều override mấy phương thức trong interface UserDetails
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}