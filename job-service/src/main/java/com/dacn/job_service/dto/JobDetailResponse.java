package com.dacn.job_service.dto;

import com.dacn.job_service.enums.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobDetailResponse {
    private UUID id;
    private String title;
    private String description;
    private String requirements;
    private String benefits;
    private CompanySummaryResponse company;
    private String categoryName;
    private JobType jobType;
    private WorkMode workMode;
    private JobLevel jobLevel;
    private Integer experienceYearsMin;
    private Integer experienceYearsMax;
    private SalaryType salaryType;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String currency;
    private Boolean isSalaryNegotiable;
    private Integer quantity;
    private String location;
    private String addressDetail;
    private JobStatus status;
    private Integer viewsCount;
    private Integer applicationsCount;
    private List<SkillResponse> skills;
    private LocalDateTime publishedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
