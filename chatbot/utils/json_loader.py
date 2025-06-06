import os
import json
from typing import Any, Dict, List

def load_json(file_name: str) -> List[Dict[str, Any]]:
    base_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))  # rasa_project/ 기준
    file_path = os.path.join(base_dir, "data", "json", file_name)

    with open(file_path, "r", encoding="utf-8") as f:
        return json.load(f)
