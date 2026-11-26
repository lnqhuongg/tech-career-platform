import logging
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from schemas import (
    ResumeParseRequest, ResumeParsedResponse,
    CvAnalysisRequest, CvAnalysisResponse,
    JobMatchRequest, JobMatchResponse,
    ChatRequest, ChatResponse
)
from parser import parse_resume, analyze_cv_quality
from matcher import evaluate_job_match, get_embedding_model
from chatbot import chat_career_advisor
import config

# Cấu hình logging
logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(name)s: %(message)s")
logger = logging.getLogger("ai-service")

app = FastAPI(
    title="Tech Career Platform - AI Service",
    description="Microservice cung cấp các năng lực Trí tuệ Nhân tạo: Trích xuất CV (LLM NER), Thẩm định chất lượng CV, So khớp việc làm (Sentence-BERT), Phân tích Skill Gap và Chatbot Hướng nghiệp.",
    version="1.0.0"
)

# Cấu hình CORS để frontend và các microservice khác gọi thuận tiện
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.on_event("startup")
def on_startup():
    logger.info("Khởi động ai-service...")
    logger.info(f"Cấu hình Gemini Model: {config.GEMINI_MODEL}")
    logger.info(f"Cấu hình Sentence-BERT Model: {config.EMBEDDING_MODEL_NAME}")
    # Tải trước mô hình embedding vào RAM để các request sau phản hồi tức thì
    try:
        get_embedding_model()
    except Exception as e:
        logger.error(f"Lỗi khởi tạo Embedding Model lúc startup: {e}")

@app.get("/health")
def health_check():
    return {
        "status": "UP",
        "service": "ai-service",
        "gemini_enabled": bool(config.GEMINI_API_KEY),
        "gemini_model": config.GEMINI_MODEL,
        "embedding_model": config.EMBEDDING_MODEL_NAME
    }

@app.post("/ai/parse-resume", response_model=ResumeParsedResponse)
def api_parse_resume(req: ResumeParseRequest):
    """
    Endpoint bóc tách thông tin có cấu trúc từ văn bản thô của CV (LLM-based NER).
    """
    try:
        return parse_resume(req.raw_text)
    except Exception as e:
        logger.error(f"Lỗi xử lý parse-resume: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=f"Lỗi bóc tách CV: {str(e)}")

@app.post("/ai/analyze-cv", response_model=CvAnalysisResponse)
def api_analyze_cv(req: CvAnalysisRequest):
    """
    Endpoint thẩm định chất lượng CV chuyên sâu bằng AI (UC13):
    Chấm điểm bố cục, nội dung, chỉ ra điểm mạnh, điểm cần cải thiện và lời khuyên hành động.
    """
    try:
        return analyze_cv_quality(req.raw_text, req.target_role or "")
    except Exception as e:
        logger.error(f"Lỗi phân tích chất lượng CV: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=f"Lỗi thẩm định CV: {str(e)}")

@app.post("/ai/match-job", response_model=JobMatchResponse)
def api_match_job(req: JobMatchRequest):
    """
    Endpoint so khớp độ tương đồng ngữ nghĩa giữa CV và JD (Sentence-BERT + Cosine Similarity)
    và phân tích lỗ hổng kỹ năng (Skill Gap Analysis).
    """
    try:
        return evaluate_job_match(req)
    except Exception as e:
        logger.error(f"Lỗi xử lý match-job: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=f"Lỗi so khớp việc làm: {str(e)}")

@app.post("/ai/chat", response_model=ChatResponse)
def api_chat(req: ChatRequest):
    """
    Endpoint Trợ lý AI Tư vấn Hướng nghiệp (Prompt Engineering + Context Injection).
    """
    try:
        return chat_career_advisor(req)
    except Exception as e:
        logger.error(f"Lỗi xử lý chatbot: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=f"Lỗi tư vấn hướng nghiệp: {str(e)}")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host=config.HOST, port=config.PORT, reload=True)
