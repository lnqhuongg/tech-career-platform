package com.dacn.user_service.service.impl;

import com.dacn.user_service.dto.request.CandidateProfileRequest;
import com.dacn.user_service.dto.request.RecruiterProfileRequest;
import com.dacn.user_service.exception.ConflictException;
import com.dacn.user_service.exception.ForbiddenException;
import com.dacn.user_service.exception.ResourceNotFoundException;
import com.dacn.user_service.model.Candidate;
import com.dacn.user_service.model.Recruiter;
import com.dacn.user_service.repository.CandidateRepository;
import com.dacn.user_service.repository.RecruiterRepository;
import com.dacn.user_service.security.CurrentUser;
import com.dacn.user_service.service.ProfileService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Triển khai các nghiệp vụ tự phục vụ của hồ sơ ứng viên và recruiter.
 *
 * <p>Lớp này chỉ xử lý hồ sơ gắn với tài khoản hiện tại và việc đọc hồ sơ theo
 * quy tắc hiển thị. Những thao tác quản trị diện rộng được đặt trong
 * {@code AdminProfileServiceImpl}. Thông tin đăng nhập, mật khẩu, refresh token
 * và trạng thái xác thực tài khoản thuộc phạm vi của Auth Service.</p>
 *
 * <p>Các phương thức thay đổi dữ liệu chạy trong transaction để bảo đảm những
 * thay đổi trên entity được lưu nhất quán. Những phương thức chỉ đọc được đánh
 * dấu read-only nhằm thể hiện rõ ý định và tránh chi phí không cần thiết.</p>
 */
@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    /** Truy cập hồ sơ ứng viên và tìm hồ sơ theo accountId. */
    private final CandidateRepository candidateRepository;
    /** Truy cập hồ sơ nhà tuyển dụng và kiểm tra hồ sơ theo accountId. */
    private final RecruiterRepository recruiterRepository;

    public ProfileServiceImpl(
            CandidateRepository candidateRepository,
            RecruiterRepository recruiterRepository
    ) {
        this.candidateRepository = candidateRepository;
        this.recruiterRepository = recruiterRepository;
    }

    /** Kiểm tra loại tài khoản, ngăn tạo trùng hồ sơ và lưu hồ sơ ứng viên mới. */
    public Candidate createCandidate(CurrentUser user, CandidateProfileRequest request) {
        requireAccountType(user, "CANDIDATE", "Chỉ tài khoản ứng viên mới được tạo hồ sơ ứng viên");

        if (candidateRepository.findByAccountId(user.getAccountId()).isPresent()) {
            throw new ConflictException("Hồ sơ ứng viên đã tồn tại");
        }

        Candidate candidate = new Candidate();
        candidate.setAccountId(user.getAccountId());
        apply(candidate, request);

        try {
            return candidateRepository.saveAndFlush(candidate);
        } catch (DataIntegrityViolationException exception) {
            // Xử lý cả trường hợp hai request đồng thời vượt qua bước kiểm tra ban đầu.
            throw new ConflictException("Tài khoản đã có hồ sơ ứng viên");
        }
    }

    /** Tìm hồ sơ bằng accountId lấy từ danh tính đã xác thực, không nhận ID tùy ý từ client. */
    public Candidate myCandidate(CurrentUser user) {
        return candidateRepository.findByAccountId(user.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ ứng viên"));
    }

    /** Tìm hồ sơ ứng viên theo mã định danh; quyền truy cập endpoint được cấu hình ở tầng bảo mật. */
    @Transactional(readOnly = true)
    @Override
    public Candidate candidate(UUID candidateId) {
        return candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ ứng viên"));
    }

    /** Chỉ cập nhật hồ sơ gắn với tài khoản hiện tại. */
    public Candidate updateCandidate(CurrentUser user, CandidateProfileRequest request) {
        Candidate candidate = myCandidate(user);
        apply(candidate, request);
        return candidate;
    }

    /** Kiểm tra loại tài khoản và bảo đảm mỗi tài khoản chỉ có một hồ sơ recruiter. */
    public Recruiter createRecruiter(CurrentUser user, RecruiterProfileRequest request) {
        requireAccountType(user, "RECRUITER", "Chỉ tài khoản tuyển dụng mới được tạo hồ sơ recruiter");

        if (recruiterRepository.findByAccountId(user.getAccountId()).isPresent()) {
            throw new ConflictException("Hồ sơ recruiter đã tồn tại");
        }

        Recruiter recruiter = new Recruiter();
        recruiter.setAccountId(user.getAccountId());
        apply(recruiter, request);

        try {
            return recruiterRepository.saveAndFlush(recruiter);
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("Tài khoản đã có hồ sơ recruiter");
        }
    }

    /** Tìm hồ sơ recruiter thuộc tài khoản đã đăng nhập. */
    public Recruiter myRecruiter(CurrentUser user) {
        return recruiterRepository.findByAccountId(user.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ recruiter"));
    }

    /** Lấy hồ sơ recruiter theo mã; quy tắc truy cập endpoint được cấu hình ở tầng bảo mật. */
    @Transactional(readOnly = true)
    public Recruiter recruiter(UUID recruiterId) {
        return recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ recruiter"));
    }

    /** Cập nhật thông tin hồ sơ recruiter của chính tài khoản hiện tại. */
    public Recruiter updateRecruiter(CurrentUser user, RecruiterProfileRequest request) {
        Recruiter recruiter = myRecruiter(user);
        apply(recruiter, request);
        return recruiter;
    }

    /** Từ chối thao tác nếu danh tính không thuộc đúng loại tài khoản nghiệp vụ. */
    private void requireAccountType(CurrentUser user, String expectedType, String message) {
        if (user == null || !expectedType.equals(user.getAccountType())) {
            throw new ForbiddenException(message);
        }
    }

    /** Sao chép các trường được phép cập nhật từ request sang entity ứng viên. */
    private void apply(Candidate candidate, CandidateProfileRequest request) {
        candidate.setFullName(request.getFullName().trim());
        candidate.setSummary(trimToNull(request.getSummary()));
        candidate.setPhone(trimToNull(request.getPhone()));
        candidate.setLocation(trimToNull(request.getLocation()));
        candidate.setYearsOfExperience(request.getYearsOfExperience());
    }

    /** Sao chép các trường hồ sơ recruiter được phép chỉnh sửa. */
    private void apply(Recruiter recruiter, RecruiterProfileRequest request) {
        recruiter.setFullName(request.getFullName().trim());
        recruiter.setCompanyId(request.getCompanyId());
        recruiter.setPhone(trimToNull(request.getPhone()));
    }

    /** Loại bỏ khoảng trắng đầu/cuối và chuẩn hóa chuỗi rỗng thành null. */
    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
