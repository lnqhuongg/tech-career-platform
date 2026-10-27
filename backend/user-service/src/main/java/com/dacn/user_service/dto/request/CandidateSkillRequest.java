package com.dacn.user_service.dto.request;

import com.dacn.user_service.model.ProficiencyLevel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Dữ liệu đầu vào để thêm kỹ năng vào hồ sơ ứng viên.
 */
@Setter
@Getter
@NoArgsConstructor
public class CandidateSkillRequest {
    @NotNull
    private UUID skillId;
    @NotNull
    private ProficiencyLevel proficiencyLevel;
    @Min(0) @Max(80)
    private Integer yearsOfExperience;

    public CandidateSkillRequest(UUID skillId, ProficiencyLevel proficiencyLevel, Integer yearsOfExperience) {
        this.skillId = skillId;
        this.proficiencyLevel = proficiencyLevel;
        this.yearsOfExperience = yearsOfExperience;
    }
}
