package com.dacn.application_service.service.impl;

import com.dacn.application_service.common.AppException;
import com.dacn.application_service.dto.ApplicationResponse;
import com.dacn.application_service.dto.CreateApplicationRequest;
import com.dacn.application_service.dto.CreateRejectedApplicationRequest;
import com.dacn.application_service.enums.ApplicationStatus;
import com.dacn.application_service.model.Application;
import com.dacn.application_service.model.ApplicationHistory;
import com.dacn.application_service.repository.ApplicationHistoryRepository;
import com.dacn.application_service.repository.ApplicationRepository;
import com.dacn.application_service.service.IApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ApplicationServiceImpl implements IApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationHistoryRepository applicationHistoryRepository;

    public ApplicationServiceImpl (
            ApplicationRepository applicationRepository,
            ApplicationHistoryRepository applicationHistoryRepository) {
        this.applicationRepository = applicationRepository;
        this.applicationHistoryRepository = applicationHistoryRepository;
    }

    /*
     * CREATE APPLICATION
     */
    @Override
    public ApplicationResponse create (CreateApplicationRequest request) {

        // Gọi user service kiểm tra tồn tại jobseeker id, kiểm tra thêm active, có phải là job seeker
        // Gọi job service kiểm tra tồn tại job id với job có đang open không
        // Gọi resume service kiểm tra tồn tại resume id

        // Kiểm tra đã nộp CV vô job đó chưa
        boolean exists = applicationRepository
            .existsByJobIdAndJobSeekerIdAndStatusIn(
                    request.getJobId(),
                    request.getJobSeekerId(),
                    List.of(
                            ApplicationStatus.PENDING,
                            ApplicationStatus.REVIEWING,
                            ApplicationStatus.INTERVIEW
                    )
            );

        if (exists) {
            throw new AppException(409, "Bạn đã nộp Hồ sơ ứng tuyển cho công việc này rồi!");
        }

        Application application = new Application();
        application.setJobId(request.getJobId());
        application.setJobSeekerId(request.getJobSeekerId());
        application.setResumeId(request.getResumeId());
        application.setCoverLetter(request.getCoverLetter());
        application.setStatus(ApplicationStatus.PENDING);
        application.setCreatedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());

        applicationRepository.save(application);

        ApplicationHistory applicationHistory = new ApplicationHistory();
        applicationHistory.setApplication(application);
        applicationHistory.setStatus(ApplicationStatus.PENDING);
        applicationHistory.setChangedAt(LocalDateTime.now());

        applicationHistoryRepository.save(applicationHistory);

        return toResponse(application);
    }

    /*
     * REJECTED APPLICATION (by recruiter)
     */
    @Override
    public ApplicationResponse reject (CreateRejectedApplicationRequest request, UUID id) {

        // Kiểm tra tồn tại application muốn reject
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Hồ sơ ứng tuyển không thể tìm thấy với id: " + id));

        // Kiểm tra trạng thái hồ sơ (chỉ có đang Reviewing & Interview mới có thể reject thôi)
        if (application.getStatus() == ApplicationStatus.REJECTED) {
            throw new AppException(409, "Hồ sơ ứng tuyển này đã bị từ chối trước đó rồi.");
        }

        if (application.getStatus() == ApplicationStatus.ACCEPTED) {
            throw new AppException(400, "Không thể từ chối hồ sơ đã được chấp nhận.");
        }

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new AppException(400, "Không thể từ chối hồ sơ đã được rút bởi ứng viên.");
        }

        if (application.getStatus() == ApplicationStatus.PENDING) {
            throw new AppException(400, "Không thể từ chối hồ sơ đang được chờ xử lý.");
        }

        application.setStatus(ApplicationStatus.REJECTED);
        application.setUpdatedAt(LocalDateTime.now());

        applicationRepository.save(application);

        ApplicationHistory history = ApplicationHistory.builder()
                .application(application)
                .status(ApplicationStatus.REJECTED)
                .changedByUserId(request.getRecruiterId())
                .note(request.getNote())
                .changedAt(LocalDateTime.now())
                .build();

        applicationHistoryRepository.save(history);

        return toResponse(application);
    }

    /*
     * GET ALL CÓ PHÂN TRANG
     */
    @Override
    public Page<ApplicationResponse> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Application> applicationPage =
                applicationRepository.findAll(pageable);

        return applicationPage.map(this::toResponse);
    }

    private ApplicationResponse toResponse(Application entity) {
        return ApplicationResponse.builder()
                .id(entity.getId())
                .jobId(entity.getJobId())
                .jobSeekerId(entity.getJobSeekerId())
                .resumeId(entity.getResumeId())
                .coverLetter(entity.getCoverLetter())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
