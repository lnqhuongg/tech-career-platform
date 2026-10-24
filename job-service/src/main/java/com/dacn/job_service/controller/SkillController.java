package com.dacn.job_service.controller;

import com.dacn.job_service.dto.SkillResponse;
import com.dacn.job_service.enums.SkillCategory;
import com.dacn.job_service.service.ISkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillController {

    private final ISkillService skillService;

    public SkillController(ISkillService skillService) {
        this.skillService = skillService;
    }

    /*
     * LẤY DANH SÁCH TẤT CẢ KỸ NĂNG (HỖ TRỢ LỌC THEO CATEGORY NẾU CẦN)
     * URL: GET /skills hoặc GET /skills?category=BACKEND
     */
    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAll(
            @RequestParam(required = false) SkillCategory category
    ) {
        List<SkillResponse> responses = skillService.getAll(category);
        return ResponseEntity.ok(responses);
    }
}
