package com.dacn.job_service.dto;

import com.dacn.job_service.enums.CompanyIndustry;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCompanyRequest {

    private String name;
    private String logoUrl;
    private String website;
    private String companySize;
    private CompanyIndustry industry;
    private String description;
    private String address;
    private String city;
}
