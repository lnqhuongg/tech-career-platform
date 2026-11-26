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
public class AiChatRequest {
    private String message;
    private List<Map<String, String>> history;

    @JsonProperty("resume_context")
    private String resumeContext;

    @JsonProperty("job_context")
    private String jobContext;
}
