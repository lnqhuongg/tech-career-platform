package com.dacn.user_service.service;

import com.dacn.user_service.dto.request.CandidateProfileRequest;
import com.dacn.user_service.dto.request.RecruiterProfileRequest;
import com.dacn.user_service.model.Candidate;
import com.dacn.user_service.model.Recruiter;
import com.dacn.user_service.security.CurrentUser;

import java.util.UUID;

/**
 * Khai báo các nghiệp vụ hồ sơ mà người dùng đã đăng nhập có thể thực hiện.
 * Các thao tác quản trị như tìm kiếm toàn bộ hồ sơ hoặc xác minh recruiter
 * được tách sang {@link AdminProfileService} để mỗi interface có trách nhiệm rõ ràng.
 */
public interface ProfileService {

    /** Tạo hồ sơ ứng viên cho tài khoản ứng viên hiện tại. */
    Candidate createCandidate(CurrentUser user, CandidateProfileRequest request);

    /** Lấy hồ sơ ứng viên thuộc tài khoản hiện tại. */
    Candidate myCandidate(CurrentUser user);

    /** Cập nhật hồ sơ ứng viên thuộc tài khoản hiện tại. */
    Candidate updateCandidate(CurrentUser user, CandidateProfileRequest request);

    /** Lấy hồ sơ ứng viên theo mã định danh. Quyền truy cập được kiểm soát ở tầng bảo mật/API. */
    Candidate candidate(UUID candidateId);

    /** Tạo hồ sơ nhà tuyển dụng cho tài khoản recruiter hiện tại. */
    Recruiter createRecruiter(CurrentUser user, RecruiterProfileRequest request);

    /** Lấy hồ sơ recruiter thuộc tài khoản hiện tại. */
    Recruiter myRecruiter(CurrentUser user);

    /** Lấy hồ sơ recruiter theo mã để phục vụ API xem hồ sơ. */
    Recruiter recruiter(UUID recruiterId);

    /** Cập nhật hồ sơ recruiter thuộc tài khoản hiện tại. */
    Recruiter updateRecruiter(CurrentUser user, RecruiterProfileRequest request);
}
