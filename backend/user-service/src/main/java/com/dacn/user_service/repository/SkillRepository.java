package com.dacn.user_service.repository;
import com.dacn.user_service.model.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
/** Các truy vấn dữ liệu danh mục kỹ năng. */
public interface SkillRepository extends JpaRepository<Skill, UUID> {
    boolean existsByNormalizedName(String normalizedName);
    Page<Skill> findByActiveTrueAndNameContainingIgnoreCase(String query, Pageable pageable);
}
