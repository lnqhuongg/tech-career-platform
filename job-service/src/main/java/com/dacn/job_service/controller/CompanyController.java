package com.dacn.job_service.controller;

import com.dacn.job_service.dto.CompanyResponse;
import com.dacn.job_service.dto.CreateCompanyRequest;
import com.dacn.job_service.dto.UpdateCompanyRequest;
import com.dacn.job_service.service.ICompanyService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final ICompanyService companyService;

    public CompanyController(ICompanyService companyService) {
        this.companyService = companyService;
    }

    /*
     * 1. TẠO MỚI CÔNG TY
     * URL: POST /companies
     */
    @PostMapping
    public ResponseEntity<CompanyResponse> create(@RequestBody CreateCompanyRequest request) {
        CompanyResponse response = companyService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
     * 2. CẬP NHẬT THÔNG TIN CÔNG TY
     * URL: PUT /companies/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateCompanyRequest request
    ) {
        CompanyResponse response = companyService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /*
     * 3. LẤY CHI TIẾT CÔNG TY THEO ID
     * URL: GET /companies/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getById(@PathVariable UUID id) {
        CompanyResponse response = companyService.getById(id);
        return ResponseEntity.ok(response);
    }

    /*
     * 4. LẤY DANH SÁCH TẤT CẢ CÔNG TY (CÓ PHÂN TRANG)
     * URL: GET /companies?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<CompanyResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<CompanyResponse> response = companyService.getAll(page, size);
        return ResponseEntity.ok(response);
    }
}
