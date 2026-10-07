package com.dacn.job_service.repository;

import com.dacn.job_service.model.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, UUID> {
    Optional<JobCategory> findByCode(String code);
}
