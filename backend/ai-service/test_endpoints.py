from fastapi.testclient import TestClient
from main import app

client = TestClient(app)

print("--- 1. Test /health ---")
res = client.get("/health")
print("Status:", res.status_code, "Body:", res.json())
assert res.status_code == 200

print("\n--- 2. Test /ai/match-job (Sentence-BERT + Skill Gap) ---")
res = client.post("/ai/match-job", json={
    "resume_text": "Java Backend Developer with Spring Boot and PostgreSQL experience",
    "candidate_skills": ["Java", "Spring Boot", "PostgreSQL"],
    "job_title": "Junior Java Developer",
    "job_description": "Looking for Java backend developer with Spring Boot and Kafka",
    "required_skills": ["Java", "Spring Boot", "Kafka"]
})
print("Status:", res.status_code)
print("Response:", res.json())
assert res.status_code == 200
data = res.json()
assert "match_score" in data
assert "matched_skills" in data
assert "missing_skills" in data

print("\n--- 3. Test /ai/parse-resume ---")
res = client.post("/ai/parse-resume", json={
    "raw_text": "Nguyen Van B\nJava Backend Engineer\nEmail: b@gmail.com\nPhone: 0987654321\nSkills: Java, Docker, Git"
})
print("Status:", res.status_code)
print("Response:", res.json())
assert res.status_code == 200

print("\n--- 4. Test /ai/chat ---")
res = client.post("/ai/chat", json={
    "message": "Làm thế nào để chuẩn bị phỏng vấn Java Backend?"
})
print("Status:", res.status_code)
print("Reply:\n", res.json().get("reply"))
assert res.status_code == 200

print("\n==========================================")
print(">>> ALL FASTAPI ENDPOINTS VERIFIED 100%! <<<")
print("==========================================")
