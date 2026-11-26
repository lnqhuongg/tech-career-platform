package com.dacn.resume_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Thực thể CV trung tâm: dùng để nộp hồ sơ xin việc, hiển thị danh sách CV,
 * đặt CV mặc định, xóa mềm và đối sánh kỹ năng với công việc.
 */
@Entity
@Table(name = "resume_parsed_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeParsedData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String title;

    @Column(name = "resume_id")
    private UUID resumeId;

    @Column(name = "candidate_name")
    private String candidateName;

    @Column(name = "target_role")
    private String targetRole;

    private String phone;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "total_experience_months")
    private Integer totalExperienceMonths;

    @Column(name = "parsing_status", nullable = false)
    private String parsingStatus; // 'DRAFT', 'COMPLETED', 'FAILED'

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "extracted_skills", columnDefinition = "jsonb")
    private List<String> extractedSkills;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "educations", columnDefinition = "jsonb")
    private List<Map<String, Object>> educations;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "experiences", columnDefinition = "jsonb")
    private List<Map<String, Object>> experiences;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "certifications", columnDefinition = "jsonb")
    private List<Map<String, Object>> certifications;

    @Column(name = "updated_by_user", nullable = false)
    private Boolean updatedByUser;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.title == null || this.title.isBlank()) {
            this.title = "Hồ sơ nghề nghiệp";
        }
        if (this.parsingStatus == null) {
            this.parsingStatus = "DRAFT";
        }
        if (this.isDefault == null) {
            this.isDefault = false;
        }
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
        if (this.updatedByUser == null) {
            this.updatedByUser = false;
        }
        if (this.totalExperienceMonths == null) {
            this.totalExperienceMonths = 0;
        }
    }
}
