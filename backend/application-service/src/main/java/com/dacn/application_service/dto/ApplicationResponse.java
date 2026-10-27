package com.dacn.application_service.dto;

import com.dacn.application_service.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ApplicationResponse {
    private UUID id;
    private UUID jobId;
    private UUID jobSeekerId;
    private UUID resumeId;
    private String coverLetter;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}