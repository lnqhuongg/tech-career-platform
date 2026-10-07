package com.dacn.application_service.enums;

public enum ApplicationStatus {
    PENDING, // Đã nộp hồ sơ, chờ Recruiter/Employer xử lý
    REVIEWING, // Hồ sơ đang được xem xét
    INTERVIEW, // Ứng viên được mời phỏng vấn
    ACCEPTED, // Hồ sơ được chấp nhận (lúc này nhân viên vào công ty làm việc)
    REJECTED, // Ứng viên bị từ chối hồ sơ
    WITHDRAWN // Ứng viên tự rút hồ sơ sau khi nộp
}
