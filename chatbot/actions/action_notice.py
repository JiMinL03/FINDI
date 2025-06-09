import logging
from typing import Any, Text, Dict, List

from rasa_sdk import Tracker
from rasa_sdk.executor import CollectingDispatcher
from rasa_sdk.events import SlotSet

from utils.base_action import BaseAction
from utils.json_loader import load_json
from utils.logging_utils import get_logger

logger = get_logger(__name__)

class ActionNoticeAll(BaseAction):
    def name(self) -> Text:
        return "action_notice"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        notice_type = tracker.get_slot("notice_type")
        logger.debug(f"notice : {notice_type}")

        if not notice_type:
            return self.fail(dispatcher, "잘못된 입력입니다.")

        all_notices = load_json("INFO_SQUARE.json")

        logger.debug(f"load_data = {all_notices}")

        filtered = [
            n for n in all_notices
            if n.get("CATEGORY", "").lower() == notice_type.lower()
        ]

        if not filtered:
            dispatcher.utter_message(text=f"'{notice_type}' 카테고리에 해당하는 공지가 없습니다.\u2063__END__")
            return []

        # 프론트에 전체 데이터를 한 번에 전달
        json_response = {
            "notice": [
                {
                    "title": n.get("TITLE", "제목 없음"),
                    "author": n.get("WRITER", "알 수 없음"),
                    "link": n.get("CONTENT", "#")
                }
                for n in filtered
            ],
        }

        dispatcher.utter_message(text="공지사항 리스트입니다.", json_message=json_response)
        logger.debug(f"전체 공지 전달 : {json_response}")
        return []
