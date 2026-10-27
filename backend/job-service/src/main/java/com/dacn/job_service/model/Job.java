package com.dacn.job_service.model;

import com.dacn.job_service.enums.JobLevel;
import com.dacn.job_service.enums.JobStatus;
import com.dacn.job_service.enums.JobType;
import com.dacn.job_service.enums.WorkMode;
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

    // 3. Các Enums
    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false, length = 30)
    private JobType jobType = JobType.FULL_TIME;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_mode", nullable = false, length = 30)
    private WorkMode workMode = WorkMode.ON_SITE;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_level", nullable = false, length = 30)
    private JobLevel jobLevel = JobLevel.MIDDLE;

    // Yêu cầu kinh nghiệm (số năm tối thiểu)
    @Column(name = "experience_years")
    private Integer experienceYears = 0;

    // Lương (Dùng BigDecimal cho tiền tệ, NULL cả 2 = Thỏa thuận)
    @Column(name = "salary_min", precision = 15, scale = 2)
    private BigDecimal salaryMin;

    @Column(name = "salary_max", precision = 15, scale = 2)
    private BigDecimal salaryMax;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(name = "address_detail")
    private String addressDetail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private JobStatus status = JobStatus.PUBLISHED;

    @Column(name = "views_count")
    private Integer viewsCount = 0;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 4. Liên kết danh sách Kỹ năng (Quan hệ Many-to-Many trực tiếp với Skill)
    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "job_skills",
        joinColumns = @JoinColumn(name = "job_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

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
