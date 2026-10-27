package com.dacn.user_service.repository;

import com.dacn.user_service.model.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/** Các truy vấn dữ liệu hồ sơ ứng viên. */
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {

    Optional<Candidate> findByAccountId(UUID accountId);

    /**
     * Optional location filter. When omitted or blank, profiles from every location are included.
     */
    @Query("""
            SELECT candidate
            FROM Candidate candidate
            WHERE (
                :location IS NULL
                OR :location = ''
                OR LOWER(COALESCE(candidate.location, '')) LIKE LOWER(CONCAT('%', :location, '%'))
            )
            """)
    Page<Candidate> search(
            @Param("location") String location,
            Pageable pageable
    );
}
