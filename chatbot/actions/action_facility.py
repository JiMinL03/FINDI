import json
import os
import logging
from typing import Any, Text, Dict, List

from rasa_sdk import Tracker
from rasa_sdk.executor import CollectingDispatcher

from utils.base_action import BaseAction
from utils.fuzzy_matcher import *
from utils.json_loader import load_json
from utils.logging_utils import get_logger  # 이렇게 가져온다고 가정

logger = get_logger(__name__)


class ActionFacilityInfo(BaseAction):
    def name(self) -> Text:
        return "action_facility_info"

    slots_to_reset = ["building_name"]

    def run(self, dispatcher, tracker, domain) -> List[Dict[Text, Any]]:
        try:
            user_input = tracker.get_slot("building_name") or tracker.latest_message.get("text")
            logger.info(f"[INPUT] user_input: {user_input}")

            if not user_input:
                logger.warning("[WARN] 건물명 입력 없음")
                return self.fail(dispatcher, "건물명을 입력해 주세요.")

            # 전처리 및 매칭
            processed_input = preprocess_text(user_input)
            building_data = load_json("building.json")
            building_names = [item["BUILDING_NAME"] for item in building_data]

            if processed_input not in building_names:
                return self.fail(dispatcher, f"'{user_input}'과 일치하는 건물을 찾을 수 없습니다.")

            matched_name = processed_input
            building_info = next((item for item in building_data if item["BUILDING_NAME"] == matched_name), None)

            if not building_info:
                return self.fail(dispatcher, f"{matched_name}의 정보를 찾을 수 없습니다.")

            building_id = building_info["BUILDING_ID"]
            campus_map_data = load_json("campus_map.json")
            campus_info = next((item for item in campus_map_data if item["BUILDING_ID"] == building_id), None)

            if not campus_info:
                return self.fail(dispatcher, f"{matched_name}의 층별 정보를 찾을 수 없습니다.")

            # 층 정보: 정렬된 리스트
            ordered_keys = [
                "ONE_LAYER", "TWO_LAYER", "THREE_LAYER", "FOUR_LAYER",
                "FIVE_LAYER", "SIX_LAYER", "SEVEN_LAYER", "EIGHT_LAYER", "NINE_LAYER"
            ]
            layers = []
            for key in ordered_keys:
                if key in campus_info and campus_info[key]:
                    floor_name = layer_key_to_floor(key)
                    layers.append(f"{floor_name}: {campus_info[key]}")

            etc_info = campus_info.get("ETC", "")
            content_info = campus_info.get("CONTENT", "")

            # 7. 메시지 조립
            dispatcher.utter_message(text=f"🏢 {matched_name} 정보입니다:")

            if content_info:
                dispatcher.utter_message(text=f"📌 주요 기관: {content_info}")

            if layers:
                dispatcher.utter_message(text="🗂️ 층별 구성:\n" + "\n".join(layers))

            if etc_info:
                dispatcher.utter_message(text=f"🛠️ 기타 정보: {etc_info}")

            logger.info("[SUCCESS] 정보 전달 완료, 슬롯 초기화")
            return self.reset_slots()

        except Exception as e:
            logger.error(f"[EXCEPTION] 오류 발생: {str(e)}", exc_info=True)
            return self.fail(dispatcher, f"오류가 발생했습니다: {str(e)}")


def layer_key_to_floor(layer_key: str) -> str:
    mapping = {
        "ONE_LAYER": "1층",
        "TWO_LAYER": "2층",
        "THREE_LAYER": "3층",
        "FOUR_LAYER": "4층",
        "FIVE_LAYER": "5층",
        "SIX_LAYER": "6층",
        "SEVEN_LAYER": "7층",
        "EIGHT_LAYER": "8층",
        "NINE_LAYER": "9층",
    }
    return mapping.get(layer_key, layer_key.replace("_LAYER", "층"))
