import java.util.ArrayList;

public class Location {
    String name;
    String description;

    ArrayList<Item> items;
    ArrayList<Stationary> stationaries;

    public Location(String name, String description, ArrayList<Item> items, ArrayList<Stationary> stationaries) {
        this.name = name;
        this.description = description;
        this.items = items;
        this.stationaries = stationaries;
    }
}
