import com.google.gson.*;

import java.io.IOException;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String json = Files.readString(Path.of("src/package.json"));
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();


        String name = obj.get("name").getAsString();
        int age = obj.get("age").getAsInt();
        IO.out("Name: " + name + ", Age: " + age);

        IO.out("GO TO HELL");
    }
};