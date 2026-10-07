package com.dacn.job_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "job_skills", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"job_id", "skill_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class JobSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // Nhiều JobSkill thuộc về 1 Job
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    // Nhiều JobSkill thuộc về 1 Skill
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
    @Column(name = "is_required", nullable = false)
    private Boolean isRequired = true;
    @Column(name = "min_years_experience")
    private Integer minYearsExperience = 0;
}
