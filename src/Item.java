import java.util.ArrayList;

public class Item extends Interactable{
    int value;


    @Override
    public String inspect() {
        return name + "\n Beschreibung: " + description + "\n Wert: " + value;
    }


    public Item(String name, String description, ArrayList<Dialogue> dialogue, int value){
        super(name,description,dialogue);
        this.value = value;
    }
}
