package com.dacn.user_service.service.impl;

import com.dacn.user_service.dto.request.SkillRequest;
import com.dacn.user_service.exception.ConflictException;
import com.dacn.user_service.exception.ResourceNotFoundException;
import com.dacn.user_service.model.Skill;
import com.dacn.user_service.repository.SkillRepository;
import com.dacn.user_service.service.AdminSkillService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

/**
 * Triển khai các thao tác quản trị danh mục kỹ năng.
 *
 * <p>Danh mục kỹ năng là dữ liệu dùng chung cho nhiều hồ sơ, vì vậy việc tạo và
 * chỉnh sửa được tách khỏi nghiệp vụ ứng viên tự quản lý kỹ năng của mình. Tên
 * kỹ năng được chuẩn hóa trước khi kiểm tra trùng để ngăn các bản ghi chỉ khác
 * chữ hoa/thường hoặc khoảng trắng.</p>
 *
 * <p>Kiểm tra trùng ở tầng ứng dụng giúp trả thông báo dễ hiểu; ràng buộc duy nhất
 * ở cơ sở dữ liệu vẫn là lớp bảo vệ cuối cùng khi nhiều request chạy đồng thời.</p>
 */
@Service
@Transactional
public class AdminSkillServiceImpl implements AdminSkillService {

    /** Repository lưu danh mục kỹ năng dùng chung. */
    private final SkillRepository skillRepository;

    public AdminSkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    /** Chuẩn hóa tên, kiểm tra trùng và lưu kỹ năng mới vào danh mục. */
    @Override
    public Skill create(SkillRequest request) {
        String displayName = request.getName().trim();
        String normalizedName = normalize(displayName);
        if (skillRepository.existsByNormalizedName(normalizedName)) {
            throw new ConflictException("Kỹ năng đã tồn tại");
        }

        Skill skill = new Skill();
        skill.setName(displayName);
        skill.setNormalizedName(normalizedName);
        skill.setDescription(trimToNull(request.getDescription()));
        if (request.getActive() != null) {
            skill.setActive(request.getActive());
        }

        try {
            return skillRepository.saveAndFlush(skill);
        } catch (DataIntegrityViolationException exception) {
            // Xử lý trường hợp hai request đồng thời vượt qua bước kiểm tra trùng ở trên.
            throw new ConflictException("Kỹ năng đã tồn tại");
        }
    }

    /** Cập nhật dữ liệu kỹ năng; nếu đổi tên thì phải bảo đảm tên mới chưa được sử dụng. */
    @Override
    public Skill update(UUID skillId, SkillRequest request) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kỹ năng"));
        String displayName = request.getName().trim();
        String normalizedName = normalize(displayName);

        if (!normalizedName.equals(skill.getNormalizedName())
                && skillRepository.existsByNormalizedName(normalizedName)) {
            throw new ConflictException("Kỹ năng đã tồn tại");
        }

        skill.setName(displayName);
        skill.setNormalizedName(normalizedName);
        skill.setDescription(trimToNull(request.getDescription()));
        if (request.getActive() != null) {
            skill.setActive(request.getActive());
        }
        return skill;
    }

    /** Chuẩn hóa tên để so sánh nhất quán, không phụ thuộc locale của máy chủ. */
    private String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    /** Chuyển mô tả trống thành null để dữ liệu lưu thống nhất. */
    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
