package com.dacn.user_service.service.impl;

import com.dacn.user_service.exception.ResourceNotFoundException;
import com.dacn.user_service.model.Skill;
import com.dacn.user_service.repository.SkillRepository;
import com.dacn.user_service.service.SkillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/** Triển khai việc tra cứu danh mục kỹ năng dùng chung; không quản lý kỹ năng gắn vào hồ sơ. */
@Service
@Transactional(readOnly = true)
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    /** Chỉ trả về kỹ năng đang hoạt động; từ khóa rỗng được xem như không lọc theo tên. */
    @Override
    public Page<Skill> list(String query, Pageable pageable) {
        return skillRepository.findByActiveTrueAndNameContainingIgnoreCase(
                Optional.ofNullable(query).orElse("").trim(), pageable);
    }

    /** Không cho phép tra cứu qua API thông thường những kỹ năng đã bị vô hiệu hóa. */
    @Override
    public Skill getActive(UUID skillId) {
        return skillRepository.findById(skillId)
                .filter(Skill::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kỹ năng"));
    }
}
