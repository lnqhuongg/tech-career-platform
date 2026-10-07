package com.dacn.job_service.service.impl;

import com.dacn.job_service.common.AppException;
import com.dacn.job_service.dto.*;
import com.dacn.job_service.enums.JobStatus;
import com.dacn.job_service.model.Job;
import com.dacn.job_service.repository.JobRepository;
import com.dacn.job_service.repository.specification.JobSpecification;
import com.dacn.job_service.service.IJobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements IJobService {

    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /* 1. LẤY TẤT CẢ JOB ĐANG MỞ (CÓ PHÂN TRANG) */
    @Override
    public Page<JobResponse> getAll(int page, int size) {
        JobFilterRequest defaultFilter = new JobFilterRequest();
        defaultFilter.setStatus(JobStatus.PUBLISHED);
        return filter(defaultFilter, page, size);
    }

    /* 2. LỌC JOB ĐA TIÊU CHÍ */
    @Override
    public Page<JobResponse> filter(JobFilterRequest request, int page, int size) {
        // Sắp xếp bài mới đăng lên đầu tiên
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Specification<Job> spec = JobSpecification.filterJobs(request);

        Page<Job> jobPage = jobRepository.findAll(spec, pageable);
        return jobPage.map(this::toJobResponse);
    }

    /* 3. LẤY CHI TIẾT JOB THEO ID VÀ TĂNG LƯỢT XEM */
    @Override
    @Transactional
    public JobDetailResponse getById(UUID id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Không tìm thấy công việc với ID: " + id));

        // Tự động tăng lượt xem khi có người vào đọc chi tiết JD
        job.setViewsCount(job.getViewsCount() + 1);
        jobRepository.save(job);

        return toJobDetailResponse(job);
    }

    // --- CÁC HÀM MAPPER CHUYỂN ENTITY THÀNH DTO ---

    private JobResponse toJobResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .company(toCompanySummary(job))
                .categoryName(job.getCategory() != null ? job.getCategory().getName() : null)
                .jobType(job.getJobType())
                .workMode(job.getWorkMode())
                .jobLevel(job.getJobLevel())
                .salaryType(job.getSalaryType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .currency(job.getCurrency())
                .location(job.getLocation())
                .status(job.getStatus())
                .skills(toSkillResponses(job))
                .publishedAt(job.getPublishedAt())
                .expiresAt(job.getExpiresAt())
                .build();
    }

    private JobDetailResponse toJobDetailResponse(Job job) {
        return JobDetailResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .benefits(job.getBenefits())
                .company(toCompanySummary(job))
                .categoryName(job.getCategory() != null ? job.getCategory().getName() : null)
                .jobType(job.getJobType())
                .workMode(job.getWorkMode())
                .jobLevel(job.getJobLevel())
                .experienceYearsMin(job.getExperienceYearsMin())
                .experienceYearsMax(job.getExperienceYearsMax())
                .salaryType(job.getSalaryType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .currency(job.getCurrency())
                .isSalaryNegotiable(job.getIsSalaryNegotiable())
                .quantity(job.getQuantity())
                .location(job.getLocation())
                .addressDetail(job.getAddressDetail())
                .status(job.getStatus())
                .viewsCount(job.getViewsCount())
                .applicationsCount(job.getApplicationsCount())
                .skills(toSkillResponses(job))
                .publishedAt(job.getPublishedAt())
                .expiresAt(job.getExpiresAt())
                .createdAt(job.getCreatedAt())
                .build();
    }

    private CompanySummaryResponse toCompanySummary(Job job) {
        if (job.getCompany() == null) return null;
        return CompanySummaryResponse.builder()
                .id(job.getCompany().getId())
                .name(job.getCompany().getName())
                .logoUrl(job.getCompany().getLogoUrl())
                .city(job.getCompany().getCity())
                .build();
    }

    private List<SkillResponse> toSkillResponses(Job job) {
        if (job.getJobSkills() == null) return Collections.emptyList();
        return job.getJobSkills().stream()
                .map(js -> SkillResponse.builder()
                        .id(js.getSkill().getId())
                        .name(js.getSkill().getName())
                        .isRequired(js.getIsRequired())
                        .minYearsExperience(js.getMinYearsExperience())
                        .build())
                .collect(Collectors.toList());
    }
}
