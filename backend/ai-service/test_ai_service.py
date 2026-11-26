import sys
from schemas import ResumeParseRequest, JobMatchRequest, ChatRequest
from parser import parse_resume
from matcher import evaluate_job_match
from chatbot import chat_career_advisor

print("=== 1. Test Parse Resume ===")
sample_cv = """
NGUYỄN VĂN AN
Vị trí mong muốn: Java Backend Developer
Email: an.nguyen@gmail.com - SĐT: 0912345678
Tóm tắt: Lập trình viên Backend có 1 năm kinh nghiệm phát triển hệ thống với Java Spring Boot và PostgreSQL.
Kỹ năng: Java, Spring Boot, PostgreSQL, Docker, Git, RESTful API, ReactJS.
Học vấn: Đại học Sài Gòn (2020 - 2024), Chuyên ngành Kỹ thuật Phần mềm.
Kinh nghiệm:
Công ty ABC (06/2023 - 06/2024): Java Backend Intern
- Phát triển API RESTful với Spring Boot và PostgreSQL.
- Tối ưu truy vấn SQL và đóng gói ứng dụng với Docker.
"""

parsed = parse_resume(sample_cv)
print("Candidate Name:", parsed.candidate_name)
print("Target Role:", parsed.target_role)
print("Email:", parsed.email)
print("Phone:", parsed.phone)
print("Extracted Skills:", parsed.extracted_skills)
print("Summary:", parsed.summary)

print("\n=== 2. Test Job Matching & Skill Gap (Sentence-BERT) ===")
sample_req = JobMatchRequest(
    resume_text=sample_cv,
    candidate_skills=parsed.extracted_skills,
    job_title="Junior Java Backend Developer",
    job_description="Tuyển dụng lập trình viên Java Backend phát triển hệ thống Microservices. Yêu cầu thành thạo Java, Spring Boot, MySQL hoặc PostgreSQL. Biết sử dụng Kafka và Redis là lợi thế lớn.",
    required_skills=["Java", "Spring Boot", "PostgreSQL", "Kafka", "Redis"]
)

match_result = evaluate_job_match(sample_req)
print("Match Score:", match_result.match_score)
print("Semantic Score (Sentence-BERT):", match_result.semantic_score)
print("Skill Score:", match_result.skill_score)
print("Matched Skills:", match_result.matched_skills)
print("Missing Skills:", match_result.missing_skills)
print("AI Suggestions:", match_result.ai_suggestions)

print("\n=== 3. Test Career Chatbot ===")
chat_req = ChatRequest(
    message="Em nên chuẩn bị những kiến thức gì để phỏng vấn vị trí Java Backend?",
    resume_context=sample_cv
)
chat_res = chat_career_advisor(chat_req)
print("Chatbot Reply:\n", chat_res.reply)

print("\n>>> ALL LOCAL AI TESTS PASSED! <<<")
