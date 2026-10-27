-- Lược đồ cơ sở dữ liệu cho User Service.
-- Các bảng dưới đây chỉ lưu dữ liệu hồ sơ và kỹ năng; thông tin xác thực thuộc Auth Service.

-- Hồ sơ ứng viên: mỗi tài khoản chỉ có tối đa một hồ sơ.
CREATE TABLE candidates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    summary TEXT,
    phone VARCHAR(30),
    location VARCHAR(120),
    years_of_experience INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_candidate_experience CHECK (years_of_experience IS NULL OR years_of_experience >= 0)
);

-- Chỉ mục hỗ trợ lọc ứng viên theo địa điểm.
CREATE INDEX idx_candidates_location ON candidates(location);

-- Hồ sơ recruiter liên kết đến công ty do Job Service quản lý thông qua company_id; không tạo foreign key xuyên service.
CREATE TABLE recruiters (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    company_id UUID NOT NULL,
    phone VARCHAR(30),
    verification_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_recruiter_verification CHECK (verification_status IN ('PENDING', 'VERIFIED', 'REJECTED'))
);

-- Danh mục kỹ năng dùng chung; normalized_name giúp ngăn tên kỹ năng trùng nhau.
CREATE TABLE skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    normalized_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Liên kết kỹ năng với ứng viên, bao gồm mức độ thành thạo và số năm kinh nghiệm.
CREATE TABLE candidate_skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    candidate_id UUID NOT NULL REFERENCES candidates(id) ON DELETE CASCADE,
    skill_id UUID NOT NULL REFERENCES skills(id),
    proficiency_level VARCHAR(20) NOT NULL,
    years_of_experience INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_candidate_skill UNIQUE (candidate_id, skill_id),
    CONSTRAINT chk_candidate_skill_experience CHECK (years_of_experience IS NULL OR years_of_experience >= 0),
    CONSTRAINT chk_candidate_skill_level CHECK (proficiency_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'))
);

-- Chỉ mục hỗ trợ truy vấn danh sách ứng viên theo kỹ năng.
CREATE INDEX idx_candidate_skills_skill ON candidate_skills(skill_id);
