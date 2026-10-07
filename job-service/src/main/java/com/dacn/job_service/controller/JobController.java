package com.dacn.job_service.controller;

import com.dacn.job_service.dto.JobDetailResponse;
import com.dacn.job_service.dto.JobFilterRequest;
import com.dacn.job_service.dto.JobResponse;
import com.dacn.job_service.service.IJobService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final IJobService jobService;

    public JobController(IJobService jobService) {
        this.jobService = jobService;
    }

    /*
     * 1. GET ALL & FILTER ĐA TIÊU CHÍ (CÓ PHÂN TRANG)
     * URL ví dụ: /jobs?page=0&size=10&keyword=Java&location=TP.HCM&jobLevel=SENIOR
     */
    @GetMapping
    public ResponseEntity<Page<JobResponse>> getJobs(
            @ModelAttribute JobFilterRequest filterRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<JobResponse> response = jobService.filter(filterRequest, page, size);
        return ResponseEntity.ok(response);
    }

    /*
     * 2. LẤY CHI TIẾT JOB THEO ID
     * URL ví dụ: /jobs/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobDetailResponse> getById(@PathVariable UUID id) {
        JobDetailResponse response = jobService.getById(id);
        return ResponseEntity.ok(response);
    }
}
