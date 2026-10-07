-- =============================================================================
-- TECH CAREER PLATFORM - JOB SERVICE SCHEMA (POSTGRESQL)
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

    -- Nhà tuyển dụng đại diện / tạo công ty (tham chiếu sang User Service)
    recruiter_id UUID NOT NULL,

    name VARCHAR(255) NOT NULL,
    logo_url VARCHAR(500),
    banner_url VARCHAR(500),
    website VARCHAR(255),
    
    company_size VARCHAR(50) DEFAULT '50-150',  -- '1-50', '50-150', '150-500', '500-1000', '1000+'
    industry VARCHAR(100),                      -- 'IT - Phần mềm', 'Fintech', 'Thương mại điện tử',...
    description TEXT,
    
    address VARCHAR(255),                       -- Trụ sở chính
    city VARCHAR(100),                          -- TP. Hồ Chí Minh, Hà Nội, Đà Nẵng,...
    country VARCHAR(100) DEFAULT 'Vietnam',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

/* -----------------------------------------------------------------------------
 * 2. TABLE: JOB_CATEGORIES (Danh mục ngành nghề / Chuyên môn)
 * ----------------------------------------------------------------------------- */
CREATE TABLE job_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,          -- Tên hiển thị (VD: Lập trình phần mềm)
    code VARCHAR(50) NOT NULL UNIQUE,          -- Mã định danh (VD: SOFTWARE_ENGINEERING)
    description VARCHAR(255),
    icon_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

/* -----------------------------------------------------------------------------
 * 3. TABLE: SKILLS (Danh mục kỹ năng công nghệ)
 * ----------------------------------------------------------------------------- */
CREATE TABLE skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,          -- Java, Spring Boot, React, AWS, Docker,...
    category VARCHAR(50) NOT NULL,              -- BACKEND, FRONTEND, MOBILE, DEVOPS, AI_DATA, DATABASE, TESTING, SECURITY
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

/* -----------------------------------------------------------------------------
 * 4. TABLE: JOBS (Tin tuyển dụng)
 * ----------------------------------------------------------------------------- */
CREATE TABLE jobs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Quan hệ nội bộ & liên service
    company_id UUID NOT NULL,
    recruiter_id UUID NOT NULL,                 -- ID Recruiter đăng bài (User Service)
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

    -- Yêu cầu kinh nghiệm (năm)
    experience_years_min INT DEFAULT 0,
    experience_years_max INT,

    -- Lương thưởng
    salary_type VARCHAR(20) NOT NULL DEFAULT 'RANGE',
    salary_min DECIMAL(15, 2),
    salary_max DECIMAL(15, 2),
    currency VARCHAR(10) NOT NULL DEFAULT 'VND',
    is_salary_negotiable BOOLEAN NOT NULL DEFAULT FALSE,

    -- Địa điểm & Số lượng tuyển
    quantity INT NOT NULL DEFAULT 1,
    location VARCHAR(100) NOT NULL,             -- TP.HCM, Hà Nội, Đà Nẵng, Remote,...
    address_detail VARCHAR(255),                -- Địa chỉ văn phòng làm việc cụ thể

    -- Trạng thái & Thống kê vòng đời
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    views_count INT NOT NULL DEFAULT 0,
    applications_count INT NOT NULL DEFAULT 0,

    published_at TIMESTAMP,
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
        CHECK (job_type IN ('FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERNSHIP', 'FREELANCE')),

    CONSTRAINT chk_jobs_work_mode
        CHECK (work_mode IN ('ON_SITE', 'REMOTE', 'HYBRID')),

    CONSTRAINT chk_jobs_job_level
        CHECK (job_level IN ('INTERN', 'FRESHER', 'JUNIOR', 'MIDDLE', 'SENIOR', 'LEAD', 'MANAGER', 'DIRECTOR')),

    CONSTRAINT chk_jobs_salary_type
        CHECK (salary_type IN ('RANGE', 'FIXED', 'NEGOTIABLE')),

    CONSTRAINT chk_jobs_status
        CHECK (status IN ('DRAFT', 'PENDING_APPROVAL', 'PUBLISHED', 'CLOSED', 'EXPIRED', 'REJECTED'))
);

/* -----------------------------------------------------------------------------
 * 5. TABLE: JOB_SKILLS (Bảng trung gian: Kỹ năng yêu cầu cho Job)
 * ----------------------------------------------------------------------------- */
