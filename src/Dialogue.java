import java.util.ArrayList;

public class Dialogue {
    boolean onetime;
    String text;
    ArrayList<Choice> choices;

    public Dialogue(String text, boolean onetime, ArrayList<Choice> choices){
        this.onetime = onetime;
        this.text = text;
        this.choices = choices;
    }

}
