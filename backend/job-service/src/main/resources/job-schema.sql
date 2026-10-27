-- =============================================================================
-- TECH CAREER PLATFORM - JOB SERVICE SCHEMA (POSTGRESQL) - REFINED
-- =============================================================================

DROP SCHEMA IF EXISTS job_service CASCADE;

CREATE SCHEMA IF NOT EXISTS job_service
    AUTHORIZATION postgres;

SET search_path TO job_service;

-- Kích hoạt extension hỗ trợ tạo UUID ngẫu nhiên nếu chưa có
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

/* -----------------------------------------------------------------------------
 * 1. TABLE: COMPANIES (Thông tin công ty tuyển dụng)
 * ----------------------------------------------------------------------------- */
CREATE TABLE companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    logo_url VARCHAR(500),
    website VARCHAR(255),
    
    company_size VARCHAR(50) DEFAULT '50-150',  -- '1-50', '50-150', '150-500', '500-1000', '1000+'
    industry VARCHAR(50),                       -- IT_SOFTWARE, FINTECH, ECOMMERCE,...
    description TEXT,
    
    address VARCHAR(255),                       -- Địa chỉ trụ sở
    city VARCHAR(100),                          -- TP. Hồ Chí Minh, Hà Nội, Đà Nẵng,...

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_companies_industry
        CHECK (industry IS NULL OR industry IN (
            'IT_SOFTWARE', 'FINTECH', 'ECOMMERCE', 'EDTECH',
            'LOGISTICS', 'GAME', 'AI_DATA', 'BANKING', 'HEALTHCARE', 'OTHER'
        ))
);

/* -----------------------------------------------------------------------------
 * 2. TABLE: JOB_CATEGORIES (Danh mục ngành nghề / Chuyên môn)
 * ----------------------------------------------------------------------------- */
CREATE TABLE job_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,          -- Tên hiển thị (VD: Lập trình phần mềm) ngành nghề lĩnh vực
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

/* -----------------------------------------------------------------------------
 * 3. TABLE: SKILLS (Danh mục kỹ năng công nghệ)
 * ----------------------------------------------------------------------------- */
CREATE TABLE skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,          -- Java, Spring Boot, React, AWS, Docker,...
    category VARCHAR(50) NOT NULL,              -- BACKEND, FRONTEND, MOBILE, DEVOPS, AI_DATA,...
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_skills_category
        CHECK (category IN (
            'BACKEND', 'FRONTEND', 'MOBILE', 'DATABASE',
            'DEVOPS', 'AI_DATA', 'TESTING', 'SECURITY', 'OTHER'
        ))
);

/* -----------------------------------------------------------------------------
 * 4. TABLE: JOBS (Tin tuyển dụng)
 * ----------------------------------------------------------------------------- */
