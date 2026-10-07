package com.dacn.application_service.repository;

import com.dacn.application_service.enums.ApplicationStatus;
import com.dacn.application_service.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    boolean existsByJobIdAndJobSeekerIdAndStatusIn(
            UUID jobId,
            UUID jobSeekerId,
            List<ApplicationStatus> statuses
    );

}
