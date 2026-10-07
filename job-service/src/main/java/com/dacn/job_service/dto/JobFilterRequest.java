package com.dacn.job_service.dto;

import com.dacn.job_service.enums.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobFilterRequest {
    private String keyword;
    private String location;
    private JobLevel jobLevel;
    private WorkMode workMode;
    private JobType jobType;
    private BigDecimal minSalary;
    private UUID categoryId;
    private JobStatus status;
}
