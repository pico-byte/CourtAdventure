import java.awt.*;
import java.util.ArrayList;

public class Npc extends Interactable{

    ArrayList<Item> inventory;


    @Override
    public String inspect() {
        return name + "\n Beschreibung: " + description + "\n Wert: ";
    }

    public Npc(String name, String description, ArrayList<Dialogue> dialogue, ArrayList<Item> items){
        super(name,description,dialogue);
        this.inventory = items;
    }
}
