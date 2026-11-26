import re
import json
import logging
from typing import List, Dict, Any
from schemas import (
    ResumeParsedResponse, EducationItem, ExperienceItem, CertificationItem,
    CvAnalysisResponse
)
import config

logger = logging.getLogger(__name__)

COMMON_IT_SKILLS = [
    "Java", "Spring Boot", "Spring", "Hibernate", "JPA", "Python", "Django", "Flask", "FastAPI",
    "JavaScript", "TypeScript", "React", "ReactJS", "Vue", "VueJS", "Angular", "Node.js", "Express",
    "Next.js", "NestJS", "HTML", "CSS", "Tailwind CSS", "Bootstrap", "Sass",
    "C#", ".NET", "ASP.NET", "C++", "C", "PHP", "Laravel", "Golang", "Go", "Rust", "Kotlin", "Swift",
    "PostgreSQL", "MySQL", "MongoDB", "Redis", "Oracle", "SQL Server", "SQLite", "Elasticsearch",
    "Docker", "Kubernetes", "AWS", "Azure", "GCP", "CI/CD", "Git", "GitHub", "GitLab", "Jenkins",
    "Linux", "Ubuntu", "RESTful API", "GraphQL", "Microservices", "Kafka", "RabbitMQ",
    "Figma", "UI/UX", "Machine Learning", "NLP", "Deep Learning", "TensorFlow", "PyTorch",
    "Sentence-BERT", "BERT", "LLM", "Prompt Engineering", "OpenAI", "Gemini",
    "Unit Testing", "JUnit", "Jest", "Agile", "Scrum", "Jira"
]

