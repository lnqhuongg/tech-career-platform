package com.dacn.user_service.repository;

import com.dacn.user_service.model.CandidateSkill;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Các truy vấn dữ liệu kỹ năng gắn với hồ sơ ứng viên. */
public interface CandidateSkillRepository extends JpaRepository<CandidateSkill, UUID> {

    /** Tải kèm thông tin kỹ năng để có thể ánh xạ sang DTO mà không phát sinh truy vấn lazy sau khi giao dịch kết thúc. */
    @EntityGraph(attributePaths = "skill")
    List<CandidateSkill> findByCandidateId(UUID candidateId);

    /** Tải kèm hồ sơ ứng viên và kỹ năng để kiểm tra quyền sở hữu, đồng thời trả dữ liệu đã cập nhật. */
    @EntityGraph(attributePaths = {"candidate", "skill"})
    Optional<CandidateSkill> findWithAssociationsById(UUID id);

    /** Kiểm tra ứng viên đã thêm kỹ năng này hay chưa để tránh tạo liên kết trùng lặp. */
    boolean existsByCandidateIdAndSkillId(UUID candidateId, UUID skillId);
}
