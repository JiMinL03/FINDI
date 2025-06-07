import json
import logging
from typing import Any, Text, Dict, List

from rasa_sdk import Tracker
from rasa_sdk.executor import CollectingDispatcher

from utils.base_action import BaseAction
from utils.json_loader import load_json
from utils.logging_utils import get_logger

logger = get_logger(__name__)

class ActionUserServices(BaseAction):
    def name(self) -> Text:
        return "action_user_services"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        try:
            logger.info("사용자 서비스 정보 요청 처리 시작")

            # services.json 파일 로드
            services_data = load_json("user_link.json")

            if not services_data:
                logger.warning("서비스 데이터가 없습니다.")
                dispatcher.utter_message(text="현재 제공 가능한 서비스 정보가 없습니다.")
                return []

            # 메시지 구성
            dispatcher.utter_message(text="🛠️ 아래는 주요 온라인 서비스 링크입니다:")

            for service in services_data:
                name = service.get("SERVICE_NAME")
                link = service.get("SERVICE_LINK")

                if name and link:
                    dispatcher.utter_message(text=f"🔗 [{name}]({link})")

            return []

        except Exception as e:
            logger.error(f"[EXCEPTION] 사용자 서비스 정보 처리 중 오류 발생: {str(e)}", exc_info=True)
            dispatcher.utter_message(text="서비스 정보를 불러오는 중 오류가 발생했습니다.")
            return []
