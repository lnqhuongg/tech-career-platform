package com.dacn.application_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationRequest {
    private UUID jobId;
    private UUID jobSeekerId;
    private UUID resumeId;
    private String coverLetter;
}
