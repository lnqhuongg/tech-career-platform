import logging
from typing import List
from schemas import ChatRequest, ChatResponse, ChatMessage
import config

logger = logging.getLogger(__name__)

SYSTEM_PROMPT = """
Bạn là Trợ lý AI Tư vấn Hướng nghiệp (Career Advisor AI) chuyên sâu về lĩnh vực Công nghệ Thông tin (IT) tại Việt Nam, thuộc hệ thống Tech Career Platform.

Vai trò & Phong cách của bạn:
1. Thân thiện, chuyên nghiệp, động viên và đưa ra các lời khuyên thực tế, cụ thể cho sinh viên và lập trình viên.
2. Giúp ứng viên:
   - Tối ưu hóa CV/Resume chuẩn ATS.
   - Định hướng lộ trình học tập, nâng cao kỹ năng (Skill Gap).
   - Luyện tập phỏng vấn kỹ thuật (Coding, System Design, Behavioral).
   - Đánh giá mức độ phù hợp với các tin tuyển dụng.
3. Nếu có thông tin ngữ cảnh về CV hoặc Job Description của ứng viên, hãy tận dụng tối đa để cá nhân hóa câu trả lời.
4. Trả lời bằng tiếng Việt gãy gọn, có cấu trúc bullet point rõ ràng khi cần thiết.

5. NGUYÊN TẮC KIỂM SOÁT PHẠM VI (GUARDRAIL - BẮT BUỘC):
   - Bạn CHỈ tư vấn các câu hỏi liên quan đến: Công nghệ thông tin, lập trình, nghề nghiệp IT, kinh nghiệm dự án, kỹ năng mềm trong công việc, đánh giá CV và chuẩn bị phỏng vấn.
   - Nếu người dùng hỏi các câu hỏi hoàn toàn ngoài luồng (ví dụ: hỏi công thức nấu ăn, chiêm tinh, thời tiết, giải toán phổ thông, tin tức giải trí, bóng đá, v.v.), bạn hãy LỊCH SỰ TỪ CHỐI và nhắc khéo:
     "Xin lỗi bạn, tôi là Trợ lý AI chuyên trách Tư vấn Hướng nghiệp CNTT nên chỉ có thể hỗ trợ các chủ đề liên quan đến nghề nghiệp, kỹ năng lập trình, CV và tuyển dụng IT. Bạn có thắc mắc nào về định hướng học tập hay việc làm ngành công nghệ không ạ?"
"""

def chat_career_advisor(req: ChatRequest) -> ChatResponse:
    if config.GEMINI_API_KEY:
        try:
            from google import genai
            from google.genai import types

            client = genai.Client(api_key=config.GEMINI_API_KEY)

            # Xây dựng nội dung ngữ cảnh
            context_parts = [SYSTEM_PROMPT]
            if req.resume_context:
                context_parts.append(f"\n[Thông tin hồ sơ ứng viên]:\n{req.resume_context}")
            if req.job_context:
                context_parts.append(f"\n[Thông tin việc làm quan tâm]:\n{req.job_context}")

            # Đính kèm lịch sử hội thoại gần nhất
            conversation_history = "\n".join([f"{msg.role.upper()}: {msg.content}" for msg in req.history[-6:]])
            if conversation_history:
                context_parts.append(f"\n[Lịch sử trao đổi]:\n{conversation_history}")

            context_parts.append(f"\nUSER: {req.message}\nASSISTANT:")
            full_prompt = "\n".join(context_parts)

            resp = client.models.generate_content(
                model=config.GEMINI_MODEL,
                contents=full_prompt,
                config=types.GenerateContentConfig(
                    temperature=0.7,
                    max_output_tokens=4096,
                    thinking_config=types.ThinkingConfig(thinking_budget=0)
                )
            )

            if resp and resp.text:
                return ChatResponse(reply=resp.text.strip())
        except Exception as e:
            logger.warning(f"Lỗi khi gọi Gemini Chatbot: {e}")

    # Fallback phản hồi khi chưa có API Key hoặc mạng gián đoạn
    return ChatResponse(
        reply=(
            f"Chào bạn! Tôi là Trợ lý AI Tư vấn Hướng nghiệp CNTT.\n\n"
            f"Hiện tại hệ thống đã ghi nhận câu hỏi của bạn: \"{req.message}\".\n\n"
            f"📌 Lời khuyên nhanh cho bạn:\n"
            f"- Hãy đảm bảo CV của bạn có đầy đủ các kỹ năng cốt lõi (Java, Spring Boot, React, Docker...).\n"
            f"- Tận dụng chức năng 'AI Job Match' trên hệ thống để đo lường độ phù hợp và tìm ra các kỹ năng còn thiếu (Skill Gap).\n"
            f"- Chuẩn bị kỹ các câu hỏi về cấu trúc dữ liệu, giải thuật và dự án thực chiến mà bạn đã xây dựng."
        )
    )
