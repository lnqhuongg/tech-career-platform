    DROP SCHEMA IF EXISTS application_service CASCADE;

    CREATE SCHEMA IF NOT EXISTS application_service
        AUTHORIZATION postgres;

    /* -------------------------------------------------------
    * TABLE: APPLICATION (Lưu thông tin xin việc của ứng viên)
    */ -------------------------------------------------------
    CREATE TABLE applications (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

        job_id UUID NOT NULL,
        job_seeker_id UUID NOT NULL,
        resume_id UUID,

        -- thư ứng tuyển
        cover_letter TEXT,

        status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

        CONSTRAINT chk_application_status
          CHECK (status IN (
            'PENDING',
            'REVIEWING',
            'INTERVIEW',
            'ACCEPTED',
            'REJECTED',
            'WITHDRAWN'
          ))
    );

    /* -------------------------------------------------------
    * TABLE: APPLICATION STATUS HISTORY (Lịch sử xin việc)
    */ -------------------------------------------------------
    CREATE TABLE application_status_history (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

        application_id UUID NOT NULL,

        status VARCHAR(20) NOT NULL,

        -- thay đổi bởi recruiter nào
        changed_by_user_id UUID,

        note TEXT,

        changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

        CONSTRAINT fk_application_history_application
            FOREIGN KEY (application_id)
                REFERENCES applications(id)
                ON DELETE CASCADE,

        CONSTRAINT chk_history_status
            CHECK (status IN (
              'PENDING',
              'REVIEWING',
              'INTERVIEW',
              'ACCEPTED',
              'REJECTED',
              'WITHDRAWN'
            ))
    );

    /* -------------------------------------------------------
    * INDEXES: Tạo các index (cho việc tìm kiếm)
    */ -------------------------------------------------------
    CREATE INDEX idx_applications_job_id
        ON applications(job_id);

    CREATE INDEX idx_applications_job_seeker_id
        ON applications(job_seeker_id);

    CREATE INDEX idx_applications_status
        ON applications(status);

    CREATE INDEX idx_application_history_application_id
        ON application_status_history(application_id);