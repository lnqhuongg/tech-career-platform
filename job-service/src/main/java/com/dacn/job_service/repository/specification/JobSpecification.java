package com.dacn.job_service.repository.specification;

import com.dacn.job_service.dto.JobFilterRequest;
import com.dacn.job_service.enums.JobStatus;
import com.dacn.job_service.model.Job;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class JobSpecification {

    public static Specification<Job> filterJobs(JobFilterRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Tìm theo từ khóa trong Tiêu đề (Không phân biệt hoa thường)
            if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
                String keyword = "%" + request.getKeyword().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("title")), keyword));
            }

            // 2. Lọc theo địa điểm (Location)
            if (request.getLocation() != null && !request.getLocation().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("location"), request.getLocation().trim()));
            }

            // 3. Lọc theo Cấp bậc (JobLevel)
            if (request.getJobLevel() != null) {
                predicates.add(cb.equal(root.get("jobLevel"), request.getJobLevel()));
            }

            // 4. Lọc theo Hình thức (WorkMode: REMOTE, ON_SITE, HYBRID)
            if (request.getWorkMode() != null) {
                predicates.add(cb.equal(root.get("workMode"), request.getWorkMode()));
            }

            // 5. Lọc theo Loại hình công việc (JobType: FULL_TIME, PART_TIME...)
            if (request.getJobType() != null) {
                predicates.add(cb.equal(root.get("jobType"), request.getJobType()));
            }

            // 6. Lọc theo mức lương tối thiểu (Lương max của Job >= mức lương mong muốn)
            if (request.getMinSalary() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salaryMax"), request.getMinSalary()));
            }

            // 7. Lọc theo Danh mục chuyên môn (Category ID)
            if (request.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), request.getCategoryId()));
            }

            // 8. Trạng thái Job: Mặc định nếu không truyền thì chỉ lấy các Job PUBLISHED (đang tuyển)
            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            } else {
                predicates.add(cb.equal(root.get("status"), JobStatus.PUBLISHED));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
