from rapidfuzz import fuzz

PREPROCESS_DICT = {
    "동의병원": "동의의료원",
    "동의 병원": "동의의료원",
    "병원": "동의의료원",

    "상영관": "상영관(제2학생회관)",
    "제2학생회관": "상영관(제2학생회관)",

    "효민관": "효민체육관",
    "체육관": "효민체육관",
    "체육": "효민체육관",

    "도서관": "중앙도서관",
    "중도": "중앙도서관",

    "본관": "대학본관",
    "대학 본관": "대학본관",
    "대본": "대학본관",

    "법관": "법정관",
    "법학관": "법정관",

    "상경": "상경관",
    "상경관건물": "상경관",

    "국제": "국제관",
    "국제건물": "국제관",

    "공학": "공학관",
    "공학건물": "공학관",

    "정보관": "정보공학관",

    "산학관": "산학협력관",

    "음악": "음악관",
    "음악건물": "음악관",

    "창의": "창의관",

    "스포츠센터": "동의스포츠센터",
    "체력단련장": "동의스포츠센터",

    "여대생관": "여대생커리어개발관",
    "여커관": "여대생커리어개발관",

    "인문1": "제1인문관",
    "인문2": "제2인문관",
    "1인": "제1인문관",
    "2인": "제2인문관",

    "생활과학": "생활과학관",
    "생과관": "생활과학관",

    "효민1": "제1효민생활관",
    "효민2": "제2효민생활관",
    "효민1긱": "제1효민생활관",
    "효민2긱": "제2효민생활관",
    "효민1기숙사": "제1효민생활관",
    "효민2기숙사": "제2효민생활관",

    "미래생활관": "행복기숙사(미래생활관)",
    "행복기숙사": "행복기숙사(미래생활관)",
    "행긱": "행복기숙사(미래생활관)",

    "군사교육단": "학생군사교육단"
}

def preprocess_text(text: str) -> str:
    text = text.replace(" ", "")  # 공백 제거
    return PREPROCESS_DICT.get(text, text)  # 딕셔너리에서 변환, 없으면 원본 반환

def hybrid_match(input_text: str, candidates: list[str], threshold=75) -> str | None:
    # 1. 딕셔너리 기반 전처리 + 정확 매칭 시도
    processed = preprocess_text(input_text)
    if processed in candidates:
        return processed

    # 2. fuzzy matching
    from rapidfuzz import process
    match = process.extractOne(processed, candidates, scorer=fuzz.ratio)
    if match and match[1] >= threshold:
        return match[0]

    return None