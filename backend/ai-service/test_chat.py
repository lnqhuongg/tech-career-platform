from schemas import ChatRequest
from chatbot import chat_career_advisor

req = ChatRequest(message="Em là sinh viên sắp tốt nghiệp, em nên chuẩn bị những kiến thức gì để phỏng vấn vị trí Java Backend Fresher?")
res = chat_career_advisor(req)
print("=== CHATBOT RESPONSE ===")
print(res.reply)
