package com.dacn.user_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "candidates", uniqueConstraints = @UniqueConstraint(name = "uk_candidate_account", columnNames = "account_id"))
@Getter @Setter
/** Entity lưu thông tin hồ sơ ứng viên. */
public class Candidate {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(name = "account_id", nullable = false, updatable = false) private UUID accountId;
    @Column(nullable = false, length = 150) private String fullName;
    @Column(columnDefinition = "TEXT") private String summary;
    @Column(length = 30) private String phone;
    @Column(length = 120) private String location;
    @Column(name = "years_of_experience") private Integer yearsOfExperience;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @PrePersist void created() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate void updated() { updatedAt = LocalDateTime.now(); }
}
