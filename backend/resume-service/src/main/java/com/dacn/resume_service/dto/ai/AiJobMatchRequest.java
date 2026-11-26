package com.dacn.resume_service.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiJobMatchRequest {
    @JsonProperty("resume_id")
    private String resumeId;

    @JsonProperty("resume_text")
    private String resumeText;

    @JsonProperty("candidate_skills")
    private List<String> candidateSkills;

    @JsonProperty("job_id")
    private String jobId;

    @JsonProperty("job_title")
    private String jobTitle;

    @JsonProperty("job_description")
    private String jobDescription;

    @JsonProperty("required_skills")
    private List<String> requiredSkills;
}
