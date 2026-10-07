package com.dacn.job_service.model;

import com.dacn.job_service.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // 1. Khóa ngoại liên kết Company (Nhiều Job thuộc 1 Công ty)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // ID người đăng (Recruiter từ User Service)
    @Column(name = "recruiter_id", nullable = false)
    private UUID recruiterId;

    // 2. Khóa ngoại liên kết Danh mục ngành nghề
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private JobCategory category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    // 3. Các Enums: BẮT BUỘC dùng @Enumerated(EnumType.STRING)
    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false, length = 30)
    private JobType jobType = JobType.FULL_TIME;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_mode", nullable = false, length = 30)
    private WorkMode workMode = WorkMode.ON_SITE;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_level", nullable = false, length = 30)
    private JobLevel jobLevel = JobLevel.MIDDLE;

    @Column(name = "experience_years_min")
    private Integer experienceYearsMin = 0;

    @Column(name = "experience_years_max")
    private Integer experienceYearsMax;

    @Enumerated(EnumType.STRING)
    @Column(name = "salary_type", nullable = false, length = 20)
    private SalaryType salaryType = SalaryType.RANGE;

    // Dùng BigDecimal cho tiền tệ để tránh lỗi sai số dấu phẩy động
    @Column(name = "salary_min", precision = 15, scale = 2)
    private BigDecimal salaryMin;

    @Column(name = "salary_max", precision = 15, scale = 2)
    private BigDecimal salaryMax;

    @Column(length = 10)
    private String currency = "VND";

    @Column(name = "is_salary_negotiable")
    private Boolean isSalaryNegotiable = false;

    private Integer quantity = 1;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(name = "address_detail")
    private String addressDetail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private JobStatus status = JobStatus.DRAFT;

    @Column(name = "views_count")
    private Integer viewsCount = 0;

    @Column(name = "applications_count")
    private Integer applicationsCount = 0;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 4. Liên kết danh sách JobSkill (1 Job có nhiều JobSkill)
    @Builder.Default
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobSkill> jobSkills = new ArrayList<>();

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