CREATE TABLE job_skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id UUID NOT NULL,
    skill_id UUID NOT NULL,
    is_required BOOLEAN NOT NULL DEFAULT TRUE,  -- TRUE: Must-have, FALSE: Nice-to-have
    min_years_experience INT DEFAULT 0,

    CONSTRAINT fk_job_skills_job
        FOREIGN KEY (job_id)
            REFERENCES jobs(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_job_skills_skill
        FOREIGN KEY (skill_id)
            REFERENCES skills(id)
            ON DELETE CASCADE,

    CONSTRAINT uq_job_skill
        UNIQUE (job_id, skill_id)
);

/* -----------------------------------------------------------------------------
 * 6. TABLE: SAVED_JOBS (Việc làm ứng viên đã lưu lại)
 * ----------------------------------------------------------------------------- */
CREATE TABLE saved_jobs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_seeker_id UUID NOT NULL,                -- Ứng viên (User Service)
    job_id UUID NOT NULL,
    saved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_saved_jobs_job
        FOREIGN KEY (job_id)
            REFERENCES jobs(id)
            ON DELETE CASCADE,

    CONSTRAINT uq_saved_job_seeker
        UNIQUE (job_seeker_id, job_id)
);

/* -----------------------------------------------------------------------------
 * 7. INDEXES (Tối ưu hóa truy vấn & tìm kiếm)
 * ----------------------------------------------------------------------------- */
-- Company
CREATE INDEX idx_companies_recruiter_id ON companies(recruiter_id);
CREATE INDEX idx_companies_name ON companies(name);

-- Job filters & listings
CREATE INDEX idx_jobs_company_id ON jobs(company_id);
CREATE INDEX idx_jobs_recruiter_id ON jobs(recruiter_id);
CREATE INDEX idx_jobs_category_id ON jobs(category_id);
CREATE INDEX idx_jobs_status_published_at ON jobs(status, published_at DESC);
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

/* -----------------------------------------------------------------------------
 * 8. SEED DATA MẪU (Dữ liệu ban đầu cho danh mục và kỹ năng)
 * ----------------------------------------------------------------------------- */
-- Danh mục chuyên môn IT
INSERT INTO job_categories (name, code, description) VALUES
('Phát triển phần mềm', 'SOFTWARE_ENGINEERING', 'Backend, Frontend, Fullstack, Mobile Development'),
('Dữ liệu & Trí tuệ nhân tạo', 'DATA_AI', 'Data Engineering, Data Science, Machine Learning, AI'),
('Điện toán đám mây & DevOps', 'CLOUD_DEVOPS', 'DevOps, Cloud Architecture, SRE, SysAdmin'),
('Kiểm thử phần mềm', 'QA_QC', 'Manual Testing, Automation Testing, QA Lead'),
('Quản lý sản phẩm & Dự án', 'PRODUCT_PROJECT', 'Product Owner, Product Manager, Scrum Master, PM'),
('Bảo mật & An toàn thông tin', 'CYBER_SECURITY', 'Security Engineer, Penetration Tester, SOC')
ON CONFLICT (code) DO NOTHING;

-- Danh mục kỹ năng công nghệ
INSERT INTO skills (name, category) VALUES
-- Backend
('Java', 'BACKEND'),
('Spring Boot', 'BACKEND'),
('Node.js', 'BACKEND'),
('Python', 'BACKEND'),
('Golang', 'BACKEND'),
('.NET', 'BACKEND'),
-- Frontend
('ReactJS', 'FRONTEND'),
('VueJS', 'FRONTEND'),
('Angular', 'FRONTEND'),
('TypeScript', 'FRONTEND'),
('Next.js', 'FRONTEND'),
-- Mobile
('Flutter', 'MOBILE'),
('React Native', 'MOBILE'),
('Swift (iOS)', 'MOBILE'),
('Kotlin (Android)', 'MOBILE'),
-- Database
('PostgreSQL', 'DATABASE'),
('MySQL', 'DATABASE'),
('MongoDB', 'DATABASE'),
('Redis', 'DATABASE'),
-- DevOps & Cloud
('Docker', 'DEVOPS'),
('Kubernetes', 'DEVOPS'),
('AWS', 'DEVOPS'),
('CI/CD', 'DEVOPS'),
-- AI & Data
('TensorFlow', 'AI_DATA'),
('PyTorch', 'AI_DATA'),
('Pandas', 'AI_DATA')
ON CONFLICT (name) DO NOTHING;