def parse_resume(raw_text: str) -> ResumeParsedResponse:
    """
    Bóc tách thông tin từ văn bản CV sử dụng LLM (Google Gemini)
    kết hợp phương pháp Heuristic / Regex làm cơ chế Fallback đảm bảo tính liên tục.
    """
    if not raw_text or not raw_text.strip():
        return ResumeParsedResponse()

    # 1. Thử bóc tách bằng LLM (Google Gemini) nếu có cấu hình API Key
    if config.GEMINI_API_KEY:
        try:
            from google import genai
            from google.genai import types

            client = genai.Client(api_key=config.GEMINI_API_KEY)
            prompt = f"""
Bạn là chuyên gia phân tích CV và tuyển dụng IT hàng đầu. Hãy phân tích văn bản CV sau và trích xuất thông tin cấu trúc dưới dạng JSON thuần túy (không kèm markdown ```json ``` hoặc bất kỳ text nào khác).

Yêu cầu các trường dữ liệu JSON:
- candidate_name (string hoặc null): Họ và tên ứng viên.
- target_role (string hoặc null): Vị trí mong muốn hoặc chức danh chính (ví dụ: Java Backend Developer, Frontend Engineer...).
- phone (string hoặc null): Số điện thoại liên hệ.
- email (string hoặc null): Địa chỉ email.
- summary (string hoặc null): Tóm tắt hồ sơ ngắn gọn (2-3 câu).
- total_experience_months (integer): Tổng số tháng kinh nghiệm ước tính (nếu là sinh viên/mới tốt nghiệp thì 0-12).
- extracted_skills (danh sách string): Danh sách các kỹ năng kỹ thuật, công nghệ, ngôn ngữ, framework, database, tools tìm thấy.
- educations (danh sách object): [ {{"institution": "tên trường", "degree": "bằng cấp", "major": "ngành học", "start_year": "năm bắt đầu", "end_year": "năm kết thúc/hiện tại"}} ]
- experiences (danh sách object): [ {{"company": "tên công ty", "position": "vị trí", "duration": "thời gian", "description": "mô tả công việc và kết quả đạt được"}} ]
- certifications (danh sách object): [ {{"name": "tên chứng chỉ", "issuer": "tổ chức cấp", "year": "năm cấp"}} ]

Nội dung CV cần phân tích:
\"\"\"{raw_text[:6000]}\"\"\"
"""
            response = client.models.generate_content(
                model=config.GEMINI_MODEL,
                contents=prompt,
                config=types.GenerateContentConfig(
                    temperature=0.0,
                    max_output_tokens=4096,
                    thinking_config=types.ThinkingConfig(thinking_budget=0)
                )
            )

            if response and response.text:
                cleaned = response.text.strip()
                if cleaned.startswith("```json"):
                    cleaned = cleaned[7:]
                elif cleaned.startswith("```"):
                    cleaned = cleaned[3:]
                if cleaned.endswith("```"):
                    cleaned = cleaned[:-3]
                cleaned = cleaned.strip()

                data = json.loads(cleaned)
                return ResumeParsedResponse(
                    candidate_name=data.get("candidate_name"),
                    target_role=data.get("target_role"),
                    phone=data.get("phone"),
                    email=data.get("email"),
                    summary=data.get("summary"),
                    total_experience_months=int(data.get("total_experience_months", 0)),
                    extracted_skills=data.get("extracted_skills", []),
                    educations=[EducationItem(**e) for e in data.get("educations", [])],
                    experiences=[ExperienceItem(**exp) for exp in data.get("experiences", [])],
                    certifications=[CertificationItem(**c) for c in data.get("certifications", [])]
                )
        except Exception as e:
            logger.warning(f"Lỗi khi bóc tách CV qua Gemini: {e}. Kích hoạt Fallback trích xuất nội bộ.")

    # 2. Cơ chế Fallback: Trích xuất nội bộ dùng biểu thức chính quy (Regex) và từ điển
    email_match = re.search(r'[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+', raw_text)
    email = email_match.group(0) if email_match else None

    phone_match = re.search(r'(\+?84|0)(3|5|7|8|9)[0-9]{8}', raw_text.replace(" ", "").replace(".", "").replace("-", ""))
    phone = phone_match.group(0) if phone_match else None

    # Quét kỹ năng theo danh mục từ điển
    extracted_skills = []
    for skill in COMMON_IT_SKILLS:
        pattern = r'\b' + re.escape(skill) + r'\b'
        if re.search(pattern, raw_text, re.IGNORECASE):
            extracted_skills.append(skill)

    lines = [line.strip() for line in raw_text.splitlines() if line.strip()]
    candidate_name = lines[0] if lines else "Ứng viên"

    return ResumeParsedResponse(
        candidate_name=candidate_name,
        target_role="Lập trình viên CNTT",
        phone=phone,
        email=email,
        summary="Hồ sơ được trích xuất tự động từ CV cá nhân.",
        total_experience_months=0,
        extracted_skills=extracted_skills,
        educations=[],
        experiences=[],
        certifications=[]
    )

