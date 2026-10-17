package com.dacn.job_service.service.impl;

import com.dacn.job_service.dto.JobCategoryResponse;
import com.dacn.job_service.model.JobCategory;
import com.dacn.job_service.repository.JobCategoryRepository;
import com.dacn.job_service.service.IJobCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobCategoryServiceImpl implements IJobCategoryService {

    private final JobCategoryRepository jobCategoryRepository;

    public JobCategoryServiceImpl(JobCategoryRepository jobCategoryRepository) {
        this.jobCategoryRepository = jobCategoryRepository;
    }

    @Override
    public List<JobCategoryResponse> getAll() {
        return jobCategoryRepository.findAll().stream()
                .map(this::toJobCategoryResponse)
                .collect(Collectors.toList());
    }

    private JobCategoryResponse toJobCategoryResponse(JobCategory category) {
        return JobCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
