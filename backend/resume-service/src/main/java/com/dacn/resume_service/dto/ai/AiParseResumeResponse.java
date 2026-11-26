package com.dacn.resume_service.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiParseResumeResponse {
    @JsonProperty("candidate_name")
    private String candidateName;

    @JsonProperty("target_role")
    private String targetRole;

    private String phone;
    private String email;
    private String summary;

    @JsonProperty("total_experience_months")
    private Integer totalExperienceMonths;

    @JsonProperty("extracted_skills")
    private List<String> extractedSkills;

    private List<Map<String, Object>> educations;
    private List<Map<String, Object>> experiences;
    private List<Map<String, Object>> certifications;
}
