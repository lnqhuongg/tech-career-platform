-- =============================================================================
-- TECH CAREER PLATFORM - RESUME SERVICE SCHEMA (POSTGRESQL)
-- =============================================================================

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

/* -----------------------------------------------------------------------------
 * 1. TABLE: RESUMES (Kho lưu trữ file PDF upload & văn bản thô phục vụ bóc tách)
 * Bảng này thuần túy đóng vai trò lưu file đính kèm và trích xuất text.
 * ----------------------------------------------------------------------------- */
CREATE TABLE IF NOT EXISTS resumes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- ID tài khoản người tải lên
    user_id UUID NOT NULL,

    -- Thông tin file đính kèm
    file_url VARCHAR(500),                      -- Đường dẫn URL hoặc relative path
    file_name VARCHAR(255) NOT NULL,           -- Tên file gốc (VD: "NguyenVanA_Resume.pdf")
    file_size BIGINT NOT NULL,                 -- Kích thước file theo bytes
    file_type VARCHAR(50) DEFAULT 'application/pdf',

    -- Văn bản thô bóc tách tự động từ file PDF bằng Apache PDFBox
    raw_text TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_resumes_user_id ON resumes(user_id);

/* -----------------------------------------------------------------------------
 * 2. TABLE: RESUME_PARSED_DATA (Hồ sơ CV chính thức dùng để nộp việc & so khớp)
 * Đây là thực thể CV trung tâm: chứa tiêu đề, thông tin cá nhân, kỹ năng,
 * cờ mặc định, xóa mềm và trạng thái hoàn thiện.
 * ----------------------------------------------------------------------------- */
CREATE TABLE IF NOT EXISTS resume_parsed_data (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Thuộc về tài khoản ứng viên nào
    user_id UUID NOT NULL,

    -- Tiêu đề CV (VD: "CV Java Backend Fresher", "CV Frontend Developer")
    title VARCHAR(255) NOT NULL DEFAULT 'Hồ sơ nghề nghiệp',

    -- Liên kết tới file PDF gốc nếu được tạo từ upload (NULL nếu tự tạo form trên web)
    resume_id UUID REFERENCES resumes(id) ON DELETE SET NULL,

    -- Thông tin cá nhân nhận diện từ CV hoặc do ứng viên nhập
    candidate_name VARCHAR(150),
    target_role VARCHAR(150),                  -- Vị trí mong muốn (VD: "Java Backend Developer")
    phone VARCHAR(30),
    email VARCHAR(150),
    summary TEXT,                              -- Tóm tắt giới thiệu bản thân
    total_experience_months INT DEFAULT 0,     -- Tổng số tháng kinh nghiệm ước tính

    -- Trạng thái bóc tách / hoàn thiện:
    -- 'DRAFT' (Mới tải lên, AI vừa bóc tách xong, đang chờ ứng viên kiểm tra)
    -- 'COMPLETED' (Ứng viên đã xem xét, chỉnh sửa và xác nhận lưu, hoặc tạo trực tuyến)
    -- 'FAILED' (Lỗi đọc file PDF không có text hoặc file lỗi)
    parsing_status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',

    -- Đánh dấu CV chính dùng để nộp hồ sơ nhanh
    is_default BOOLEAN NOT NULL DEFAULT FALSE,

    -- Xóa mềm (tránh mất dữ liệu nếu đơn ứng tuyển trước đó đã nộp CV này)
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Danh sách kỹ năng chuẩn hóa (RẤT QUAN TRỌNG CHO SKILL GAP ANALYSIS)
    -- Mảng JSON các string: ["Java", "Spring Boot", "PostgreSQL", "Docker", "Git"]
    extracted_skills JSONB DEFAULT '[]'::jsonb,

    -- Lịch sử học vấn: [{"institution": "Đại học Sài Gòn", "major": "CNTT", "degree": "Cử nhân", "start_year": "2020", "end_year": "2024"}]
    educations JSONB DEFAULT '[]'::jsonb,

    -- Kinh nghiệm làm việc / Đồ án:
    -- [{"company": "ABC Tech", "position": "Backend Intern", "duration": "06/2023 - 01/2024", "description": "..."}]
    experiences JSONB DEFAULT '[]'::jsonb,

    -- Chứng chỉ / Giải thưởng: [{"name": "AWS Certified", "issuer": "Amazon", "year": "2025"}]
    certifications JSONB DEFAULT '[]'::jsonb,

    -- Đánh dấu ứng viên đã tự tay kiểm tra, chỉnh sửa (Human-in-the-loop)
    updated_by_user BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_parsing_status CHECK (parsing_status IN ('DRAFT', 'COMPLETED', 'FAILED'))
);

CREATE INDEX IF NOT EXISTS idx_parsed_data_user_id 
    ON resume_parsed_data(user_id) 
    WHERE is_deleted = FALSE;

CREATE INDEX IF NOT EXISTS idx_parsed_skills 
    ON resume_parsed_data USING gin (extracted_skills);
