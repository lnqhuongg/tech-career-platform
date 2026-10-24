package com.dacn.job_service.service;

import com.dacn.job_service.dto.CompanyResponse;
import com.dacn.job_service.dto.CreateCompanyRequest;
import com.dacn.job_service.dto.UpdateCompanyRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ICompanyService {

    CompanyResponse create(CreateCompanyRequest request);

    CompanyResponse update(UUID id, UpdateCompanyRequest request);

    CompanyResponse getById(UUID id);

    Page<CompanyResponse> getAll(int page, int size);
}
