package com.dacn.application_service.controller;

import com.dacn.application_service.dto.ApplicationResponse;
import com.dacn.application_service.dto.CreateApplicationRequest;
import com.dacn.application_service.dto.CreateRejectedApplicationRequest;
import com.dacn.application_service.service.IApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final IApplicationService applicationService;

    public ApplicationController (IApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /*
     * ỨNG VIÊN NỘP HỒ SƠ
     */
    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            @RequestBody CreateApplicationRequest request) {
        ApplicationResponse response = applicationService.create(request);
        return ResponseEntity.ok(response);
    }

    /*
     * HỒ SƠ BỊ TỪ CHỐI BỞI EMPLOYER/RECRUITER
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<ApplicationResponse> reject(
            @PathVariable UUID id,
            @RequestBody CreateRejectedApplicationRequest request) {
        ApplicationResponse response = applicationService.reject(request, id);
        return ResponseEntity.ok(response);
    }

    /*
     * LẤY TẤT CẢ HỒ SƠ CỦA ỨNG VIÊN
     */
    @GetMapping
    public ResponseEntity<Page<ApplicationResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Page<ApplicationResponse> response = applicationService.getAll(page, size);
        return ResponseEntity.ok(response);
    }
}
