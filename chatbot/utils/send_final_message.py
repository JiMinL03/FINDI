from typing import Any, Dict, List, Text
from rasa_sdk import Tracker
from rasa_sdk.executor import CollectingDispatcher
from rasa_sdk.events import SlotSet

def send_final_message(dispatcher: CollectingDispatcher, message: Text, status: Text = "success", slots_to_reset: List[Text] = []) -> List[Dict[Text, Any]]:
    dispatcher.utter_message(
        text=message,
        metadata={
            "end_of_conversation": True,
            "status": status
        }
    )
    return [SlotSet(slot, None) for slot in slots_to_reset]
