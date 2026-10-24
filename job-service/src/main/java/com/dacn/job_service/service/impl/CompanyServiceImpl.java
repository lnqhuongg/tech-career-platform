package com.dacn.job_service.service.impl;

import com.dacn.job_service.common.AppException;
import com.dacn.job_service.dto.CompanyResponse;
import com.dacn.job_service.dto.CreateCompanyRequest;
import com.dacn.job_service.dto.UpdateCompanyRequest;
import com.dacn.job_service.model.Company;
import com.dacn.job_service.repository.CompanyRepository;
import com.dacn.job_service.service.ICompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /* 1. TẠO MỚI CÔNG TY */
    @Override
    @Transactional
    public CompanyResponse create(CreateCompanyRequest request) {
        // Validation dữ liệu
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new AppException(400, "Tên công ty không được để trống");
        }

        String trimmedName = request.getName().trim();
        if (companyRepository.existsByName(trimmedName)) {
            throw new AppException(409, "Tên công ty đã tồn tại trong hệ thống");
        }

        Company company = Company.builder()
                .name(trimmedName)
                .logoUrl(request.getLogoUrl())
                .website(request.getWebsite())
                .companySize(request.getCompanySize() != null && !request.getCompanySize().trim().isEmpty()
                        ? request.getCompanySize().trim() : "50-150")
                .industry(request.getIndustry())
                .description(request.getDescription())
                .address(request.getAddress())
                .city(request.getCity())
                .build();

        Company savedCompany = companyRepository.save(company);
        return toCompanyResponse(savedCompany);
    }

    /* 2. CẬP NHẬT THÔNG TIN CÔNG TY */
    @Override
    @Transactional
    public CompanyResponse update(UUID id, UpdateCompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Không tìm thấy công ty với ID: " + id));

        if (request.getName() != null) {
            String trimmedName = request.getName().trim();
            if (trimmedName.isEmpty()) {
                throw new AppException(400, "Tên công ty không được để trống");
            }
            if (companyRepository.existsByNameAndIdNot(trimmedName, id)) {
                throw new AppException(409, "Tên công ty đã được sử dụng bởi công ty khác");
            }
            company.setName(trimmedName);
        }

        if (request.getLogoUrl() != null) {
            company.setLogoUrl(request.getLogoUrl());
        }
        if (request.getWebsite() != null) {
            company.setWebsite(request.getWebsite());
        }
        if (request.getCompanySize() != null) {
            company.setCompanySize(request.getCompanySize());
        }
        if (request.getIndustry() != null) {
            company.setIndustry(request.getIndustry());
        }
        if (request.getDescription() != null) {
            company.setDescription(request.getDescription());
        }
        if (request.getAddress() != null) {
            company.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            company.setCity(request.getCity());
        }

        Company updatedCompany = companyRepository.save(company);
        return toCompanyResponse(updatedCompany);
    }

    /* 3. LẤY CHI TIẾT CÔNG TY THEO ID */
    @Override
    public CompanyResponse getById(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Không tìm thấy công ty với ID: " + id));
        return toCompanyResponse(company);
    }

    /* 4. LẤY DANH SÁCH CÔNG TY (PHÂN TRANG) */
    @Override
    public Page<CompanyResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return companyRepository.findAll(pageable).map(this::toCompanyResponse);
    }

    private CompanyResponse toCompanyResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .logoUrl(company.getLogoUrl())
                .website(company.getWebsite())
                .companySize(company.getCompanySize())
                .industry(company.getIndustry())
                .description(company.getDescription())
                .address(company.getAddress())
                .city(company.getCity())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }
}
