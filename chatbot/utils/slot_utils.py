
from typing import List, Text, Dict, Any
from rasa_sdk.events import SlotSet, EventType


def reset_slots(self) -> List[EventType]:
    return [SlotSet(slot, None) for slot in getattr(self, "slots_to_reset", [])]

