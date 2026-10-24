package com.dacn.user_service.dto.response;

import com.dacn.user_service.model.Candidate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Thông tin hồ sơ ứng viên được phép trả về cho API.
 */
@Getter
@Setter
@NoArgsConstructor
public class CandidateResponse {
    private UUID id;
    private UUID accountId;
    private String fullName;
    private String summary;
    private String phone;
    private String location;
    private Integer yearsOfExperience;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CandidateResponse(
            UUID id,
            UUID accountId,
            String fullName,
            String summary,
            String phone,
            String location,
            Integer yearsOfExperience,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.accountId = accountId;
        this.fullName = fullName;
        this.summary = summary;
        this.phone = phone;
        this.location = location;
        this.yearsOfExperience = yearsOfExperience;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** Chuyển entity ứng viên thành DTO phản hồi. */
    public static CandidateResponse from(Candidate candidate) {
        return new CandidateResponse(
                candidate.getId(),
                candidate.getAccountId(),
                candidate.getFullName(),
                candidate.getSummary(),
                candidate.getPhone(),
                candidate.getLocation(),
                candidate.getYearsOfExperience(),
                candidate.getCreatedAt(),
                candidate.getUpdatedAt()
        );
    }

}
