import java.io.IOException;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("/Users/Naturelist/Desktop/Dev/Code/School/LK/TextAdventure/TextAdventure/src/data.json")));
        Data data = new Data();
        data.parseArrayData(json);

        IO.out("Ein ganz normaler Tag. Du gehst deinen Weg, ahnungslos, wie schnell sich alles ändern kann.\n" +
                "Ein Blick. Ein Schritt. Ein Moment – und plötzlich findest du dich mitten in einem Gerichtssaal wieder.\n" +
                "Die Luft ist gespannt. Augen richten sich auf dich.\n" +
                "Du bist der Angeklagte.\n" +
                "\n" +
                "Was ist passiert?\n" +
                "Was wird dir vorgeworfen?\n" +
                "Und vor allem: Bist du wirklich schuldig...?\n" +
                "\n" +
                "Tauch ein in ein interaktives Abenteuer voller Lügen, Beweise, Wendungen und Entscheidungen.\n" +
                "Jede Antwort zählt. Jede Reaktion entscheidet.\n" +
                "Das Gericht hat begonnen. Und du bist mittendrin.");
    }
};