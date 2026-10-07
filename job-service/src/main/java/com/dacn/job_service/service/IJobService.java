package com.dacn.job_service.service;

import com.dacn.job_service.dto.JobDetailResponse;
import com.dacn.job_service.dto.JobFilterRequest;
import com.dacn.job_service.dto.JobResponse;
import org.springframework.data.domain.Page;
import java.util.UUID;

public interface IJobService {
    Page<JobResponse> getAll(int page, int size);
    Page<JobResponse> filter(JobFilterRequest request, int page, int size);
    JobDetailResponse getById(UUID id);
}
