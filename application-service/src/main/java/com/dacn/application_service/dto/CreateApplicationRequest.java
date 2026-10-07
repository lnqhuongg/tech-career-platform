package com.dacn.application_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateApplicationRequest {
    private UUID jobId;
    private UUID jobSeekerId;
    private UUID resumeId;
    private String coverLetter;
}
