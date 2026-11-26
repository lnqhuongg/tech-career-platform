package com.dacn.resume_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateJobRequest {

    /**
     * Danh sách kỹ năng yêu cầu của tin tuyển dụng (lấy từ job-service hoặc do client gửi lên).
     */
    private List<String> requiredSkills;

    /**
     * Tiêu đề công việc (để sinh câu gợi ý phù hợp).
     */
    private String jobTitle;

    /**
     * Mô tả chi tiết công việc (Job Description) phục vụ phân tích ngữ nghĩa sâu bằng Sentence-BERT.
     */
    private String jobDescription;
}
