package com.dacn.application_service.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateRejectedApplicationRequest {
    private UUID recruiterId;
    private String note;
}
