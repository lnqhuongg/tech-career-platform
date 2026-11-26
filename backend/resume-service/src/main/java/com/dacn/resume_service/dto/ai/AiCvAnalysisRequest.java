package com.dacn.resume_service.dto.ai;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiCvAnalysisRequest {
    private String rawText;
    private String targetRole;
}
