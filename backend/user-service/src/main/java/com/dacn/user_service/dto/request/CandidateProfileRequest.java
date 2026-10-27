package com.dacn.user_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dữ liệu đầu vào để tạo hoặc cập nhật hồ sơ ứng viên.
 */
@Setter
@Getter
@NoArgsConstructor
public class CandidateProfileRequest {
    @NotBlank @Size(max = 150)
    private String fullName;
    @Size(max = 5000)
    private String summary;
    @Size(max = 30)
    private String phone;
    @Size(max = 120)
    private String location;
    @Min(0) @Max(80)
    private Integer yearsOfExperience;

    public CandidateProfileRequest(
            String fullName,
            String summary,
            String phone,
            String location,
            Integer yearsOfExperience
    ) {
        this.fullName = fullName;
        this.summary = summary;
        this.phone = phone;
        this.location = location;
        this.yearsOfExperience = yearsOfExperience;
    }

}
