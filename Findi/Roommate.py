from flask import Flask, request, jsonify
from flask_cors import CORS
import numpy as np

app = Flask(__name__)
CORS(app, supports_credentials=True, origins=[
    "http://localhost:5174",
    "http://localhost:5173",
    "http://localhost:8080",
    "http://192.168.0.18:8080"
])

# ✅ 코사인 유사도 (생활 패턴)
def weighted_cosine_similarity(vec1, vec2, weights):
    vec1, vec2, weights = np.array(vec1), np.array(vec2), np.array(weights)
    weighted_vec1 = vec1 * weights
    weighted_vec2 = vec2 * weights
    dot_product = np.dot(weighted_vec1, weighted_vec2)
    norm_a = np.linalg.norm(weighted_vec1)
    norm_b = np.linalg.norm(weighted_vec2)
    return dot_product / (norm_a * norm_b) if norm_a * norm_b != 0 else 0

# ✅ MBTI 유사도 (같은 글자 수 비율)
def mbti_similarity(mbti1, mbti2):
    return sum(a == b for a, b in zip(mbti1.upper(), mbti2.upper())) / 4

# ✅ 추천 알고리즘
def recommend_roommates(user_vector, user_gender, user_mbti, user_isSmoking, weights, top_n=3):
    similarities = []

    for user, data in users_data.items():
        if data['gender'] != user_gender or data['is_Smoking'] != user_isSmoking:
            continue

        # 유사도 계산
        mbti_sim = mbti_similarity(user_mbti, data['mbti'])
        life_pattern_sim = weighted_cosine_similarity(user_vector, [data["life_pattern"]], weights)

        # 최종 유사도 = MBTI 60% + 생활 패턴 40%
        final_score = 0.6 * mbti_sim + 0.4 * life_pattern_sim
        similarities.append((user, final_score))

    similarities.sort(key=lambda x: x[1], reverse=True)
    return similarities[:top_n]

# ✅ 가짜 사용자 DB
users_data = {
    "A": {"gender": 1, "name": "Alice", "birth_year": 2000, "student_id": "2021001", "major": "CS", "mbti": "INTJ", "life_pattern": 1, "is_Smoking": 0},
    "B": {"gender": 1, "name": "Bob", "birth_year": 1999, "student_id": "2021002", "major": "Math", "mbti": "INTJ", "life_pattern": 1, "is_Smoking": 1},
    "C": {"gender": 0, "name": "Charlie", "birth_year": 2001, "student_id": "2021003", "major": "Physics", "mbti": "ENTP", "life_pattern": 0, "is_Smoking": 0},
    "D": {"gender": 1, "name": "David", "birth_year": 1998, "student_id": "2021004", "major": "CS", "mbti": "INTJ", "life_pattern": 0, "is_Smoking": 1},
    "E": {"gender": 0, "name": "Eve", "birth_year": 2002, "student_id": "2021005", "major": "Bio", "mbti": "ENTP", "life_pattern": 0, "is_Smoking": 1},
    "F": {"gender": 1, "name": "Frank", "birth_year": 2000, "student_id": "2021006", "major": "CS", "mbti": "ISTP", "life_pattern": 1, "is_Smoking": 0},
    "G": {"gender": 0, "name": "Holly", "birth_year": 2003, "student_id": "2021007", "major": "CS", "mbti": "ISFJ", "life_pattern": 1, "is_Smoking": 1},
    "H": {"gender": 0, "name": "Bolly", "birth_year": 2003, "student_id": "20220001", "major": "CS", "mbti": "ISTJ", "life_pattern": 1, "is_Smoking": 1}
}

# ✅ 매칭 API
@app.route('/handleMatch', methods=['POST'])
def match():
    try:
        data = request.get_json()

        # 입력값 파싱
        name = data.get('name')
        gender = int(data.get('gender'))
        mbti = data.get('mbti').upper()
        is_Smoking = int(data.get('is_Smoking'))
        life_pattern = int(data.get('life_pattern'))

        user_vector = [life_pattern]
        weights = [1.0]

        results = recommend_roommates(user_vector, gender, mbti, is_Smoking, weights, top_n=3)

        recommended = []
        for user_id, score in results:
            user = users_data[user_id]
            recommended.append({
                "name": user["name"],
                "student_id": user["student_id"],
                "major": user["major"],
                "mbti": user["mbti"],
                "score": round(score, 4)
            })

        return jsonify({
            "status": "success",
            "recommended": recommended
        })
    except Exception as e:
        return jsonify({
            "status": "error",
            "message": str(e)
        }), 500

# ✅ 실행
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
