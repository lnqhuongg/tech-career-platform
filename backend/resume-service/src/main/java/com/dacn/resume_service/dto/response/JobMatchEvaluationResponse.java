package com.dacn.resume_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobMatchEvaluationResponse {

    private UUID id;
    private UUID resumeId;
    private UUID jobId;
    private BigDecimal matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String aiSuggestions;
    private LocalDateTime evaluatedAt;
    private LocalDateTime updatedAt;
}
