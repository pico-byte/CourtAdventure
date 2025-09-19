import java.util.ArrayList;

public class Stationary extends Interactable{
    @Override
    public String inspect() {
        return name + "\n Beschreibung: " + description + "\n Wert: ";
    }

    public Stationary(String name, String description, ArrayList<Dialogue> dialogue){
        super(name,description,dialogue);
    }
}
