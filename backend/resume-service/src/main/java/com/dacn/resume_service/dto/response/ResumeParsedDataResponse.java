package com.dacn.resume_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeParsedDataResponse {
    private UUID id;
    private UUID resumeId;
    private String candidateName;
    private String targetRole;
    private String phone;
    private String email;
    private String summary;
    private Integer totalExperienceMonths;
    private List<String> extractedSkills;
    private List<Map<String, Object>> educations;
    private List<Map<String, Object>> experiences;
    private List<Map<String, Object>> certifications;
    private Boolean updatedByUser;
    private LocalDateTime parsedAt;
    private LocalDateTime updatedAt;
}
