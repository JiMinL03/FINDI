import logging
from typing import Any, Text, Dict, List

from rasa_sdk import Tracker
from rasa_sdk.executor import CollectingDispatcher
from rasa_sdk.events import SlotSet

from utils.base_action import BaseAction
from utils.json_loader import load_json
from utils.logging_utils import get_logger

logger = get_logger(__name__)

PAGE_SIZE = 3  # 한 페이지당 공지 수

class ActionNoticeInfo(BaseAction):
    def name(self) -> Text:
        return "action_notice"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker, domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        try:
            notice_type = tracker.get_slot("notice_type")
            notice_page = tracker.get_slot("notice_page") or 1
            notice_page = int(notice_page)

            logger.info(f"[INPUT] notice_type: {notice_type}, page: {notice_page}")

            if not notice_type:
                dispatcher.utter_message(text="공지사항 카테고리를 알려주세요. 예: 기숙사, 장학, 수업 등")
                return []

            notice_data = load_json("INFO_SQUARE.json")

            # 카테고리 필터링 (대소문자 무시)
            filtered_notices = [
                notice for notice in notice_data
                if notice.get("CATEGORY", "").lower() == notice_type.lower()
            ]

            if not filtered_notices:
                dispatcher.utter_message(text=f"'{notice_type}' 카테고리에 해당하는 공지사항이 없습니다.")
                return [SlotSet("notice_page", 1)]

            # 페이징 처리
            start_idx = (notice_page - 1) * PAGE_SIZE
            end_idx = start_idx + PAGE_SIZE
            current_page_notices = filtered_notices[start_idx:end_idx]

            if not current_page_notices:
                dispatcher.utter_message(text="더 이상 불러올 공지사항이 없습니다.")
                return [SlotSet("notice_page", notice_page - 1)]

            if notice_page == 1:
                dispatcher.utter_message(text=f"📢 '{notice_type}' 관련 공지사항입니다:")

            for notice in current_page_notices:
                title = notice.get("TITLE", "제목 없음")
                writer = notice.get("WRITER", "작성자 미상")
                link = notice.get("CONTENT", "#")
                dispatcher.utter_message(text=f"📝 {title}\n작성자: {writer}\n🔗 [자세히 보기]({link})")

            # 다음 페이지 유도
            if end_idx < len(filtered_notices):
                dispatcher.utter_message(text="📄 '더보기'를 입력하면 다음 공지들을 보여드릴게요.")
                return [SlotSet("notice_page", notice_page + 1)]
            else:
                dispatcher.utter_message(text="✅ 모든 공지를 다 보여드렸습니다.")
                return [SlotSet("notice_page", 1)]

        except Exception as e:
            logger.error(f"[EXCEPTION] 오류 발생: {str(e)}", exc_info=True)
            dispatcher.utter_message(text="공지사항 정보를 불러오는 중 오류가 발생했습니다.")
            return []