CREATE TABLE jobs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Quan hệ nội bộ & liên service
    company_id UUID NOT NULL,
    category_id UUID,

    -- Nội dung chi tiết tin tuyển dụng
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,                  -- Mô tả công việc (JD)
    requirements TEXT NOT NULL,                 -- Yêu cầu chuyên môn
    benefits TEXT,                              -- Quyền lợi, đãi ngộ

    -- Phân loại & Cấp bậc
    job_type VARCHAR(30) NOT NULL DEFAULT 'FULL_TIME',
    work_mode VARCHAR(30) NOT NULL DEFAULT 'ON_SITE',
    job_level VARCHAR(30) NOT NULL DEFAULT 'MIDDLE',

    -- Yêu cầu kinh nghiệm (số năm tối thiểu)
    experience_years INT DEFAULT 0,

    -- Lương thưởng (NULL cả hai = Thỏa thuận)
    salary_min DECIMAL(15, 2),
    salary_max DECIMAL(15, 2),

    -- Địa điểm làm việc
    location VARCHAR(100) NOT NULL,             -- TP.HCM, Hà Nội, Đà Nẵng, Remote,...
    address_detail VARCHAR(255),                -- Địa chỉ văn phòng làm việc cụ thể

    -- Trạng thái & Thống kê
    status VARCHAR(30) NOT NULL DEFAULT 'PUBLISHED',
    views_count INT NOT NULL DEFAULT 0,

    expires_at TIMESTAMP,                       -- Hạn chót ứng tuyển
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Ràng buộc khóa ngoại
    CONSTRAINT fk_jobs_company
        FOREIGN KEY (company_id)
            REFERENCES companies(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_jobs_category
        FOREIGN KEY (category_id)
            REFERENCES job_categories(id)
            ON DELETE SET NULL,

    -- Ràng buộc kiểm tra giá trị (Check constraints)
    CONSTRAINT chk_jobs_job_type
        CHECK (job_type IN ('FULL_TIME', 'PART_TIME', 'CONTRACT', 'FREELANCE')),

    CONSTRAINT chk_jobs_work_mode
        CHECK (work_mode IN ('ON_SITE', 'REMOTE', 'HYBRID')),

    CONSTRAINT chk_jobs_job_level
        CHECK (job_level IN ('INTERN', 'FRESHER', 'JUNIOR', 'MIDDLE', 'SENIOR', 'LEAD', 'MANAGER', 'DIRECTOR')),

    CONSTRAINT chk_jobs_status
        CHECK (status IN ('PENDING', 'PUBLISHED', 'CLOSED', 'EXPIRED', 'REJECTED'))
);

/* -----------------------------------------------------------------------------
 * 5. TABLE: JOB_SKILLS (Bảng trung gian n - n: Kỹ năng yêu cầu cho Job)
 * ----------------------------------------------------------------------------- */
CREATE TABLE job_skills (
    job_id UUID NOT NULL,
    skill_id UUID NOT NULL,

    PRIMARY KEY (job_id, skill_id),

    CONSTRAINT fk_job_skills_job
        FOREIGN KEY (job_id)
            REFERENCES jobs(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_job_skills_skill
        FOREIGN KEY (skill_id)
            REFERENCES skills(id)
            ON DELETE CASCADE
);

/* -----------------------------------------------------------------------------
 * 6. TABLE: SAVED_JOBS (Việc làm ứng viên đã lưu lại)
 * ----------------------------------------------------------------------------- */
CREATE TABLE saved_jobs (
    job_seeker_id UUID NOT NULL,                -- Ứng viên (User Service)
    job_id UUID NOT NULL,
    saved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (job_seeker_id, job_id),

    CONSTRAINT fk_saved_jobs_job
        FOREIGN KEY (job_id)
            REFERENCES jobs(id)
            ON DELETE CASCADE
);

/* -----------------------------------------------------------------------------
 * 7. INDEXES (Tối ưu hóa truy vấn & tìm kiếm)
 * ----------------------------------------------------------------------------- */
-- Company
CREATE INDEX idx_companies_name ON companies(name);

-- Job filters & listings
CREATE INDEX idx_jobs_company_id ON jobs(company_id);
CREATE INDEX idx_jobs_category_id ON jobs(category_id);
CREATE INDEX idx_jobs_status_created_at ON jobs(status, created_at DESC);
CREATE INDEX idx_jobs_location ON jobs(location);
CREATE INDEX idx_jobs_job_level ON jobs(job_level);
CREATE INDEX idx_jobs_work_mode ON jobs(work_mode);
CREATE INDEX idx_jobs_job_type ON jobs(job_type);
CREATE INDEX idx_jobs_expires_at ON jobs(expires_at) WHERE status = 'PUBLISHED';

-- Job Skills
CREATE INDEX idx_job_skills_job_id ON job_skills(job_id);
CREATE INDEX idx_job_skills_skill_id ON job_skills(skill_id);

-- Saved Jobs
CREATE INDEX idx_saved_jobs_seeker_id ON saved_jobs(job_seeker_id);


