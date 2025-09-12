public class Interactable {
    String name;
    String description;

    int value;

    public String inspect(){
        return name + "\n Beschreibung: " + description + "\n Wert: " + value;
    }

    public String use(){
        return "Can't do that";
    }
}
