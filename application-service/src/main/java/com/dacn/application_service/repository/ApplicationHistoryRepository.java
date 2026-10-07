package com.dacn.application_service.repository;

import com.dacn.application_service.model.ApplicationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApplicationHistoryRepository extends JpaRepository<ApplicationHistory, UUID> {
}
