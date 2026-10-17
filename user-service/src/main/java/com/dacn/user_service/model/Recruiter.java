package com.dacn.user_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "recruiters", uniqueConstraints = @UniqueConstraint(name = "uk_recruiter_account", columnNames = "account_id"))
@Getter @Setter
/** Entity lưu thông tin hồ sơ nhà tuyển dụng. */
public class Recruiter {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(name = "account_id", nullable = false, updatable = false) private UUID accountId;
    @Column(nullable = false, length = 150) private String fullName;
    @Column(name = "company_id", nullable = false) private UUID companyId;
    @Column(length = 30) private String phone;
    @Enumerated(EnumType.STRING) @Column(name = "verification_status", nullable = false) private VerificationStatus verificationStatus = VerificationStatus.PENDING;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @PrePersist void created() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate void updated() { updatedAt = LocalDateTime.now(); }
}
