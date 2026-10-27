package com.dacn.user_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "skills", uniqueConstraints = @UniqueConstraint(name = "uk_skill_normalized_name", columnNames = "normalized_name"))
@Getter @Setter
/** Entity đại diện cho một kỹ năng trong danh mục dùng chung. */
public class Skill {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(nullable = false, length = 100) private String name;
    @Column(name = "normalized_name", nullable = false, length = 100) private String normalizedName;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(name = "is_active", nullable = false) private boolean active = true;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @PrePersist void created() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate void updated() { updatedAt = LocalDateTime.now(); }
}
