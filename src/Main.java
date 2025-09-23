import com.google.gson.*;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.io.IOException;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("/Users/Naturelist/Desktop/Dev/Code/School/LK/TextAdventure/TextAdventure/src/data.json")));
        JsonObject data = JsonParser.parseString(json).getAsJsonObject();

        JsonArray dialogueArray = data.getAsJsonArray("Dialogue");

        for (int i = 0; i < dialogueArray.size(); i++) {
            JsonObject d = dialogueArray.get(i).getAsJsonObject();

            String text = d.get("text").getAsString();

            JsonArray options = d.getAsJsonArray("options");
            for (int j = 0; j < options.size(); j++) {
                System.out.println("  option: " + options.get(j).getAsString());
            }
        }






        // parse the root object but not into a class
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        String name = obj.get("name").getAsString();
        int age = obj.get("age").getAsInt();
        IO.out("Name: " + name + ", Age: " + age);

        IO.out("GO TO HELL");
    }
};