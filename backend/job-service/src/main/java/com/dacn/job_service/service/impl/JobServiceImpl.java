package com.dacn.job_service.service.impl;

import com.dacn.job_service.common.AppException;
import com.dacn.job_service.dto.*;
import com.dacn.job_service.enums.JobLevel;
import com.dacn.job_service.enums.JobStatus;
import com.dacn.job_service.enums.JobType;
import com.dacn.job_service.enums.WorkMode;
import com.dacn.job_service.model.Company;
import com.dacn.job_service.model.Job;
import com.dacn.job_service.model.JobCategory;
import com.dacn.job_service.model.Skill;
import com.dacn.job_service.model.SavedJob;
import com.dacn.job_service.model.SavedJobId;
import com.dacn.job_service.repository.CompanyRepository;
import com.dacn.job_service.repository.JobCategoryRepository;
import com.dacn.job_service.repository.JobRepository;
import com.dacn.job_service.repository.SavedJobRepository;
import com.dacn.job_service.repository.SkillRepository;
import com.dacn.job_service.repository.specification.JobSpecification;
import com.dacn.job_service.service.IJobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements IJobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final SkillRepository skillRepository;
    private final SavedJobRepository savedJobRepository;

    public JobServiceImpl(JobRepository jobRepository,
                          CompanyRepository companyRepository,
                          JobCategoryRepository jobCategoryRepository,
                          SkillRepository skillRepository,
                          SavedJobRepository savedJobRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.skillRepository = skillRepository;
        this.savedJobRepository = savedJobRepository;
    }

    /* 1. LẤY TẤT CẢ JOB ĐANG MỞ (CÓ PHÂN TRANG) */
    @Override
    public Page<JobResponse> getAll(int page, int size) {
        JobFilterRequest defaultFilter = new JobFilterRequest();
        defaultFilter.setStatus(JobStatus.PUBLISHED);
        return filter(defaultFilter, page, size);
    }

    /* 2. LỌC JOB ĐA TIÊU CHÍ */
    @Override
    public Page<JobResponse> filter(JobFilterRequest request, int page, int size) {
        // Sắp xếp bài mới đăng lên đầu tiên
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Specification<Job> spec = JobSpecification.filterJobs(request);

        Page<Job> jobPage = jobRepository.findAll(spec, pageable);
        return jobPage.map(this::toJobResponse);
    }

    /* 3. LẤY CHI TIẾT JOB THEO ID VÀ TĂNG LƯỢT XEM */
    @Override
    @Transactional
    public JobDetailResponse getById(UUID id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Không tìm thấy công việc với ID: " + id));

        // Tự động tăng lượt xem khi có người vào đọc chi tiết JD
        job.setViewsCount(job.getViewsCount() + 1);
        jobRepository.save(job);

        return toJobDetailResponse(job);
    }

    /* 4. TẠO MỚI TIN TUYỂN DỤNG */
    @Override
    @Transactional
    public JobDetailResponse create(CreateJobRequest request) {
        // 1. Validation dữ liệu đầu vào cơ bản
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new AppException(400, "Tiêu đề công việc không được để trống");
        }
        if (request.getCompanyId() == null) {
            throw new AppException(400, "ID công ty không được để trống");
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new AppException(400, "Mô tả công việc không được để trống");
        }
        if (request.getRequirements() == null || request.getRequirements().trim().isEmpty()) {
            throw new AppException(400, "Yêu cầu công việc không được để trống");
        }
        if (request.getLocation() == null || request.getLocation().trim().isEmpty()) {
            throw new AppException(400, "Địa điểm làm việc không được để trống");
        }
        if (request.getSalaryMin() != null && request.getSalaryMax() != null
                && request.getSalaryMin().compareTo(request.getSalaryMax()) > 0) {
            throw new AppException(400, "Mức lương tối thiểu không được lớn hơn mức lương tối đa");
        }

        // 2. Kiểm tra tồn tại công ty
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new AppException(404, "Không tìm thấy công ty với ID: " + request.getCompanyId()));

        // 3. Kiểm tra danh mục ngành nghề (nếu có truyền)
        JobCategory category = null;
        if (request.getCategoryId() != null) {
            category = jobCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(404, "Không tìm thấy danh mục ngành nghề với ID: " + request.getCategoryId()));
        }

        // 4. Lấy danh sách kỹ năng liên kết (nếu có truyền)
        List<Skill> skills = new ArrayList<>();
        if (request.getSkillIds() != null && !request.getSkillIds().isEmpty()) {
            skills = skillRepository.findAllById(request.getSkillIds());
        }

        // 5. Khởi tạo đối tượng Job và lưu vào Database
        Job job = Job.builder()
                .company(company)
                .category(category)
                .title(request.getTitle().trim())
                .description(request.getDescription().trim())
                .requirements(request.getRequirements().trim())
                .benefits(request.getBenefits())
                .jobType(request.getJobType() != null ? request.getJobType() : JobType.FULL_TIME)
                .workMode(request.getWorkMode() != null ? request.getWorkMode() : WorkMode.ON_SITE)
                .jobLevel(request.getJobLevel() != null ? request.getJobLevel() : JobLevel.MIDDLE)
                .experienceYears(request.getExperienceYears() != null ? request.getExperienceYears() : 0)
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .location(request.getLocation().trim())
                .addressDetail(request.getAddressDetail())
                .status(request.getStatus() != null ? request.getStatus() : JobStatus.PUBLISHED)
                .viewsCount(0)
                .expiresAt(request.getExpiresAt())
                .skills(skills)
                .build();

        Job savedJob = jobRepository.save(job);
        return toJobDetailResponse(savedJob);
    }

    /* 5. CẬP NHẬT TIN TUYỂN DỤNG */
    @Override
    @Transactional
    public JobDetailResponse update(UUID id, UpdateJobRequest request) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Không tìm thấy công việc với ID: " + id));

        if (request.getTitle() != null) {
            String trimmedTitle = request.getTitle().trim();
            if (trimmedTitle.isEmpty()) {
                throw new AppException(400, "Tiêu đề công việc không được để trống");
            }
            job.setTitle(trimmedTitle);
        }

        if (request.getDescription() != null) {
            String trimmedDesc = request.getDescription().trim();
            if (trimmedDesc.isEmpty()) {
                throw new AppException(400, "Mô tả công việc không được để trống");
            }
            job.setDescription(trimmedDesc);
        }

        if (request.getRequirements() != null) {
            String trimmedReq = request.getRequirements().trim();
            if (trimmedReq.isEmpty()) {
                throw new AppException(400, "Yêu cầu công việc không được để trống");
            }
            job.setRequirements(trimmedReq);
        }

        if (request.getBenefits() != null) {
            job.setBenefits(request.getBenefits());
        }

        if (request.getCategoryId() != null) {
            JobCategory category = jobCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(404, "Không tìm thấy danh mục ngành nghề với ID: " + request.getCategoryId()));
            job.setCategory(category);
        }

        if (request.getSkillIds() != null) {
            if (!request.getSkillIds().isEmpty()) {
                List<Skill> skills = skillRepository.findAllById(request.getSkillIds());
                job.setSkills(skills);
            } else {
                job.setSkills(new ArrayList<>());
            }
        }

        if (request.getJobType() != null) {
            job.setJobType(request.getJobType());
        }

        if (request.getWorkMode() != null) {
            job.setWorkMode(request.getWorkMode());
        }

        if (request.getJobLevel() != null) {
            job.setJobLevel(request.getJobLevel());
        }

        if (request.getExperienceYears() != null) {
            job.setExperienceYears(request.getExperienceYears());
        }

        if (request.getSalaryMin() != null) {
            job.setSalaryMin(request.getSalaryMin());
        }

        if (request.getSalaryMax() != null) {
            job.setSalaryMax(request.getSalaryMax());
        }

        if (job.getSalaryMin() != null && job.getSalaryMax() != null
                && job.getSalaryMin().compareTo(job.getSalaryMax()) > 0) {
            throw new AppException(400, "Mức lương tối thiểu không được lớn hơn mức lương tối đa");
        }

        if (request.getLocation() != null) {
            String trimmedLoc = request.getLocation().trim();
            if (trimmedLoc.isEmpty()) {
                throw new AppException(400, "Địa điểm làm việc không được để trống");
            }
            job.setLocation(trimmedLoc);
        }

        if (request.getAddressDetail() != null) {
            job.setAddressDetail(request.getAddressDetail());
        }

        if (request.getStatus() != null) {
            job.setStatus(request.getStatus());
        }

        if (request.getExpiresAt() != null) {
            job.setExpiresAt(request.getExpiresAt());
        }

        Job savedJob = jobRepository.save(job);
        return toJobDetailResponse(savedJob);
    }

    /* 6. ĐÓNG TIN TUYỂN DỤNG */
    @Override
    @Transactional
    public JobDetailResponse closeJob(UUID id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Không tìm thấy công việc với ID: " + id));

        job.setStatus(JobStatus.CLOSED);
        Job savedJob = jobRepository.save(job);
        return toJobDetailResponse(savedJob);
    }

    /* 7. XÓA TIN TUYỂN DỤNG */
    @Override
    @Transactional
    public void deleteJob(UUID id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Không tìm thấy công việc với ID: " + id));

        jobRepository.delete(job);
    }

    /* 8. LẤY DANH SÁCH BÀI ĐĂNG CỦA CÔNG TY */
    @Override
    public Page<JobResponse> getJobsByCompany(UUID companyId, int page, int size) {
        if (companyId == null) {
            throw new AppException(400, "ID công ty không được để trống");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return jobRepository.findByCompanyId(companyId, pageable).map(this::toJobResponse);
    }

    /* 9. LƯU VIỆC LÀM (BOOKMARK) */
    @Override
    @Transactional
    public void saveJob(UUID jobId, UUID seekerId) {
        if (seekerId == null) {
            throw new AppException(400, "ID ứng viên không được để trống");
        }
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppException(404, "Không tìm thấy công việc với ID: " + jobId));

        if (savedJobRepository.existsByIdJobSeekerIdAndIdJobId(seekerId, jobId)) {
            throw new AppException(409, "Bạn đã lưu công việc này trước đó rồi");
        }

        SavedJob savedJob = SavedJob.builder()
                .id(new SavedJobId(seekerId, jobId))
                .job(job)
                .build();

        savedJobRepository.save(savedJob);
    }

    /* 10. BỎ LƯU VIỆC LÀM */
    @Override
    @Transactional
    public void unsaveJob(UUID jobId, UUID seekerId) {
        if (seekerId == null) {
            throw new AppException(400, "ID ứng viên không được để trống");
        }
        if (!jobRepository.existsById(jobId)) {
            throw new AppException(404, "Không tìm thấy công việc với ID: " + jobId);
        }

        if (!savedJobRepository.existsByIdJobSeekerIdAndIdJobId(seekerId, jobId)) {
            throw new AppException(404, "Công việc này chưa được lưu trước đó");
        }

        savedJobRepository.deleteByIdJobSeekerIdAndIdJobId(seekerId, jobId);
    }

    /* 11. XEM DANH SÁCH VIỆC ĐÃ LƯU */
    @Override
    public Page<JobResponse> getSavedJobs(UUID seekerId, int page, int size) {
        if (seekerId == null) {
            throw new AppException(400, "ID ứng viên không được để trống");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "savedAt"));
        return savedJobRepository.findByIdJobSeekerId(seekerId, pageable)
                .map(savedJob -> toJobResponse(savedJob.getJob()));
    }

    // --- CÁC HÀM MAPPER CHUYỂN ENTITY THÀNH DTO ---

    private JobResponse toJobResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .company(toCompanySummary(job))
                .categoryName(job.getCategory() != null ? job.getCategory().getName() : null)
                .jobType(job.getJobType())
                .workMode(job.getWorkMode())
                .jobLevel(job.getJobLevel())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .location(job.getLocation())
                .status(job.getStatus())
                .skills(toSkillResponses(job))
                .expiresAt(job.getExpiresAt())
                .createdAt(job.getCreatedAt())
                .build();
    }

    private JobDetailResponse toJobDetailResponse(Job job) {
        return JobDetailResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .benefits(job.getBenefits())
                .company(toCompanySummary(job))
                .categoryName(job.getCategory() != null ? job.getCategory().getName() : null)
                .jobType(job.getJobType())
                .workMode(job.getWorkMode())
                .jobLevel(job.getJobLevel())
                .experienceYears(job.getExperienceYears())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .location(job.getLocation())
                .addressDetail(job.getAddressDetail())
                .status(job.getStatus())
                .viewsCount(job.getViewsCount())
                .skills(toSkillResponses(job))
                .expiresAt(job.getExpiresAt())
                .createdAt(job.getCreatedAt())
                .build();
    }

    private CompanySummaryResponse toCompanySummary(Job job) {
        if (job.getCompany() == null) return null;
        return CompanySummaryResponse.builder()
                .id(job.getCompany().getId())
                .name(job.getCompany().getName())
                .logoUrl(job.getCompany().getLogoUrl())
                .city(job.getCompany().getCity())
                .build();
    }

    private List<SkillResponse> toSkillResponses(Job job) {
        if (job.getSkills() == null) return Collections.emptyList();
        return job.getSkills().stream()
                .map(s -> SkillResponse.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .category(s.getCategory())
                        .build())
                .collect(Collectors.toList());
    }
}
