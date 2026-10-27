package com.dacn.user_service.service.impl;

import com.dacn.user_service.exception.ResourceNotFoundException;
import com.dacn.user_service.model.Candidate;
import com.dacn.user_service.model.Recruiter;
import com.dacn.user_service.model.VerificationStatus;
import com.dacn.user_service.repository.CandidateRepository;
import com.dacn.user_service.repository.RecruiterRepository;
import com.dacn.user_service.service.AdminProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Triển khai các thao tác quản trị hồ sơ ứng viên và recruiter.
 *
 * <p>Các phương thức trong lớp này phục vụ màn hình quản trị: tìm kiếm nhiều hồ sơ,
 * xem chi tiết theo mã và cập nhật trạng thái xác minh recruiter. Việc giới hạn
 * người gọi chỉ là ADMIN được thực hiện ở SecurityConfig; không nên xem việc tách
 * class này là thay thế cho cơ chế phân quyền.</p>
 *
 * <p>Những thao tác thay đổi trạng thái được thực hiện trong transaction để trạng
 * thái mới được ghi nhất quán. Các truy vấn danh sách chỉ đọc sử dụng transaction
 * read-only và Pageable để không tải toàn bộ dữ liệu cùng lúc.</p>
 */
@Service
@Transactional
public class AdminProfileServiceImpl implements AdminProfileService {

    /** Repository truy vấn hồ sơ ứng viên theo bộ lọc và mã định danh. */
    private final CandidateRepository candidateRepository;

    /** Repository truy vấn và cập nhật trạng thái xác minh recruiter. */
    private final RecruiterRepository recruiterRepository;

    public AdminProfileServiceImpl(
            CandidateRepository candidateRepository,
            RecruiterRepository recruiterRepository
    ) {
        this.candidateRepository = candidateRepository;
        this.recruiterRepository = recruiterRepository;
    }

    /** Chuẩn hóa bộ lọc tùy chọn rồi chuyển việc tìm kiếm/phân trang cho repository. */
    @Override
    @Transactional(readOnly = true)
    public Page<Candidate> candidates(String location, Pageable pageable) {
        return candidateRepository.search(
                normalizeFilter(location),
                pageable
        );
    }

    /** Trả về 404 nếu mã hồ sơ không tồn tại, tránh trả về giá trị null cho controller. */
    @Override
    @Transactional(readOnly = true)
    public Candidate findCandidateById(UUID candidateId) {
        return candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ ứng viên"));
    }

    /** Tìm kiếm recruiter theo tên không phân biệt chữ hoa/chữ thường. */
    @Override
    @Transactional(readOnly = true)
    public Page<Recruiter> recruiters(String query, Pageable pageable) {
        return recruiterRepository.findByFullNameContainingIgnoreCase(
                Optional.ofNullable(query).orElse("").trim(), pageable);
    }

    /** Lấy hồ sơ recruiter theo mã dành cho màn hình quản trị. */
    @Override
    @Transactional(readOnly = true)
    public Recruiter recruiterById(UUID recruiterId) {
        return recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ recruiter"));
    }

    /** Cập nhật trạng thái xác minh; trạng thái hợp lệ được ràng buộc bởi enum VerificationStatus. */
    @Override
    public Recruiter verifyRecruiter(UUID recruiterId, VerificationStatus status) {
        Recruiter recruiter = recruiterById(recruiterId);
        recruiter.setVerificationStatus(status);
        return recruiter;
    }

    /** Trả về null cho bộ lọc trống để repository có thể bỏ qua điều kiện tương ứng. */
    private String normalizeFilter(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
