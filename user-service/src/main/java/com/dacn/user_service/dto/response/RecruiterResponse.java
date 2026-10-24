package com.dacn.user_service.dto.response;

import com.dacn.user_service.model.Recruiter;
import com.dacn.user_service.model.VerificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Thông tin hồ sơ nhà tuyển dụng được phép trả về cho API.
 */

@Setter
@Getter
@NoArgsConstructor
public class RecruiterResponse {
    private UUID id;
    private UUID accountId;
    private String fullName;
    private UUID companyId;
    private String phone;
    private VerificationStatus verificationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RecruiterResponse(
            UUID id,
            UUID accountId,
            String fullName,
            UUID companyId,
            String phone,
            VerificationStatus verificationStatus,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.accountId = accountId;
        this.fullName = fullName;
        this.companyId = companyId;
        this.phone = phone;
        this.verificationStatus = verificationStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    /** Chuyển entity nhà tuyển dụng thành DTO phản hồi. */
    public static RecruiterResponse from(Recruiter recruiter) {
        return new RecruiterResponse(
                recruiter.getId(),
                recruiter.getAccountId(),
                recruiter.getFullName(),
                recruiter.getCompanyId(),
                recruiter.getPhone(),
                recruiter.getVerificationStatus(),
                recruiter.getCreatedAt(),
                recruiter.getUpdatedAt()
        );
    }

}
