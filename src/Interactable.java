import java.util.ArrayList;

public abstract class Interactable {
    String name;
    String description;
    ArrayList<Dialogue> dialogue;

    public abstract String inspect();

    public String use(){
        return "Can't do that";
    }


    public Interactable(String name, String description, ArrayList<Dialogue> dialogue){
        this.name = name;
        this.description = description;
        this.dialogue = dialogue;
    }
}
