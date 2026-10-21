# actions/actions.py
from typing import Any, Text, Dict, List
from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher


class ActionRecommendDoctor(Action):
    def name(self) -> Text:
        return "action_recommend_doctor"

    def run(
            self,
            dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any],
    ) -> List[Dict[Text, Any]]:

        pet_type = tracker.get_slot("pet_type")

        if pet_type == "狗":
            dispatcher.utter_message(response="utter_recommend_zhang")
        elif pet_type == "猫":
            dispatcher.utter_message(response="utter_recommend_li")
        else:
            # 包括 None、兔子等不支持类型
            dispatcher.utter_message(response="utter_recommend_chen")

        return []