def analyze_cv_quality(raw_text: str, target_role: str = "") -> CvAnalysisResponse:
    """
    Thẩm định toàn diện chất lượng CV bằng AI (UC13):
    - Chấm điểm bố cục (format_score) và nội dung chuyên môn (content_score)
    - Nhận diện điểm sáng (strengths) và điểm thiếu sót (improvements)
    - Đưa ra hướng dẫn hành động cụ thể (actionable_advice) theo chuẩn CNTT
    """
    if not raw_text or not raw_text.strip():
        return CvAnalysisResponse(
            overall_score=0,
            format_score=0,
            content_score=0,
            strengths=[],
            improvements=["CV chưa có nội dung văn bản để phân tích"],
            actionable_advice="Vui lòng tải lên tệp CV chuẩn chứa nội dung văn bản để AI phân tích."
        )

    if config.GEMINI_API_KEY:
        try:
            from google import genai
            from google.genai import types

            client = genai.Client(api_key=config.GEMINI_API_KEY)
            role_hint = target_role if target_role else "Lập trình viên CNTT (Fresher/Junior)"
            prompt = f"""
Bạn là chuyên gia thẩm định CV và tuyển dụng nhân sự CNTT hàng đầu. Hãy đánh giá chi tiết bản CV dưới đây cho vị trí mục tiêu: {role_hint}.
Yêu cầu trả về đúng định dạng JSON thuần túy (không kèm markdown ```json ``` hoặc giải thích nào khác):
{{
  "overall_score": 85,
  "format_score": 88,
  "content_score": 82,
  "strengths": [
    "Điểm mạnh nổi bật 1 (ví dụ: Danh sách kỹ năng công nghệ phân loại rõ ràng)",
    "Điểm mạnh nổi bật 2 (ví dụ: Có đồ án thực hành thể hiện rõ công nghệ áp dụng)"
  ],
  "improvements": [
    "Điểm thiếu sót 1 (ví dụ: Phần mô tả dự án còn thiếu số liệu định lượng về kết quả)",
    "Điểm thiếu sót 2 (ví dụ: Chưa có liên kết GitHub hoặc sản phẩm demo để chứng minh)"
  ],
  "actionable_advice": "Lời khuyên hành động 2-3 câu: hướng dẫn cụ thể cách viết lại phần kinh nghiệm hoặc bổ sung kỹ năng để tăng cơ hội trúng tuyển."
}}

Nội dung CV cần đánh giá:
\"\"\"{raw_text[:6000]}\"\"\"
"""
            response = client.models.generate_content(
                model=config.GEMINI_MODEL,
                contents=prompt,
                config=types.GenerateContentConfig(
                    temperature=0.0,
                    max_output_tokens=2048,
                    thinking_config=types.ThinkingConfig(thinking_budget=0)
                )
            )

            if response and response.text:
                cleaned = response.text.strip()
                if cleaned.startswith("```json"):
                    cleaned = cleaned[7:]
                elif cleaned.startswith("```"):
                    cleaned = cleaned[3:]
                if cleaned.endswith("```"):
                    cleaned = cleaned[:-3]
                cleaned = cleaned.strip()

                data = json.loads(cleaned)
                return CvAnalysisResponse(
                    overall_score=int(data.get("overall_score", 75)),
                    format_score=int(data.get("format_score", 75)),
                    content_score=int(data.get("content_score", 75)),
                    strengths=data.get("strengths", []),
                    improvements=data.get("improvements", []),
                    actionable_advice=data.get("actionable_advice", "")
                )
        except Exception as e:
            logger.warning(f"Lỗi khi gọi Gemini thẩm định CV: {e}. Kích hoạt thuật toán đánh giá dự phòng.")

    # Fallback dự phòng nếu không có API key hoặc lỗi mạng
    skills_found = [s for s in COMMON_IT_SKILLS if re.search(r'\b' + re.escape(s) + r'\b', raw_text, re.IGNORECASE)]
    has_email = bool(re.search(r'[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+', raw_text))
    has_github = "github" in raw_text.lower()
    
    score = 65
    strengths = []
    improvements = []
    
    if len(skills_found) >= 5:
        score += 20
        strengths.append(f"Hồ sơ liệt kê được {len(skills_found)} kỹ năng công nghệ ({', '.join(skills_found[:5])}...)")
    else:
        improvements.append("Số lượng kỹ năng công nghệ còn khá khiêm tốn, nên bổ sung thêm các framework và công cụ đã học")

    if has_github:
        score += 10
        strengths.append("Có liên kết GitHub/kho mã nguồn chứng minh sản phẩm thực tế")
    else:
        improvements.append("Nên bổ sung liên kết GitHub hoặc sản phẩm thực tế để nhà tuyển dụng dễ dàng thẩm định mã nguồn")

    if has_email:
        strengths.append("Thông tin liên hệ email đầy đủ, chuyên nghiệp")
    else:
        improvements.append("Cần bổ sung thông tin liên hệ rõ ràng ở phần đầu CV")

    return CvAnalysisResponse(
        overall_score=min(100, score),
        format_score=80,
        content_score=score,
        strengths=strengths,
        improvements=improvements,
        actionable_advice="Hãy tối ưu hóa phần mô tả đồ án theo công thức STAR (Situation, Task, Action, Result) và cập nhật thêm các kỹ năng chuyên sâu để nâng cao điểm số CV."
    )
