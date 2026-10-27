package com.dacn.user_service.service;

import com.dacn.user_service.model.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/** Khai báo nghiệp vụ tra cứu danh mục kỹ năng dùng chung. */
public interface SkillService {

    /** Tìm kiếm và phân trang các kỹ năng đang hoạt động. */
    Page<Skill> list(String query, Pageable pageable);

    /** Lấy một kỹ năng đang hoạt động; không tiết lộ kỹ năng đã bị vô hiệu hóa. */
    Skill getActive(UUID skillId);
}
