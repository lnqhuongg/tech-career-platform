package com.dacn.user_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "candidate_skills", uniqueConstraints = @UniqueConstraint(name = "uk_candidate_skill", columnNames = {"candidate_id", "skill_id"}))
@Getter @Setter
/** Entity liên kết một ứng viên với một kỹ năng và thông tin kinh nghiệm. */
public class CandidateSkill {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "candidate_id") private Candidate candidate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "skill_id") private Skill skill;
    @Enumerated(EnumType.STRING) @Column(name = "proficiency_level", nullable = false) private ProficiencyLevel proficiencyLevel;
    @Column(name = "years_of_experience") private Integer yearsOfExperience;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @PrePersist void created() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate void updated() { updatedAt = LocalDateTime.now(); }
}
