package com.dacn.resume_service.dto.ai;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiCvAnalysisResponse {
    private Integer overallScore;
    private Integer formatScore;
    private Integer contentScore;
    private List<String> strengths;
    private List<String> improvements;
    private String actionableAdvice;
}
