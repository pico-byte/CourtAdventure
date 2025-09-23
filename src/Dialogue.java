import com.google.gson.internal.bind.util.ISO8601Utils;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Dialogue {
    String text;
    ArrayList<String> options;
    Dialogue nextDialogue;


    public Dialogue(String text, String... options){
        this.text = text;
        this.options = new ArrayList<>(Arrays.asList(options));
    }

}
