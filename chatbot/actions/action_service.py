import json
import logging
from typing import Any, Text, Dict, List

from rasa_sdk import Tracker
from rasa_sdk.executor import CollectingDispatcher

from utils.base_action import BaseAction
from utils.json_loader import load_json
from utils.logging_utils import get_logger

logger = get_logger(__name__)

class ActionUserServicesFullList(BaseAction):
    def name(self) -> Text:
        return "action_user_services"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        try:
            services_data = load_json("user_link.json")
            if not services_data:
                return self.fail(dispatcher, "잘못된 입력입니다.")

            # 키 이름 변환
            formatted_services = [
                {"name": s["SERVICE_NAME"], "link": s["SERVICE_LINK"]}
                for s in services_data
            ]

            dispatcher.utter_message(json_message={
                "services": formatted_services,
                "show_controls": True
            })
            return []

        except Exception as e:
            dispatcher.utter_message(text="서비스 정보를 불러오는 중 오류가 발생했습니다.")
            return []
