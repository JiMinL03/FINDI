@echo off
REM chatbot 폴더로 이동
cd chatbot

REM 가상환경 활성화
call .venv\Scripts\activate.bat

REM 액션 서버를 새 터미널 창에서 실행
start cmd /k "rasa run actions"

REM 메인 Rasa 서버 실행
rasa run --enable-api --cors "*" --debug
