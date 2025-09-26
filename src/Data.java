import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

public class Data {
    ArrayList<Location> locations;
    public void parseArrayData(String json) throws IOException {
        JsonObject data = JsonParser.parseString(json).getAsJsonObject();

        //Dialogue



        JsonArray dialogueArray = data.getAsJsonArray("Items");

        JsonObject d = dialogueArray.get(0).getAsJsonObject();
        String name = d.get("name").getAsString();
        String description = d.get("description").getAsString();
        JsonArray options = d.getAsJsonArray("options");
        IO.out(name);
        IO.out(description);
    }
}
