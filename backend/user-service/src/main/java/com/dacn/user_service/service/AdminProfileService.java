package com.dacn.user_service.service;

import com.dacn.user_service.model.Candidate;
import com.dacn.user_service.model.Recruiter;
import com.dacn.user_service.model.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Khai báo nghiệp vụ quản trị hồ sơ dành riêng cho ADMIN.
 * Interface này tách các thao tác tra cứu diện rộng và kiểm duyệt khỏi luồng tự phục vụ.
 */
public interface AdminProfileService {

    /** Tìm kiếm và phân trang hồ sơ ứng viên theo vị trí mong muốn và địa điểm. */
    Page<Candidate> candidates(String location, Pageable pageable);

    /** Lấy chi tiết hồ sơ ứng viên theo mã dành cho màn hình quản trị. */
    Candidate findCandidateById(UUID candidateId);

    /** Tìm kiếm và phân trang hồ sơ recruiter theo tên. */
    Page<Recruiter> recruiters(String query, Pageable pageable);

    /** Lấy chi tiết hồ sơ recruiter theo mã dành cho màn hình quản trị. */
    Recruiter recruiterById(UUID recruiterId);

    /** Cập nhật trạng thái xác minh recruiter sau khi ADMIN kiểm tra thông tin. */
    Recruiter verifyRecruiter(UUID recruiterId, VerificationStatus status);
}
