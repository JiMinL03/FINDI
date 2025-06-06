from typing import Any, Text, Dict, List
from rasa_sdk import Tracker
from rasa_sdk.executor import CollectingDispatcher

from utils.base_action import BaseAction
from utils.json_loader import load_json
from utils.logging_utils import get_logger

logger = get_logger(__name__)


class ActionFacilityInfo(BaseAction):
    def name(self) -> Text:
        return "action_facility_info"

    slots_to_reset = ["building_name"]

    def run(self, dispatcher, tracker, domain) -> List[Dict[Text, Any]]:
        try:
            building_name = tracker.get_slot("building_name")
            logger.info(f"[INPUT] building_name slot: {building_name}")

            if not building_name:
                logger.warning("[WARN] building_name 슬롯 비어 있음")
                return self.fail(dispatcher, "건물명을 선택해 주세요.")

            # building.json 에서 건물 정보 검색
            building_data = load_json("building.json")
            building_info = next(
                (item for item in building_data if item["BUILDING_NAME"] == building_name), None
            )

            if not building_info:
                return self.fail(dispatcher, f"'{building_name}'에 대한 정보를 찾을 수 없습니다.")

            building_id = building_info["BUILDING_ID"]

            # campus_map.json 에서 층별 정보 가져오기
            campus_map_data = load_json("campus_map.json")
            campus_info = next(
                (item for item in campus_map_data if item["BUILDING_ID"] == building_id), None
            )

            if not campus_info:
                return self.fail(dispatcher, f"{building_name}의 층별 정보를 찾을 수 없습니다.")

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

            # 메시지 조립
            dispatcher.utter_message(text=f"🏢 {building_name} 정보입니다:")

            if content_info:
                dispatcher.utter_message(text=f"📌 주요 기관: {content_info}")

            if layers:
                dispatcher.utter_message(text="🗂️ 층별 구성:\n" + "\n".join(layers))

            if etc_info:
                dispatcher.utter_message(text=f"🛠️ 기타 정보: {etc_info}")

            logger.info("[SUCCESS] 건물 정보 응답 완료, 슬롯 초기화")
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
