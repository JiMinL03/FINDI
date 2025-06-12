#!/bin/bash
# 가상환경 활성화
source .venv/bin/activate

# chatbot 디렉토리로 이동
cd chatbot || exit

# 액션 서버 백그라운드 실행
nohup rasa run actions > actions.log 2>&1 &

# 메인 Rasa 서버 실행
rasa run --enable-api --cors "*" --debug
