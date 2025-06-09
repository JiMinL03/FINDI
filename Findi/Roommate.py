from flask import Flask, request, jsonify
from flask_cors import CORS
import numpy as np
import requests  # ✅ 필수

app = Flask(__name__)
CORS(app, supports_credentials=True, origins=[
    "http://localhost:5174",
    "http://localhost:5173",
    "http://localhost:8080",
    "http://192.168.0.18:8080"
])

def weighted_cosine_similarity(vec1, vec2, weights):
    vec1, vec2, weights = np.array(vec1), np.array(vec2), np.array(weights)
    weighted_vec1 = vec1 * weights
    weighted_vec2 = vec2 * weights
    dot_product = np.dot(weighted_vec1, weighted_vec2)
    norm_a = np.linalg.norm(weighted_vec1)
    norm_b = np.linalg.norm(weighted_vec2)
    return dot_product / (norm_a * norm_b) if norm_a * norm_b != 0 else 0

def mbti_similarity(mbti1, mbti2):
    return sum(a == b for a, b in zip(mbti1.upper(), mbti2.upper())) / 4

@app.route("/handleMatch", methods=["POST"])
def match():
    try:
        user = request.get_json()
        gender = int(user["gender"])
        mbti = user["mbti"]
        life = int(user["life_pattern"])
        smoke = int(user["is_Smoking"])
        weights = [1.0]
        user_vec = [life]

        # ✅ Spring 서버에서 사용자 데이터 가져오기
        res = requests.get("http://localhost:8080/api/roommate/all")
        all_users = res.json()

        results = []
        for u in all_users:
            if int(u["gender"]) != gender or int(u["is_Smoking"]) != smoke:
                continue
            mbti_sim = mbti_similarity(mbti, u["mbti"])
            life_sim = weighted_cosine_similarity(user_vec, [int(u["life_pattern"])], weights)
            score = 0.6 * mbti_sim + 0.4 * life_sim
            results.append((u, score))

        results.sort(key=lambda x: x[1], reverse=True)
        top_matches = [
            {
                "name": u["name"],
                "student_id": u["student_id"],
                "major": u["major"],
                "mbti": u["mbti"],
                "gender": u["gender"],
                "life_pattern": u["life_pattern"],
                "is_Smoking": u["is_Smoking"],
                "score": round(score, 4),
            }
            for u, score in results[:3]
        ]

        return jsonify({"status": "success", "recommended": top_matches})
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

@app.route("/send_email", methods=['POST'])
def send_email():
    print("send_email 함수 호출됨")
    data = request.get_json()
    print("받은 JSON 데이터:", data)

    to_name = data.get('to_name')

    if not to_name:
        return jsonify({"status": "fail", "message": "'to_name' 값이 없습니다."}), 400

    try:
        response = requests.post(
            "http://localhost:8080/api/sendEmail",
            json={
                "toName": to_name,
            }
        )
        print("응답 상태 코드:", response.status_code)
        print("응답 텍스트:", response.text)
        response.raise_for_status()
        print("스프링부트 응답 텍스트:", response.text)
        print("스프링부트 응답:", response.json())  # 디버깅용 출력
        return jsonify(response.json())

    except requests.exceptions.RequestException as e:
        print("스프링부트 요청 실패:", str(e))
        return jsonify({"status": "fail", "message": "스프링부트 요청 실패: " + str(e)}), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
