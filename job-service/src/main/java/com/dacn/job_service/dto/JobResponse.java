package com.dacn.job_service.dto;

import com.dacn.job_service.enums.JobLevel;
import com.dacn.job_service.enums.JobStatus;
import com.dacn.job_service.enums.JobType;
import com.dacn.job_service.enums.WorkMode;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobResponse {
    private UUID id;
    private String title;
    private CompanySummaryResponse company;
    private String categoryName;
    private JobType jobType;
    private WorkMode workMode;
    private JobLevel jobLevel;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String location;
    private JobStatus status;
    private List<SkillResponse> skills;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
