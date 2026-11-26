package com.dacn.resume_service.service;

import com.dacn.resume_service.dto.request.SaveParsedDataRequest;
import com.dacn.resume_service.dto.request.UpdateResumeRequest;
import com.dacn.resume_service.dto.response.ResumeResponse;
import com.dacn.resume_service.security.CurrentUser;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IResumeService {

    /**
     * Tải lên file CV (PDF), lưu file, trích xuất text và tạo bản nháp bóc tách.
     */
    ResumeResponse uploadResume(CurrentUser user, MultipartFile file, String title);

    /**
     * Lấy danh sách toàn bộ CV của ứng viên đang đăng nhập.
     */
    List<ResumeResponse> getMyResumes(CurrentUser user);

    /**
     * Lấy chi tiết một CV kèm dữ liệu bóc tách có cấu trúc.
     */
    ResumeResponse getResumeById(CurrentUser user, UUID resumeId);

    /**
     * Cập nhật thông tin quản lý CV (tiêu đề, đặt làm mặc định).
     */
    ResumeResponse updateResume(CurrentUser user, UUID resumeId, UpdateResumeRequest request);

    /**
     * Ứng viên xác nhận và chỉnh sửa thông tin bóc tách (Human-in-the-loop).
     */
    ResumeResponse saveParsedData(CurrentUser user, UUID resumeId, SaveParsedDataRequest request);

    /**
     * Đặt một CV làm CV mặc định cho tài khoản.
     */
    ResumeResponse setDefault(CurrentUser user, UUID resumeId);

    /**
     * Xóa mềm CV.
     */
    void deleteResume(CurrentUser user, UUID resumeId);

    /**
     * Tải nội dung file PDF để tải về hoặc xem trực tiếp trên trình duyệt.
     */
    Resource loadFileResource(UUID resumeId, CurrentUser user);

    /**
     * Lấy văn bản thô của CV (dùng cho AI service đối sánh hoặc nhúng vector).
     */
    String getRawText(UUID resumeId);

    /**
     * Thẩm định toàn diện chất lượng CV bằng AI (UC13): Chấm điểm, nhận diện điểm mạnh/yếu và lời khuyên.
     */
    com.dacn.resume_service.dto.ai.AiCvAnalysisResponse analyzeResumeQuality(CurrentUser user, UUID resumeId);
}
