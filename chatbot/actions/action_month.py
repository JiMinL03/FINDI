import logging
from typing import List, Text, Dict, Any
from rasa_sdk import Tracker
from rasa_sdk.executor import CollectingDispatcher
import re

from utils.base_action import BaseAction
from utils.json_loader import load_json

logger = logging.getLogger(__name__)

class ActionAcademicCalendar(BaseAction):
    def name(self) -> Text:
        return "action_academic_calendar"

    slots_to_reset = ["month"]

    def run(
            self,
            dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any],
    ) -> List[Dict[Text, Any]]:

        user_input = tracker.get_slot("month")
        logger.info(f"[INPUT] month 슬롯 값: {user_input}")

        if not user_input:
            return self.fail(dispatcher, "잘못된 입력입니다.")

        try:
            calendar_data = load_json("schedule.json")
            logger.info(f"[LOAD_JSON] {len(calendar_data)}개의 일정 항목 로드됨")

        except Exception as e:
            logger.error(f"[ERROR] JSON 로드 실패: {e}")
            return self.fail(dispatcher, "학사 일정을 불러오는 데 문제가 발생했어요.")

        result = []

        # 연도 입력인 경우 (예: "2025년")
        year_match = re.match(r"(20\d{2})년", user_input)
        if year_match:
            year = year_match.group(1)
            logger.info(f"[MATCH] 연도 매칭됨: {year}")
            for item in calendar_data:
                if item["MONTHVALUE"].startswith(f"{year}년"):
                    result.append(f"{item['MONTHVALUE']}: {item['CONTENTS']}")
            logger.debug(f"[RESULT] {year}년 일정 개수: {len(result)}")
            if not result:
                return self.fail(dispatcher, f"{year}년의 일정 정보를 찾을 수 없습니다.")
            dispatcher.utter_message(
                text=f"📅 {year}년 학사 일정입니다:\n" + "\n".join(result) + "\u2063__END__"
            )
            return self.reset_slots()

        # 월 입력인 경우 (예: "3월", "10월")
        month_match = re.match(r"(\d{1,2})월", user_input)
        if month_match:
            month = f"{int(month_match.group(1))}월"
            logger.info(f"[MATCH] 월 매칭됨: {month}")
            for item in calendar_data:
                if item["MONTHVALUE"].startswith(month) or f" {month}" in item["MONTHVALUE"]:
                    result.append(f"{item['MONTHVALUE']}: {item['CONTENTS']}")
            logger.debug(f"[RESULT] {month} 일정 개수: {len(result)}")
            if not result:
                return self.fail(dispatcher, f"{month}의 학사 일정을 찾을 수 없습니다.")
            dispatcher.utter_message(
                text=f"📘 {month} 일정입니다:\n" + "\n".join(result) + "\u2063__END__"
            )
            return self.reset_slots()

        # 유효하지 않은 입력인 경우
        logger.warning(f"[WARNING] 입력 매칭 실패: {user_input}")
        return self.fail(dispatcher, "유효한 월 또는 연도를 입력해주세요. 예: '3월', '2025년'")
