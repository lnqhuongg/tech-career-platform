package com.dacn.resume_service.service;

import com.dacn.resume_service.dto.request.EvaluateJobRequest;
import com.dacn.resume_service.dto.response.JobMatchEvaluationResponse;
import com.dacn.resume_service.security.CurrentUser;

import java.util.List;
import java.util.UUID;

public interface IJobMatchService {

    /**
     * Thực hiện đối sánh CV với tin tuyển dụng (Job) và phân tích khoảng cách kỹ năng (Skill Gap).
     */
    JobMatchEvaluationResponse evaluateMatch(CurrentUser user, UUID resumeId, UUID jobId, EvaluateJobRequest request);

    /**
     * Lấy kết quả đánh giá đã lưu trước đó giữa CV và Job.
     */
    JobMatchEvaluationResponse getEvaluation(CurrentUser user, UUID resumeId, UUID jobId);

    /**
     * Lấy toàn bộ danh sách các đánh giá Job Matching của một CV.
     */
    List<JobMatchEvaluationResponse> getEvaluationsByResume(CurrentUser user, UUID resumeId);
}
