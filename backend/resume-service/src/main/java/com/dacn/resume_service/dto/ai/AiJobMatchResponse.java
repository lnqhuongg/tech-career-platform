package com.dacn.resume_service.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiJobMatchResponse {
    @JsonProperty("match_score")
    private BigDecimal matchScore;

    @JsonProperty("semantic_score")
    private BigDecimal semanticScore;

    @JsonProperty("skill_score")
    private BigDecimal skillScore;

    @JsonProperty("matched_skills")
    private List<String> matchedSkills;

    @JsonProperty("missing_skills")
    private List<String> missingSkills;

    @JsonProperty("ai_suggestions")
    private String aiSuggestions;
}
