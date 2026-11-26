package com.dacn.resume_service.repository;

import com.dacn.resume_service.model.ResumeParsedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResumeParsedDataRepository extends JpaRepository<ResumeParsedData, UUID> {

    List<ResumeParsedData> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(UUID userId);

    Optional<ResumeParsedData> findByIdAndIsDeletedFalse(UUID id);

    Optional<ResumeParsedData> findByUserIdAndIsDefaultTrueAndIsDeletedFalse(UUID userId);

    Optional<ResumeParsedData> findByResumeId(UUID resumeId);

    Optional<ResumeParsedData> findByResumeIdAndIsDeletedFalse(UUID resumeId);

    void deleteByResumeId(UUID resumeId);

    @Modifying
    @Query("UPDATE ResumeParsedData r SET r.isDefault = false WHERE r.userId = :userId AND r.id <> :resumeParsedDataId")
    void clearOtherDefaults(@Param("userId") UUID userId, @Param("resumeParsedDataId") UUID resumeParsedDataId);
}
