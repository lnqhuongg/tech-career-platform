package com.dacn.user_service.repository;
import com.dacn.user_service.model.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
/** Các truy vấn dữ liệu hồ sơ nhà tuyển dụng. */
public interface RecruiterRepository extends JpaRepository<Recruiter, UUID> {
    Optional<Recruiter> findByAccountId(UUID accountId);
    Page<Recruiter> findByFullNameContainingIgnoreCase(String query, Pageable pageable);
}
