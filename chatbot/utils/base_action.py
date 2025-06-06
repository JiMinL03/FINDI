from typing import List, Text, Dict, Any
from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher
from rasa_sdk.events import SlotSet

class BaseAction(Action):
    def name(self) -> Text:
        return "base_action"  # 더미 값 (실행되지 않음)

    slots_to_reset: List[Text] = []

    def reset_slots(self) -> List[Dict[Text, Any]]:
        return [SlotSet(slot, None) for slot in self.slots_to_reset]

    def fail(self, dispatcher: CollectingDispatcher, message: str) -> List[Dict[Text, Any]]:
        """오류 메시지 전송 후 슬롯 초기화"""
        dispatcher.utter_message(text=message)
        return self.reset_slots()
