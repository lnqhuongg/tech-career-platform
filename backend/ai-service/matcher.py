import os
import logging
import numpy as np
from typing import List, Tuple
from sklearn.metrics.pairwise import cosine_similarity
import config
from sentence_transformers import SentenceTransformer
from schemas import JobMatchRequest, JobMatchResponse

logger = logging.getLogger(__name__)

# Singleton tải mô hình Sentence-BERT
_embedding_model = None

def get_embedding_model():
    global _embedding_model
    if _embedding_model is None:
        logger.info(f"Đang khởi tạo mô hình Sentence-BERT: {config.EMBEDDING_MODEL_NAME}...")
        _embedding_model = SentenceTransformer(config.EMBEDDING_MODEL_NAME)
        logger.info("Mô hình Sentence-BERT đã sẵn sàng!")
    return _embedding_model

# Bảng từ đồng nghĩa / quy chuẩn kỹ năng công nghệ
SKILL_ALIASES = {
    "reactjs": "react",
    "react.js": "react",
    "vuejs": "vue",
    "vue.js": "vue",
    "nodejs": "node.js",
    "node": "node.js",
    "js": "javascript",
    "ts": "typescript",
    "golang": "go",
    "postgres": "postgresql",
    "postgre": "postgresql",
    "k8s": "kubernetes",
    "amazon web services": "aws",
    "google cloud platform": "gcp",
    "dot net": ".net",
    "c sharp": "c#",
    "spring": "spring boot"
}

def normalize_skill(skill: str) -> str:
    cleaned = skill.strip().lower()
    return SKILL_ALIASES.get(cleaned, cleaned)

def evaluate_job_match(req: JobMatchRequest) -> JobMatchResponse:
    """
    So khớp CV và Job Description kết hợp:
    1. Nhúng ngữ nghĩa đa chiều bằng Sentence-BERT và đo độ tương đồng Cosine.
    2. Phân tích lỗ hổng kỹ năng (Skill Gap Analysis) qua đối sánh tập hợp.
    3. Sinh nhận xét và lộ trình khắc phục bằng LLM / Template.
    """
    model = get_embedding_model()

    # 1. Chuẩn hóa và sắp xếp kỹ năng theo thứ tự bảng chữ cái
    # Điều này đảm bảo chuỗi đầu vào luôn cố định 100%, loại bỏ hoàn toàn việc lệch điểm
    # (ví dụ: 67% vs 68%) do thứ tự phần tử ngẫu nhiên giữa các lần gửi.
    candidate_skills = sorted(list(set(req.candidate_skills or [])))
    required_skills = sorted(list(set(req.required_skills or [])))

    candidate_skills_str = ", ".join(candidate_skills)
    resume_repr = f"Skills: {candidate_skills_str}. {req.resume_text[:2000]}"

    required_skills_str = ", ".join(required_skills)
    job_repr = f"Position: {req.job_title}. Required Skills: {required_skills_str}. {req.job_description[:2000]}"

    # 2. Tính Semantic Similarity bằng Sentence-BERT
    resume_vec = model.encode([resume_repr])
    job_vec = model.encode([job_repr])
    cos_sim = float(cosine_similarity(resume_vec, job_vec)[0][0])
    # Quy chuẩn về thang điểm 0 - 100
    semantic_score = round(max(0.0, min(100.0, cos_sim * 100.0)), 2)

    # 3. Phân tích Skill Gap
    matched_skills = []
    missing_skills = []

    norm_candidate = {normalize_skill(s): s for s in candidate_skills}

    for req_skill in required_skills:
        norm_req = normalize_skill(req_skill)
        # Khớp chính xác hoặc khớp qua từ đồng nghĩa
        if norm_req in norm_candidate:
            matched_skills.append(req_skill)
        else:
            # Khớp chuỗi con (VD: "Spring" trong "Spring Boot")
            matched = False
            for c_norm in norm_candidate.keys():
                if norm_req in c_norm or c_norm in norm_req:
                    matched = True
                    matched_skills.append(req_skill)
                    break
            if not matched:
                missing_skills.append(req_skill)

    # 4. Tính điểm kỹ năng và điểm tổng hợp
    if not required_skills:
        skill_score = 100.0
    else:
        skill_score = round((len(matched_skills) / len(required_skills)) * 100.0, 2)

    # Điểm tổng hợp: 50% Ngữ nghĩa ngữ cảnh (Sentence-BERT) + 50% Độ phủ kỹ năng cứng (Skill Set Overlap)
    match_score = round(0.5 * semantic_score + 0.5 * skill_score, 2)

    # 5. Sinh nhận xét và gợi ý AI
    suggestions = generate_ai_suggestions(
        job_title=req.job_title or "vị trí tuyển dụng",
        match_score=match_score,
        matched_skills=matched_skills,
        missing_skills=missing_skills
    )

    return JobMatchResponse(
        match_score=match_score,
        semantic_score=semantic_score,
        skill_score=skill_score,
        matched_skills=matched_skills,
        missing_skills=missing_skills,
        ai_suggestions=suggestions
    )

def generate_ai_suggestions(job_title: str, match_score: float, matched_skills: List[str], missing_skills: List[str]) -> str:
    # Thử gọi Gemini LLM để cá nhân hóa nhận xét
    if config.GEMINI_API_KEY:
        try:
            from google import genai
            from google.genai import types
            client = genai.Client(api_key=config.GEMINI_API_KEY)
            prompt = f"""
Bạn là chuyên gia tư vấn tuyển dụng CNTT. Hãy đưa ra 2-3 câu nhận xét súc tích, mang tính khích lệ và định hướng hành động cụ thể cho ứng viên:
- Vị trí ứng tuyển: {job_title}
- Điểm phù hợp tổng quan: {match_score}%
- Kỹ năng đã có: {', '.join(matched_skills) if matched_skills else 'Chưa phát hiện'}
- Kỹ năng còn thiếu: {', '.join(missing_skills) if missing_skills else 'Không có'}

Yêu cầu: Viết bằng tiếng Việt tự nhiên, trực diện, gợi ý khóa học/chủ đề cần học thêm nếu có kỹ năng thiếu.
"""
            resp = client.models.generate_content(
                model=config.GEMINI_MODEL,
                contents=prompt,
                config=types.GenerateContentConfig(
                    max_output_tokens=2048,
                    thinking_config=types.ThinkingConfig(thinking_budget=0),
                    temperature=0.0
                )
            )
            if resp and resp.text:
                return resp.text.strip()
        except Exception as e:
            logger.warning(f"Lỗi khi sinh gợi ý qua Gemini: {e}. Dùng template gợi ý.")

    # Fallback Template
    if not missing_skills:
        return f"Tuyệt vời! Hồ sơ của bạn đạt độ tương thích cao ({match_score}%) với {job_title}. Bạn đáp ứng đầy đủ tất cả kỹ năng cốt lõi được yêu cầu. Hãy tự tin ứng tuyển ngay!"
    
    missing_str = ", ".join(missing_skills)
    if match_score >= 70:
        return f"Hồ sơ của bạn phù hợp {match_score}% cho {job_title}. Bạn đã có nền tảng tốt ({', '.join(matched_skills)}). Để tối ưu cơ hội trúng tuyển, hãy bổ sung thêm các kỹ năng sau vào dự án thực tế: {missing_str}."
    else:
        return f"Độ tương thích hiện tại là {match_score}% cho {job_title}. Nhà tuyển dụng đang tìm kiếm ứng viên thành thạo: {missing_str}. Bạn nên tập trung ôn luyện và bổ sung các đồ án thực hành liên quan đến các công nghệ này trước khi ứng tuyển."
