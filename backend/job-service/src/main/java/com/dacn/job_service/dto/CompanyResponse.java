package com.dacn.job_service.dto;

import com.dacn.job_service.enums.CompanyIndustry;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse {

    private UUID id;
    private String name;
    private String logoUrl;
    private String website;
    private String companySize;
    private CompanyIndustry industry;
    private String description;
    private String address;
    private String city;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
