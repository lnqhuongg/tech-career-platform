package com.dacn.user_service.dto.request;

import com.dacn.user_service.model.VerificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dữ liệu cập nhật trạng thái xác minh nhà tuyển dụng.
 */
@Setter
@Getter
@NoArgsConstructor
public class VerificationRequest {
    @NotNull
    private VerificationStatus verificationStatus;

    public VerificationRequest(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}
