package com.dacn.job_service.service;

import com.dacn.job_service.dto.*;
import org.springframework.data.domain.Page;
import java.util.UUID;

public interface IJobService {
    Page<JobResponse> getAll(int page, int size);
    Page<JobResponse> filter(JobFilterRequest request, int page, int size);
    JobDetailResponse getById(UUID id);
    JobDetailResponse create(CreateJobRequest request);
    JobDetailResponse update(UUID id, UpdateJobRequest request);
    JobDetailResponse closeJob(UUID id);
    void deleteJob(UUID id);
    Page<JobResponse> getJobsByCompany(UUID companyId, int page, int size);
    void saveJob(UUID jobId, UUID seekerId);
    void unsaveJob(UUID jobId, UUID seekerId);
    Page<JobResponse> getSavedJobs(UUID seekerId, int page, int size);
}
