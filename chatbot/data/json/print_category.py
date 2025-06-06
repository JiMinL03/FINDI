import json

def extract_unique_categories(json_path: str):
    try:
        with open(json_path, 'r', encoding='utf-8') as f:
            data = json.load(f)

        categories = {item["CATEGORY"] for item in data if "CATEGORY" in item and item["CATEGORY"]}
        return sorted(categories)

    except Exception as e:
        print(f"오류 발생: {e}")
        return []

# 사용 예시
if __name__ == "__main__":
    category_list = extract_unique_categories("INFO_SQUARE.json")
    print("✅ 전체 카테고리 목록:")
    for c in category_list:
        print(f"- {c}")
