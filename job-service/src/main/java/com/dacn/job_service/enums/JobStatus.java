package com.dacn.job_service.enums;

public enum JobStatus {
    PENDING,            // Đang chờ Admin kiểm duyệt
    PUBLISHED,         // Đang tuyển công khai trên hệ thống
    CLOSED,            // Nhà tuyển dụng chủ động đóng tin
    EXPIRED,           // Đã hết hạn nộp hồ sơ
    REJECTED           // Tin bị từ chối duyệt
}
