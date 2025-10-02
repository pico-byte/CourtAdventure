import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    public static ArrayList<Location> locations;
    public static HashMap<String, Dialogue> dialogues;


    public static HashMap<String, Item> itemMap;
    public static HashMap<String, Stationary> stationaryMap;

    public void parseArrayData(String json){
        JsonObject data = JsonParser.parseString(json).getAsJsonObject();

        dialogues = new HashMap<>();
        //Alle Dialoge aus der JSON lesen
        JsonArray dialogueArray = data.getAsJsonArray("dialogues");
        for (JsonElement dialogue : dialogueArray) {
            JsonObject d = dialogue.getAsJsonObject();
            String id = d.get("id").getAsString();
            String text = d.get("text").getAsString();
            JsonArray choicesJ = d.get("choices").getAsJsonArray();
            ArrayList<Choice> choices = new ArrayList<>();
            boolean onetime = false;
            if (d.has("onetime")) {
                onetime = d.get("onetime").getAsBoolean();
            }
            for (JsonElement c : choicesJ) {
                choices.add(getChoice(c));
            }
            dialogues.put(id, new Dialogue(text, onetime, choices));
        }

        //Items aus der JSOn lesen
        itemMap = new HashMap<>();
        JsonArray itemArray = data.getAsJsonArray("Items");
        for (JsonElement itemElem : itemArray) {
            JsonObject i = itemElem.getAsJsonObject();
            String name = i.get("name").getAsString();
            String description = i.get("description").getAsString();
            int value = i.get("value").getAsInt();

            ArrayList<Dialogue> dialoguesForItem = new ArrayList<>();
            if (i.has("dialogues")) {
                JsonArray dlgArray = i.getAsJsonArray("dialogues");
                for (JsonElement dlgIdElem : dlgArray) {
                    String dlgId = dlgIdElem.getAsString();
                    if (dialogues.containsKey(dlgId)) {
                        dialoguesForItem.add(dialogues.get(dlgId));
                    }
                }
            }

            itemMap.put(name, new Item(name, description, dialoguesForItem, value));
        }

        //Stationäre Items/Npcs aus der JSON lesen
        stationaryMap = new HashMap<>();
        JsonArray stationaryArray = data.getAsJsonArray("Stationary");
        for (JsonElement sElem : stationaryArray) {
            JsonObject s = sElem.getAsJsonObject();
            String name = s.get("name").getAsString();
            String description = s.get("description").getAsString();

            ArrayList<Dialogue> dialoguesForStationary = new ArrayList<>();
            if (s.has("dialogues")) {
                JsonArray dlgArray = s.getAsJsonArray("dialogues");
                for (JsonElement dlgIdElem : dlgArray) {
                    String dlgId = dlgIdElem.getAsString();
                    if (dialogues.containsKey(dlgId)) {
                        dialoguesForStationary.add(dialogues.get(dlgId));
                    }
                }
            } else if (s.has("dialogue")) {
                // backwards compatibility: single dialogue
                String dlgId = s.get("dialogue").getAsString();
                if (dialogues.containsKey(dlgId)) {
                    dialoguesForStationary.add(dialogues.get(dlgId));
                }
            }

            stationaryMap.put(name, new Stationary(name, description, dialoguesForStationary));
        }

        //Locations aus der JSON auslesen
        locations = new ArrayList<>();
        JsonArray locationArray = data.getAsJsonArray("Locations");
        for (JsonElement locationElem : locationArray) {
            JsonObject l = locationElem.getAsJsonObject();

            String name = l.get("name").getAsString();
            String description = l.get("description").getAsString();

            //Items dem Ort zuordnen
            ArrayList<Item> items = new ArrayList<>();
            if (l.has("items")) {
                JsonArray itemsJ = l.getAsJsonArray("items");
                for (JsonElement itemElem : itemsJ) {
                    String itemName = itemElem.getAsString();
                    if (itemMap.containsKey(itemName)) {
                        items.add(itemMap.get(itemName));
                    }
                }
            }

            //Stationäre Items dem Ort hinzufügen
            ArrayList<Stationary> stationaries = new ArrayList<>();
            if (l.has("stationary")) {
                JsonArray stationaryJ = l.getAsJsonArray("stationary");
                for (JsonElement sElem : stationaryJ) {
                    String stationaryName = sElem.getAsString();
                    if (stationaryMap.containsKey(stationaryName)) {
                        stationaries.add(stationaryMap.get(stationaryName));
                    }
                }
            }

            locations.add(new Location(name, description, items, stationaries));
        }
    }


    private static Choice getChoice(JsonElement c) {
        JsonObject choiceJ = c.getAsJsonObject();
        String cId = choiceJ.get("nextDialogueID").getAsString();
        String cText = choiceJ.get("text").getAsString();
        Choice choice = new Choice(cText, cId);
        if (choiceJ.has("condition")) {
            choice.setConditionItem(choiceJ.get("condition").getAsString());
        }
        if (choiceJ.has("remove")) {
            choice.setPaymentItem(choiceJ.get("remove").getAsString());
        }
        return choice;
    }
}
