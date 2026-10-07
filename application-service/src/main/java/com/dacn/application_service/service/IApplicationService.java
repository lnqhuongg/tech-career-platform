package com.dacn.application_service.service;

import com.dacn.application_service.dto.ApplicationResponse;
import com.dacn.application_service.dto.CreateApplicationRequest;
import com.dacn.application_service.dto.CreateRejectedApplicationRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IApplicationService {
    ApplicationResponse create (CreateApplicationRequest request);
    ApplicationResponse reject (CreateRejectedApplicationRequest request, UUID id);
    Page<ApplicationResponse> getAll(int page, int size);
}
