package com.dacn.user_service.service;

import com.dacn.user_service.dto.request.SkillRequest;
import com.dacn.user_service.model.Skill;

import java.util.UUID;

/** Khai báo các thao tác ADMIN dùng để tạo và cập nhật danh mục kỹ năng chung. */
public interface AdminSkillService {

    /** Tạo kỹ năng mới sau khi chuẩn hóa và kiểm tra tên không bị trùng. */
    Skill create(SkillRequest request);

    /** Cập nhật tên, mô tả và trạng thái hoạt động của kỹ năng đã tồn tại. */
    Skill update(UUID skillId, SkillRequest request);
}
