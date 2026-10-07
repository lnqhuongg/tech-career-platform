package com.dacn.job_service.enums;

public enum JobStatus {
    DRAFT,             // Bản nháp, chỉ nhà tuyển dụng thấy
    PENDING_APPROVAL,  // Đang chờ Admin kiểm duyệt
    PUBLISHED,         // Đang tuyển công khai trên hệ thống
    CLOSED,            // Nhà tuyển dụng chủ động đóng tin
    EXPIRED,           // Đã hết hạn nộp hồ sơ
    REJECTED           // Tin bị từ chối duyệt
}
