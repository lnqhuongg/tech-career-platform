@echo off
chcp 65001 > nul
echo =======================================================
echo   TECH CAREER PLATFORM - AI SERVICE (PORT 8000)
echo   Sentence-BERT Semantic Matching & LLM Resume Parser
echo =======================================================
cd /d "%~dp0"
python -m uvicorn main:app --host 0.0.0.0 --port 8000 --reload
pause
