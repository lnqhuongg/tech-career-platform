package com.dacn.job_service.controller;

import com.dacn.job_service.dto.*;
import com.dacn.job_service.service.IJobService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    /*
     * 3. TẠO MỚI TIN TUYỂN DỤNG
     * URL: POST /jobs
     */
    @PostMapping
    public ResponseEntity<JobDetailResponse> create(@RequestBody CreateJobRequest request) {
        JobDetailResponse response = jobService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
     * 4. CHỈNH SỬA TIN TUYỂN DỤNG
     * URL: PUT /jobs/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobDetailResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateJobRequest request
    ) {
        JobDetailResponse response = jobService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /*
     * 5. ĐÓNG TIN TUYỂN DỤNG
     * URL: PATCH /jobs/{id}/close
     */
    @PatchMapping("/{id}/close")
    public ResponseEntity<JobDetailResponse> closeJob(@PathVariable UUID id) {
        JobDetailResponse response = jobService.closeJob(id);
        return ResponseEntity.ok(response);
    }

    /*
     * 6. XÓA TIN TUYỂN DỤNG
     * URL: DELETE /jobs/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable UUID id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    /*
     * 7. LẤY DANH SÁCH BÀI ĐĂNG CỦA CÔNG TY
     * URL: GET /jobs/company/{companyId}
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<JobResponse>> getJobsByCompany(
            @PathVariable UUID companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<JobResponse> response = jobService.getJobsByCompany(companyId, page, size);
        return ResponseEntity.ok(response);
    }

    /*
     * 8. LƯU VIỆC LÀM (BOOKMARK)
     * URL: POST /jobs/{id}/save?seekerId=... hoặc Header X-User-Id
     */
    @PostMapping("/{id}/save")
    public ResponseEntity<String> saveJob(
            @PathVariable UUID id,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerSeekerId,
            @RequestParam(required = false) UUID seekerId
    ) {
        UUID finalSeekerId = seekerId != null ? seekerId : headerSeekerId;
        jobService.saveJob(id, finalSeekerId);
        return ResponseEntity.ok("Lưu việc làm thành công");
    }

    /*
     * 9. BỎ LƯU VIỆC LÀM
     * URL: DELETE /jobs/{id}/save?seekerId=... hoặc Header X-User-Id
     */
    @DeleteMapping("/{id}/save")
    public ResponseEntity<String> unsaveJob(
            @PathVariable UUID id,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerSeekerId,
            @RequestParam(required = false) UUID seekerId
    ) {
        UUID finalSeekerId = seekerId != null ? seekerId : headerSeekerId;
        jobService.unsaveJob(id, finalSeekerId);
        return ResponseEntity.ok("Bỏ lưu việc làm thành công");
    }

    /*
     * 10. XEM DANH SÁCH CÁC VIỆC ĐÃ LƯU
     * URL: GET /jobs/saved?seekerId=... hoặc Header X-User-Id
     */
    @GetMapping("/saved")
    public ResponseEntity<Page<JobResponse>> getSavedJobs(
            @RequestHeader(value = "X-User-Id", required = false) UUID headerSeekerId,
            @RequestParam(required = false) UUID seekerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID finalSeekerId = seekerId != null ? seekerId : headerSeekerId;
        Page<JobResponse> response = jobService.getSavedJobs(finalSeekerId, page, size);
        return ResponseEntity.ok(response);
    }
}
