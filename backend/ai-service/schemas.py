from typing import List, Optional
from pydantic import BaseModel, Field

# --- Resume Parsing Models ---
class EducationItem(BaseModel):
    institution: str = ""
    degree: str = ""
    major: str = ""
    start_year: str = ""
    end_year: str = ""

class ExperienceItem(BaseModel):
    company: str = ""
    position: str = ""
    duration: str = ""
    description: str = ""

class CertificationItem(BaseModel):
    name: str = ""
    issuer: str = ""
    year: str = ""

class ResumeParseRequest(BaseModel):
    raw_text: str = Field(..., description="Văn bản thô trích xuất từ file PDF của CV")

class ResumeParsedResponse(BaseModel):
    candidate_name: Optional[str] = None
    target_role: Optional[str] = None
    phone: Optional[str] = None
    email: Optional[str] = None
    summary: Optional[str] = None
    total_experience_months: int = 0
    extracted_skills: List[str] = []
    educations: List[EducationItem] = []
    experiences: List[ExperienceItem] = []
    certifications: List[CertificationItem] = []

# --- AI CV Analysis Models (UC13) ---
class CvAnalysisRequest(BaseModel):
    raw_text: str = Field(..., description="Văn bản thô của CV cần thẩm định")
    target_role: Optional[str] = None

class CvAnalysisResponse(BaseModel):
    overall_score: int = Field(..., description="Điểm đánh giá chất lượng tổng thể thang 100")
    format_score: int = Field(..., description="Điểm bố cục và hình thức trình bày (0-100)")
    content_score: int = Field(..., description="Điểm nội dung dự án và kỹ năng kỹ thuật (0-100)")
    strengths: List[str] = Field(default=[], description="Các điểm sáng, thế mạnh nổi bật của CV")
    improvements: List[str] = Field(default=[], description="Các điểm thiếu sót, mơ hồ cần cải thiện")
    actionable_advice: str = Field(default="", description="Lời khuyên và hướng dẫn hành động cụ thể để tối ưu CV")

# --- Job Matching & Skill Gap Models ---
class JobMatchRequest(BaseModel):
    resume_id: Optional[str] = None
    resume_text: Optional[str] = ""
    candidate_skills: List[str] = []
    job_id: Optional[str] = None
    job_title: Optional[str] = ""
    job_description: Optional[str] = ""
    required_skills: List[str] = []

class JobMatchResponse(BaseModel):
    match_score: float = Field(..., description="Điểm phần trăm phù hợp tổng hợp (0-100)")
    semantic_score: float = Field(..., description="Điểm tương đồng ngữ nghĩa từ Sentence-BERT (0-100)")
    skill_score: float = Field(..., description="Điểm tỷ lệ trùng khớp kỹ năng (0-100)")
    matched_skills: List[str] = []
    missing_skills: List[str] = []
    ai_suggestions: str = Field(..., description="Nhận xét và gợi ý cải thiện kỹ năng cho ứng viên")

# --- Career Chatbot Models ---
class ChatMessage(BaseModel):
    role: str = Field(..., description="'user' hoặc 'model'/'assistant'")
    content: str

class ChatRequest(BaseModel):
    message: str = Field(..., description="Câu hỏi hoặc yêu cầu tư vấn của người dùng")
    history: List[ChatMessage] = []
    resume_context: Optional[str] = None
    job_context: Optional[str] = None

class ChatResponse(BaseModel):
    reply: str
