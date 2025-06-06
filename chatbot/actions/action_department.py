from typing import Any, Text, Dict, List
from rasa_sdk import Tracker
from rasa_sdk.executor import CollectingDispatcher
from rasa_sdk.types import DomainDict

from utils.base_action import BaseAction
from utils.json_loader import load_json
from utils.logging_utils import get_logger

logger = get_logger(__name__)


class ActionFacultyInfo(BaseAction):
    def name(self) -> Text:
        return "action_faculty_info"

    slots_to_reset = ["faculty_name", "department_name"]

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        logger.debug("action_faculty_info 실행")

        faculty_name = tracker.get_slot("faculty_name")
        logger.debug(f"슬롯(faculty_name): {faculty_name}")

        if not faculty_name:
            return self.fail(dispatcher, "해당 단과대학이 없거나 틀렸습니다. 정확한 이름을 입력해주세요.")

        try:
            colleges = load_json("college.json")
            departments = load_json("department.json")
            logger.debug(f"로드된 단과대학 데이터: {colleges}")
            logger.debug(f"로드된 학과 데이터: {departments}")
        except Exception as e:
            logger.error(f"데이터 로드 오류: {e}")
            return self.fail(dispatcher, "데이터 로드 중 문제가 발생했습니다.")

        college_id = next((c["COLLEGE_ID"] for c in colleges if c["NAME"] == faculty_name), None)
        logger.debug(f"조회된 college_id: {college_id}")

        if not college_id:
            return self.fail(dispatcher, f"{faculty_name}에 해당하는 단과대학 정보를 찾을 수 없습니다.")

        department_names = [
            d["DEPARTMENT_NAME"] for d in departments if d["COLLEGE_ID"] == college_id
        ]

        if department_names:
            department_list = "\n".join(department_names)
            dispatcher.utter_message(text=f"{faculty_name} 소속 학과는 다음과 같습니다:\n{department_list}")
        else:
            dispatcher.utter_message(text=f"{faculty_name}에는 등록된 학과가 없습니다.")

        return self.reset_slots()


class ActionDepartmentInfo(BaseAction):
    def name(self) -> Text:
        return "action_department_info"

    slots_to_reset = ["department_name"]

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: DomainDict) -> List[Dict[Text, Any]]:

        logger.debug("action_department_info 실행")

        department_name = tracker.get_slot("department_name")
        logger.debug(f"슬롯(department_name): {department_name}")

        if not department_name:
            return self.fail(dispatcher, "해당하는 학과명이 없습니다.")

        try:
            departments = load_json("department.json")
            logger.debug(f"로드된 학과 데이터: {departments}")
        except Exception as e:
            logger.error(f"데이터 로드 실패: {e}")
            return self.fail(dispatcher, "학과 정보를 불러오는 중 문제가 발생했습니다.")

        department_info = next(
            (dept for dept in departments if dept["DEPARTMENT_NAME"] == department_name),
            None
        )

        logger.debug(f"조회된 학과 정보: {department_info}")

        if not department_info:
            return self.fail(dispatcher, f"{department_name}에 대한 정보를 찾을 수 없습니다.")

        phone = department_info.get("DEPARTMENT_PHONE", "전화번호 정보 없음")
        dispatcher.utter_message(text=f"{department_name}의 전화번호는 {phone}입니다.")

        return self.reset_slots()
