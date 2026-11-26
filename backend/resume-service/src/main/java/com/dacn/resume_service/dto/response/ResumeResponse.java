package com.dacn.resume_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponse {
    /**
     * ID của hồ sơ CV nộp (ResumeParsedData ID)
     */
    private UUID id;

    /**
     * ID file PDF gốc nếu CV được tạo từ file upload (nullable)
     */
    private UUID resumeId;

    private UUID userId;
    private String title;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private String fileUrl;
    private String parsingStatus;
    private Boolean isDefault;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Dữ liệu bóc tách chi tiết của CV
    private ResumeParsedDataResponse parsedData;
}
