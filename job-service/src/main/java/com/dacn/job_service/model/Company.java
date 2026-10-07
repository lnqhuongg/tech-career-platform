package com.dacn.job_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Tham chiếu sang Recruiter trong User Service
    @Column(name = "recruiter_id", nullable = false)
    private UUID recruiterId;

    @Column(nullable = false)
    private String name;

    @Column(name = "logo_url")
    private String logoUrl;
    @Column(name = "banner_url")
    private String bannerUrl;
    private String website;
    @Column(name = "company_size")
    private String companySize;
    private String industry;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String address;
    private String city;
    private String country;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Tự động gán thời gian khi tạo mới hoặc cập nhật
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}