from typing import Any, Text, Dict, List
from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher
from utils.json_loader import load_json
from utils.logging_utils import get_logger

logger = get_logger(__name__)

class ActionSchoolLocation(Action):
    def name(self) -> Text:
        return "action_school_location"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        logger.debug("ActionSchoolLocation 실행")
        data = load_json("DEU_INFO.json")
        logger.debug(f"로드된 데이터: {data}")

        if data and isinstance(data, list) and len(data) > 0:
            info = data[0]
            address = info.get("DEU_ADDRESS", "학교 주소 정보를 찾을 수 없습니다.")
            logger.debug(f"학교 주소: {address}")

        else:
            address = "학교 주소 정보를 찾을 수 없습니다."
            logger.warning("DEU_INFO.json 데이터가 없거나 비어있음")

        dispatcher.utter_message(text=f"학교 주소는 다음과 같습니다: {address}")
        return []


class ActionSchoolContact(Action):
    def name(self) -> Text:
        return "action_school_contact"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        logger.debug("ActionSchoolContact 실행")
        data = load_json("DEU_INFO.json")
        logger.debug(f"로드된 데이터: {data}")

        if data and isinstance(data, list) and len(data) > 0:
            info = data[0]
            phone = info.get("DEU_PHONE", "전화번호 정보를 찾을 수 없습니다.")
        else:
            phone = "전화번호 정보를 찾을 수 없습니다."
            logger.warning("DEU_INFO.json 데이터가 없거나 비어있음")

        dispatcher.utter_message(text=f"학교 대표 전화번호는 {phone} 입니다.")
        return []


class ActionSchoolTransport(Action):
    def name(self) -> Text:
        return "action_school_transport"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        logger.debug("ActionSchoolTransport 실행")
        data = load_json("BUS.json")
        logger.debug(f"로드된 데이터: {data}")

        if not data:
            dispatcher.utter_message(text="셔틀버스 정보를 불러오는 데 실패했습니다.")
            logger.warning("BUS.json 데이터가 없거나 비어있음")

            return []

        messages = []
        for bus_info in data:
            bus_name = bus_info.get("BUS_NAME", "정보 없음")
            route = bus_info.get("ROUTE", "정보 없음")
            dispatch_time = bus_info.get("DISPATCH_TIME", "정보 없음")
            first_bus = bus_info.get("FIRST_BUS", "정보 없음")
            last_bus = bus_info.get("LAST_BUS", "정보 없음")

            msg = (
                f"{bus_name}\n"
                f"운행 노선: {route}\n"
                f"배차 간격: {dispatch_time}\n"
                f"첫차: {first_bus}\n"
                f"막차: {last_bus}"
            )
            messages.append(msg)

        full_message = "\n\n".join(messages)
        dispatcher.utter_message(text=f"셔틀버스 운행 정보입니다:\n\n{full_message}")

        return []
