package com.dacn.job_service.controller;

import com.dacn.job_service.dto.JobCategoryResponse;
import com.dacn.job_service.service.IJobCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class JobCategoryController {

    private final IJobCategoryService categoryService;

    public JobCategoryController(IJobCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /*
     * LẤY DANH SÁCH TẤT CẢ NGÀNH NGHỀ / CHUYÊN MÔN
     * URL: GET /categories
     */
    @GetMapping
    public ResponseEntity<List<JobCategoryResponse>> getAll() {
        List<JobCategoryResponse> responses = categoryService.getAll();
        return ResponseEntity.ok(responses);
    }
}
