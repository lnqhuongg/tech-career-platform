package com.dacn.user_service.dto.response;

import com.dacn.user_service.model.CandidateSkill;
import com.dacn.user_service.model.ProficiencyLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Thông tin một kỹ năng gắn với hồ sơ ứng viên.
 */
@Setter
@Getter
@NoArgsConstructor
public class CandidateSkillResponse {
    private UUID id;
    private SkillResponse skill;
    private ProficiencyLevel proficiencyLevel;
    private Integer yearsOfExperience;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CandidateSkillResponse(
            UUID id,
            SkillResponse skill,
            ProficiencyLevel proficiencyLevel,
            Integer yearsOfExperience,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.skill = skill;
        this.proficiencyLevel = proficiencyLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** Chuyển entity kỹ năng của ứng viên thành DTO phản hồi. */
    public static CandidateSkillResponse from(CandidateSkill candidateSkill) {
        return new CandidateSkillResponse(
                candidateSkill.getId(),
                SkillResponse.from(candidateSkill.getSkill()),
                candidateSkill.getProficiencyLevel(),
                candidateSkill.getYearsOfExperience(),
                candidateSkill.getCreatedAt(),
                candidateSkill.getUpdatedAt()
        );
    }

}
