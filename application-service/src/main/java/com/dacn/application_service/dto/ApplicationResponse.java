package com.dacn.application_service.dto;

import com.dacn.application_service.enums.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@JsonPropertyOrder
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