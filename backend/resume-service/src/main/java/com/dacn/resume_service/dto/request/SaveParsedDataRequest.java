package com.dacn.resume_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO nhận dữ liệu bóc tách được xác nhận hoặc chỉnh sửa bởi ứng viên (Human-in-the-loop).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveParsedDataRequest {

    private String candidateName;
    private String targetRole;
    private String phone;
    private String email;
    private String summary;
    private Integer totalExperienceMonths;

    private List<String> extractedSkills;
    private List<Map<String, Object>> educations;
    private List<Map<String, Object>> experiences;
    private List<Map<String, Object>> certifications;
}
