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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJobRequest {

    private UUID companyId;
    private UUID categoryId;

    private String title;
    private String description;
    private String requirements;
    private String benefits;

    private JobType jobType;
    private WorkMode workMode;
    private JobLevel jobLevel;

    private Integer experienceYears;

    private BigDecimal salaryMin;
    private BigDecimal salaryMax;

    private String location;
    private String addressDetail;

    private JobStatus status;
    private LocalDateTime expiresAt;

    private List<UUID> skillIds;
}
