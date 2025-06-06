from typing import Any, Text, Dict, List
from rasa_sdk import Tracker, FormValidationAction, Action
from rasa_sdk.events import FollowupAction, EventType, SlotSet, ActiveLoop
from rasa_sdk.executor import CollectingDispatcher
from rasa_sdk.types import DomainDict

from utils.base_action import BaseAction
from utils.json_loader import load_json
from utils.logging_utils import get_logger

logger = get_logger(__name__)

class ActionProfessorInfoByName(BaseAction):
    def name(self) -> Text:
        return "action_professor_by_name"

    slots_to_reset = ["professor_name", "department_name", "decision_slot"]

    def run(self, dispatcher, tracker, domain):
        professor_name = tracker.get_slot("professor_name")
        if not professor_name:
            return self.fail(dispatcher, "교수 이름이 지정되지 않았습니다.")
        # (실제 처리 생략)
        dispatcher.utter_message(text=f"👨‍🏫 {professor_name} 교수님 정보입니다.")
        return self.reset_slots()

class ActionProfessorsByDepartment(BaseAction):
    def name(self) -> Text:
        return "action_professor_by_department"

    slots_to_reset = ["professor_name", "department_name", "decision_slot"]

    def run(self, dispatcher, tracker, domain):
        department_name = tracker.get_slot("department_name")
        if not department_name:
            return self.fail(dispatcher, "학과명이 지정되지 않았습니다.")
        # (실제 처리 생략)
        dispatcher.utter_message(text=f"📘 {department_name} 학과 교수 목록입니다.")
        return self.reset_slots()

class ValidateProfessorInfoForm(FormValidationAction):
    def name(self) -> Text:
        return "validate_professor_info_form"

    async def extract_decision_slot(self, dispatcher, tracker, domain) -> Dict[Text, Any]:
        professor_name = next(tracker.get_latest_entity_values("professor_name"), None)
        department_name = next(tracker.get_latest_entity_values("department_name"), None)

        logger.debug(f"extract_decision_slot: professor_name={professor_name}, department_name={department_name}")

        if professor_name:
            return {"decision_slot": "professor", "professor_name": professor_name}
        elif department_name:
            return {"decision_slot": "department", "department_name": department_name}
        else:
            return {}

    async def validate_decision_slot(self, value, dispatcher, tracker, domain):
        if value in ["professor", "department"]:
            return {"decision_slot": value}
        else:
            # 그냥 None으로 세팅해서 폼이 종료되도록 함
            return {"decision_slot": None}

    async def submit(self, dispatcher, tracker, domain):
        decision = tracker.get_slot("decision_slot")
        if decision is None:
            dispatcher.utter_message(text="정보가 부족하여 폼을 종료합니다.")
            return [ActiveLoop(None)]
        else:
            return []

class ActionRouteProfessorInfo(Action):
    def name(self) -> Text:
        return "action_route_professor_info"

    def run(self, dispatcher, tracker, domain):
        decision = tracker.get_slot("decision_slot")

        if decision == "professor":
            return [SlotSet("decision_slot", None), FollowupAction("action_professor_by_name")]
        elif decision == "department":
            return [SlotSet("decision_slot", None), FollowupAction("action_professor_by_department")]
        else:
            dispatcher.utter_message(text="알 수 없는 요청입니다. 다시 시도해주세요.")
            return [SlotSet("decision_slot", None)]
