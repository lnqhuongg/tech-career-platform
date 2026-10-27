package com.dacn.user_service.dto.response;

import com.dacn.user_service.model.Skill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Thông tin kỹ năng trong danh mục chung.
 */
@Setter
@Getter
@NoArgsConstructor
public class SkillResponse {
    private UUID id;
    private String name;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SkillResponse(
            UUID id,
            String name,
            String description,
            boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** Chuyển entity kỹ năng thành DTO phản hồi. */
    public static SkillResponse from(Skill skill) {
        return new SkillResponse(
                skill.getId(),
                skill.getName(),
                skill.getDescription(),
                skill.isActive(),
                skill.getCreatedAt(),
                skill.getUpdatedAt()
        );
    }

}
