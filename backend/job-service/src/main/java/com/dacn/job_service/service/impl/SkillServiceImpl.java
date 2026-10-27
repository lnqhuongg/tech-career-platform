package com.dacn.job_service.service.impl;

import com.dacn.job_service.dto.SkillResponse;
import com.dacn.job_service.enums.SkillCategory;
import com.dacn.job_service.model.Skill;
import com.dacn.job_service.repository.SkillRepository;
import com.dacn.job_service.service.ISkillService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements ISkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public List<SkillResponse> getAll(SkillCategory category) {
        List<Skill> skills;
        if (category != null) {
            skills = skillRepository.findByCategory(category);
        } else {
            skills = skillRepository.findAll();
        }
        return skills.stream()
                .map(this::toSkillResponse)
                .collect(Collectors.toList());
    }

    private SkillResponse toSkillResponse(Skill skill) {
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .build();
    }
}
