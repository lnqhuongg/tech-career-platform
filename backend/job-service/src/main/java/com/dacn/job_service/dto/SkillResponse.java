package com.dacn.job_service.dto;

import com.dacn.job_service.enums.SkillCategory;
import lombok.*;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SkillResponse {
    private UUID id;
    private String name;
    private SkillCategory category;
}
