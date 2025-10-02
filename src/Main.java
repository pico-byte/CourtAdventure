import java.io.IOException;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //Laden der JSON
        String json = new String(Files.readAllBytes(Paths.get("src/data.json")));

        //JSON Daten interpretieren
        Data data = new Data();
        data.parseArrayData(json);

        //ADVENTURE starten
        Game game = new Game();
        game.play();
    }
}
