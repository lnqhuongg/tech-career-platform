package com.dacn.user_service.service;

import com.dacn.user_service.dto.request.CandidateSkillRequest;
import com.dacn.user_service.dto.request.CandidateSkillUpdateRequest;
import com.dacn.user_service.model.CandidateSkill;
import com.dacn.user_service.security.CurrentUser;

import java.util.List;
import java.util.UUID;

/** Khai báo nghiệp vụ quản lý các kỹ năng gắn với hồ sơ ứng viên. */
public interface CandidateSkillService {

    /** Lấy danh sách kỹ năng của hồ sơ thuộc tài khoản ứng viên hiện tại. */
    List<CandidateSkill> mine(CurrentUser user);

    /** Lấy danh sách kỹ năng của một hồ sơ ứng viên đã tồn tại. */
    List<CandidateSkill> byCandidate(UUID candidateId);

    /** Thêm một kỹ năng đang hoạt động vào hồ sơ ứng viên hiện tại. */
    CandidateSkill add(CurrentUser user, CandidateSkillRequest request);

    /** Cập nhật mức độ thành thạo và số năm kinh nghiệm của kỹ năng thuộc hồ sơ hiện tại. */
    CandidateSkill update(CurrentUser user, UUID candidateSkillId, CandidateSkillUpdateRequest request);

    /** Gỡ kỹ năng khỏi hồ sơ sau khi xác minh quyền sở hữu. */
    void delete(CurrentUser user, UUID candidateSkillId);
}
