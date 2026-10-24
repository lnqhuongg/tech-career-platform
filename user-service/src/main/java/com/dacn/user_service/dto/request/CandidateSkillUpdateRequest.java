package com.dacn.user_service.dto.request;

import com.dacn.user_service.model.ProficiencyLevel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dữ liệu cập nhật mức độ thành thạo và số năm kinh nghiệm của kỹ năng.
 */
@Setter
@Getter
@NoArgsConstructor
public class CandidateSkillUpdateRequest {
    @NotNull
    private ProficiencyLevel proficiencyLevel;
    @Min(0) @Max(80)
    private Integer yearsOfExperience;

    public CandidateSkillUpdateRequest(ProficiencyLevel proficiencyLevel, Integer yearsOfExperience) {
        this.proficiencyLevel = proficiencyLevel;
        this.yearsOfExperience = yearsOfExperience;
    }

}
