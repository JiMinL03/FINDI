from typing import Any, Text, Dict, List
from rasa_sdk import Tracker
from rasa_sdk.executor import CollectingDispatcher

from utils.base_action import BaseAction
from utils.json_loader import load_json

class ActionProfessorInfoByName(BaseAction):
    def name(self) -> Text:
        return "action_professor_by_name"

    slots_to_reset = ["professor_name"]

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        professor_name = tracker.get_slot("professor_name")

        if not professor_name:
            return self.fail(dispatcher, "잘못된 입력입니다.")


        professors = load_json("PROFESSOR.json")

        matching_professor = next(
            (prof for prof in professors if prof.get("NAME") == professor_name), None)

        if matching_professor:
            info = f"👨‍🏫 {matching_professor['NAME']} 교수님 정보입니다:\n"
            info += f"- 연구 분야: {matching_professor.get('RESEARCH_AREA', '정보 없음')}\n"
            info += f"- 이메일: {matching_professor.get('EMAIL', '정보 없음')}\n"
            info += f"- 전화번호: {matching_professor.get('PHONE', '정보 없음')}\n"
            info += f"- 연구실: {matching_professor.get('LAB', '정보 없음')}\u2063__END__"
            dispatcher.utter_message(
                text=info
            )
        else:
            dispatcher.utter_message(
                text=f"{professor_name} 교수님의 정보를 찾을 수 없습니다.\u2063__END__",
            )

        return self.reset_slots()
