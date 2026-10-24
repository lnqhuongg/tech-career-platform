package com.dacn.job_service.repository;

import com.dacn.job_service.model.SavedJob;
import com.dacn.job_service.model.SavedJobId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, SavedJobId> {

    boolean existsByIdJobSeekerIdAndIdJobId(UUID jobSeekerId, UUID jobId);

    void deleteByIdJobSeekerIdAndIdJobId(UUID jobSeekerId, UUID jobId);

    Page<SavedJob> findByIdJobSeekerId(UUID jobSeekerId, Pageable pageable);
}
