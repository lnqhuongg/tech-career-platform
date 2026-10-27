package com.dacn.user_service.service.impl;

import com.dacn.user_service.dto.request.CandidateSkillRequest;
import com.dacn.user_service.dto.request.CandidateSkillUpdateRequest;
import com.dacn.user_service.exception.ConflictException;
import com.dacn.user_service.exception.ForbiddenException;
import com.dacn.user_service.exception.ResourceNotFoundException;
import com.dacn.user_service.model.Candidate;
import com.dacn.user_service.model.CandidateSkill;
import com.dacn.user_service.model.Skill;
import com.dacn.user_service.repository.CandidateRepository;
import com.dacn.user_service.repository.CandidateSkillRepository;
import com.dacn.user_service.security.CurrentUser;
import com.dacn.user_service.service.CandidateSkillService;
import com.dacn.user_service.service.SkillService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Triển khai nghiệp vụ quản lý kỹ năng gắn với hồ sơ ứng viên.
 * Danh mục kỹ năng dùng chung được tra cứu qua {@link SkillService}; lớp này không
 * tạo, đổi tên hoặc vô hiệu hóa các kỹ năng trong danh mục.
 */
@Service
@Transactional
public class CandidateSkillServiceImpl implements CandidateSkillService {

    private final CandidateRepository candidateRepository;
    private final CandidateSkillRepository candidateSkillRepository;
    private final SkillService skillService;

    public CandidateSkillServiceImpl(
            CandidateRepository candidateRepository,
            CandidateSkillRepository candidateSkillRepository,
            SkillService skillService
    ) {
        this.candidateRepository = candidateRepository;
        this.candidateSkillRepository = candidateSkillRepository;
        this.skillService = skillService;
    }

    /** Lấy kỹ năng từ hồ sơ ứng viên suy ra từ danh tính đã xác thực. */
    @Override
    @Transactional(readOnly = true)
    public List<CandidateSkill> mine(CurrentUser user) {
        Candidate candidate = candidateFor(user);
        return candidateSkillRepository.findByCandidateId(candidate.getId());
    }

    /** Lấy kỹ năng của hồ sơ theo ID sau khi xác nhận hồ sơ đó tồn tại. */
    @Override
    @Transactional(readOnly = true)
    public List<CandidateSkill> byCandidate(UUID candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ ứng viên"));
        return candidateSkillRepository.findByCandidateId(candidate.getId());
    }

    /** Chỉ thêm kỹ năng đang hoạt động và chuyển xung đột dữ liệu thành lỗi nghiệp vụ dễ hiểu. */
    @Override
    public CandidateSkill add(CurrentUser user, CandidateSkillRequest request) {
        Candidate candidate = candidateFor(user);
        Skill skill = skillService.getActive(request.getSkillId());

        if (candidateSkillRepository.existsByCandidateIdAndSkillId(candidate.getId(), skill.getId())) {
            throw new ConflictException("Kỹ năng đã có trong hồ sơ");
        }

        CandidateSkill candidateSkill = new CandidateSkill();
        candidateSkill.setCandidate(candidate);
        candidateSkill.setSkill(skill);
        candidateSkill.setProficiencyLevel(request.getProficiencyLevel());
        candidateSkill.setYearsOfExperience(request.getYearsOfExperience());

        try {
            return candidateSkillRepository.saveAndFlush(candidateSkill);
        } catch (DataIntegrityViolationException exception) {
            // Ràng buộc duy nhất trong database xử lý trường hợp hai request chạy đồng thời.
            throw new ConflictException("Kỹ năng đã có trong hồ sơ");
        }
    }

    /** Chỉ cập nhật kỹ năng thực sự thuộc hồ sơ của tài khoản đang đăng nhập. */
    @Override
    public CandidateSkill update(
            CurrentUser user,
            UUID candidateSkillId,
            CandidateSkillUpdateRequest request
    ) {
        CandidateSkill candidateSkill = ownedCandidateSkill(user, candidateSkillId);
        candidateSkill.setProficiencyLevel(request.getProficiencyLevel());
        candidateSkill.setYearsOfExperience(request.getYearsOfExperience());
        return candidateSkill;
    }

    /** Xóa liên kết kỹ năng sau khi xác minh quyền sở hữu. */
    @Override
    public void delete(CurrentUser user, UUID candidateSkillId) {
        candidateSkillRepository.delete(ownedCandidateSkill(user, candidateSkillId));
    }

    /** Bảo đảm người gọi là CANDIDATE và đã có hồ sơ ứng viên. */
    private Candidate candidateFor(CurrentUser user) {
        if (user == null || !"CANDIDATE".equals(user.getAccountType())) {
            throw new ForbiddenException("Chỉ tài khoản ứng viên mới được quản lý kỹ năng của hồ sơ");
        }
        return candidateRepository.findByAccountId(user.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Bạn chưa tạo hồ sơ ứng viên"));
    }

    /** Tải kèm dữ liệu cần thiết và không tiết lộ kỹ năng thuộc hồ sơ của người khác. */
    private CandidateSkill ownedCandidateSkill(CurrentUser user, UUID candidateSkillId) {
        CandidateSkill candidateSkill = candidateSkillRepository.findWithAssociationsById(candidateSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kỹ năng trong hồ sơ"));

        if (user == null || !candidateSkill.getCandidate().getAccountId().equals(user.getAccountId())) {
            // Dùng 404 để không tiết lộ sự tồn tại của kỹ năng thuộc hồ sơ khác.
            throw new ResourceNotFoundException("Không tìm thấy kỹ năng trong hồ sơ");
        }
        return candidateSkill;
    }
}
