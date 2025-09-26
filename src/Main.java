import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class Main {
    ArrayList<Location> locations;
    Location currentLocation;

    Dialogue startCutscene;

    ArrayList<Item> inventory;

    public static void main(String[] args) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("/Users/Naturelist/Desktop/Dev/Code/School/LK/TextAdventure/TextAdventure/src/data.json")));
        Data data = new Data();
        data.parseArrayData(json);



        IO.out("Hello World");
    }
};