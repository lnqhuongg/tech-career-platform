package com.dacn.job_service.service;

import com.dacn.job_service.dto.SkillResponse;
import com.dacn.job_service.enums.SkillCategory;
import java.util.List;

public interface ISkillService {
    List<SkillResponse> getAll(SkillCategory category);
}
