package com.dacn.job_service.service;

import com.dacn.job_service.dto.JobCategoryResponse;
import java.util.List;

public interface IJobCategoryService {
    List<JobCategoryResponse> getAll();
}
