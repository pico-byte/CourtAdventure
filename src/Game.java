import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Game {
    /*** Alle definierten Orte**/
    ArrayList<Location> locations;

    /*** Der Ort, wo der Spieler sich aktuell befindet**/
    Location currentLocation;

    /**Alle definierten Dialoge**/
    HashMap<String, Dialogue> dialogues;
    /**Das Inventar des Spielers**/
    ArrayList<Item> inventory;

    /**Konstruktor der Game Klasse. Benötigt keine Parameter, weil alle Daten aus der Data Klasse kommen**/
    public Game() {
        locations = Data.locations;
        dialogues = Data.dialogues;
        inventory = new ArrayList<>();
        currentLocation = locations.getFirst();
    }

    /**Startet das Adventure. Gibt einen coolen Header aus und beginnt den Game loop**/
    public void play() {
        IO.out("  _______        _                 _                 _                  \n" +
                " │__   __│      │ │       ╱╲      │ │               │ │                 \n" +
                "    │ │ _____  _│ │_     ╱  ╲   __│ │_   _____ _ __ │ │_ _   _ _ __ ___ \n" +
                "    │ │╱ _ ╲ ╲╱ ╱ __│   ╱ ╱╲ ╲ ╱ _` ╲ ╲ ╱ ╱ _ ╲ '_ ╲│ __│ │ │ │ '__╱ _ ╲\n" +
                "    │ │  __╱>  <│ │_   ╱ ____ ╲ (_│ │╲ V ╱  __╱ │ │ │ │_│ │_│ │ │ │  __╱\n" +
                "    │_│╲___╱_╱╲_╲╲__│ ╱_╱    ╲_╲__,_│ ╲_╱ ╲___│_│ │_│╲__│╲__,_│_│  ╲___│\n" +
                "                                                                        \n" +
                "                    Made in a TextAdventure Engine                      \n" +
                "\n \n ------------------------------------------------------------------------"
        );
        IO.out("Willkommen in diesem Text-Adventure! Verwende den Befehl \"help\" um die Liste mit allen Befehlen zu sehen" +
                "\n Groß und Kleinschreibung bei den Namen der Orte, items usw. spielt KEINE Rolle! Und viele der Befehle haben Kurzformen (mehr unter \"help\")");
        IO.out(" ------------------------------------------------------------------------" + "\n\n");
        while (true) {
            showLocation();
            String input = IO.ask("\n \n >> ");
            handleCommand(input);
        }
    }

    /**Gibt den Aktuellen Ort, zusammen mit allen dort zu findenden Items und NPCs**/
    private void showLocation() {
        IO.out("\n ------- Standort: " + currentLocation.name + " -------");
        IO.out(currentLocation.description);

        IO.out("");

        IO.out("Mögliche Orte:");
        for (Location l : locations) {
            if (l != currentLocation) {
                IO.outLine(" | " + l.name);
            }
        }
        IO.outLine(" |");

        IO.out("\n");

        IO.out("Items an diesem Ort:");
        for (Item i : currentLocation.items) {
            IO.outLine(" | " + i.name);
        }
        IO.outLine(" |");

        IO.out("\n");
        if (!currentLocation.stationaries.isEmpty()) {
            IO.out("Menschen und andere Objekte:");
            for (Stationary s : currentLocation.stationaries) {
                IO.outLine(" | " + s.name);
            }
            IO.outLine("  |");
        }
    }

    /**Liest die Commands des Spielers aus der Konsole ein
     * @param input ist die Eingabe des Spielers**/
    private void handleCommand(String input) {
        String[] parts = input.split(" ", 2);
        String cmd = parts[0].toLowerCase();

        switch (cmd) {
            case "move", "goto", "cd", "mv", "m":
                if (parts.length > 1) moveTo(parts[1]);
                else IO.out("Wohin möchtest du gehen?");
                break;
            case "interact", "i":
                if (parts.length > 1) interact(parts[1]);
                else IO.out("Mit wem oder was willst du interagieren?");
                break;
            case "inspect", "spec":
                if (parts.length > 1) inspect(parts[1]);
                else IO.out("Wen oder was willst du inspizieren?");
                break;
            case "take", "t":
                if (parts.length > 1) takeItem(parts[1]);
                else IO.out("Welches Item willst du nehmen?");
                break;
            case "talk", "bla":
                if (parts.length > 1) talk(parts[1]);
                else IO.out("Mit wem willst du sprechen?");
                break;
            case "inv", "inventory":
                showInventory();
                break;
            case "quit":
                IO.out("Spiel beendet!");
                System.exit(0);
                break;
            case "help":
                IO.out("Folgende Befehle Kannst du verwenden (Keine Klammern, einfach nur Befehl + Parameter) \n Besonders wichtig: \n - move {Name des Ortes}       Alternativ auch: \"move\", \"goto\", \"cd\", \"mv\", \"m\"  " +
                        "\n - interact {Objekt}     Alternativ: \"i\"" +
                        "\n - take {Item}           Alternativ \"t\"" +
                        "\n - inventory             Alternativ: \"inv\"" +
                        "\n Weitere Befehle die interessant sein könnten: " +
                        "\n - inspect {Objekt}      Alternativ: \"spec\"" +
                        "\n - talk {NPC/Objekt}     Alternativ: \"talk\", \"bla\"" +
                        "\n - quit");
                break;
            default:
                IO.out("Unbekannter Befehl!");
        }
    }

    /**CMD - Bewegt den Spieler an einen neuen Ort
     * @param locationName ist der Name des neuen Ortes**/
    private void moveTo(String locationName) {
        for (Location l : locations) {
            if (l.name.equalsIgnoreCase(locationName)) {
                currentLocation = l;
                IO.out("Du bist jetzt in " + l.name + ".");
                return;
            }
        }
        IO.out("Kein Ort gefunden mit dem Namen: " + locationName);
    }

    /** Durchsucht den Ort, das Inventar und die restlichen Interactables nach diesem Item oder Interactable NPC/Stationary und führt den Hauptverwendungszweck aus. inspect für item (sinngemäß) und talk für stationary/NPCs
     * @param name ist das zu betrachtende item/stationary**/
    private void interact(String name){
        for (Stationary s : currentLocation.stationaries) {
            if (s.name.equalsIgnoreCase(name) && !s.dialogue.isEmpty()) {
                //Findet das passendste Dialog
                Dialogue d = pickDialogue(s.dialogue);
                if (d != null) {
                    //Führt den Dialog aus
                    runDialogue(d, s.dialogue);
                } else {
                    IO.out(s.name + " ist gerade nicht ansprechbar.");
                }
                return;
            }
        }
        for (Item i : currentLocation.items) {
            if (i.name.equalsIgnoreCase(name) && !i.dialogue.isEmpty()) {
                //Findet das passendste Dialog
                Dialogue d = pickDialogue(i.dialogue);
                if (d != null) {
                    //Führt den Dialog aus
                    runDialogue(d, i.dialogue);
                } else {
                    IO.out(i.name + " kannst du dir nicht anschauen");
                }
                return;
            }
        }
        for (Item i : inventory) {
            if (i.name.equalsIgnoreCase(name) && !i.dialogue.isEmpty()) {
                //Findet das passendste Dialog
                Dialogue d = pickDialogue(i.dialogue);
                if (d != null) {
                    //Führt den Dialog aus
                    runDialogue(d, i.dialogue);
                } else {
                    IO.out(i.name + " kannst du dir nicht anschauen");
                }
                return;
            }
        }
        IO.out("Hier gibt es niemanden mit dem Namen: " + name);
    }


    /** <<<OPTIONAL - ERSETZT DURCH INTERACT>>> Durchsucht das den Ort, das Inventar und die restlichen Interactables nach einem Item und ruft inspect dafür auf
     * @param name ist das zu inspizierende item**/
    private void inspect(String name) {
        //Durchsuchen des Ortes
        for (Item i : currentLocation.items) {
            if (i.name.equalsIgnoreCase(name)) {
                IO.out(i.inspect());
                return;
            }
        }
        //Durchsuchen des Inventars
        for (Item i : inventory) {
            if (i.name.equalsIgnoreCase(name)) {
                IO.out(i.inspect());
                return;
            }
        }
        //Durchsuchen der Stationären Items
        for (Stationary s : currentLocation.stationaries) {
            if (s.name.equalsIgnoreCase(name)) {
                IO.out(s.inspect());
                return;
            }
        }
        IO.out("Nichts gefunden mit dem Namen: " + name);
    }

    /**Entfernt ein item vom Ort und fügt es dem Inventar hinzu
     * @param name Name des Items**/
    private void takeItem(String name) {
        Item found = null;
        for (Item i : currentLocation.items) {
            if (i.name.equalsIgnoreCase(name)) {
                found = i;
                break;
            }
        }
        if (found != null) {
            inventory.add(found);
            currentLocation.items.remove(found);
            IO.out("Du hast " + found.name + " aufgenommen.");
        } else {
            IO.out("Kein solches Item hier.");
        }
    }

    /**Ausführen des passendsten Dialoges eines Stationären Interactables
     * @param name Name des Interactable**/
    private void talk(String name) {
        for (Stationary s : currentLocation.stationaries) {
            if (s.name.equalsIgnoreCase(name) && !s.dialogue.isEmpty()) {
                //Findet das passendste Dialog
                Dialogue d = pickDialogue(s.dialogue);
                if (d != null) {
                    //Führt den Dialog aus
                    runDialogue(d, s.dialogue);
                } else {
                    IO.out(s.name + " ist gerade nicht ansprechbar.");
                }
                return;
            }
        }
        IO.out("Hier gibt es niemanden mit dem Namen: " + name);
    }

    /**Suchen nach dem richtigen Dialog
     * @param dialogues Liste mit dem Dialog eines bestimmten Objekts**/
    private Dialogue pickDialogue(ArrayList<Dialogue> dialogues) {
        for (Dialogue dialogue : dialogues) {
            //Überprüfen, ob es eine für den Spieler zugängliche Option im Dialog gibt
            boolean hasAvailableChoice = false;

            if (dialogue.choices == null || dialogue.choices.isEmpty()) {
                //Dialog ohne Choices ist für den Spieler immer zugänglich
                hasAvailableChoice = true;
            } else {
                //Überprüfen, ob eine der Choices ausgeführt werden können
                for (Choice choice : dialogue.choices) {
                    if (choice.conditionItem == null) {
                        //Unbedingte Choices können ime ausgewählt werden
                        hasAvailableChoice = true;
                        break;
                    } else {
                        //Bedingte Choices brauchen ein bestimmtes item im Inventar des Spielers
                        boolean hasItem = false;
                        for (Item item : inventory) {
                            if (item.name.equalsIgnoreCase(choice.conditionItem)) {
                                hasItem = true;
                                break;
                            }
                        }
                        if (hasItem) {
                            hasAvailableChoice = true;
                            break;
                        }
                    }
                }
            }
            if (hasAvailableChoice) {
                return dialogue;
            }
        }
        return null;
    }

    /**Ausführen des Dialogs
     * @param dialogue Der Auszuführende Dialog
     * @param dialogueList Liste mit dem Dialog des Objektes/Items**/
    private void runDialogue(Dialogue dialogue, ArrayList<Dialogue> dialogueList) {
        Dialogue current = dialogue;
        while (current != null) {
            IO.out("\n" + current.text);

            if (current.choices == null || current.choices.isEmpty()) {
                break;
            }

            for (int i = 0; i < current.choices.size(); i++) {
                IO.out((i + 1) + ") " + current.choices.get(i).text);
            }

            int playersChoiceIndex = askPlayerChoice(current.choices.size());
            Choice chosenChoice = current.choices.get(playersChoiceIndex);

            if (chosenChoice.conditionItem != null) {
                //Hat der Spieler das Item?
                boolean hasItem = false;
                for (Item item : inventory) {
                    if (item.name.equalsIgnoreCase(chosenChoice.conditionItem)) {
                        hasItem = true;
                        break;
                    }
                }
                if (!hasItem) {
                    IO.out("Du benötigst " + chosenChoice.conditionItem + " um das zu tun.");
                    continue;
                }
            }

            //Entfernen des Items, falls diese Choice bezahlt werden muss
            if (chosenChoice.paymentItem != null) {
                for (int i = 0; i < inventory.size(); i++) {
                    Item item = inventory.get(i);
                    if (item.name.equalsIgnoreCase(chosenChoice.paymentItem)) {
                        inventory.remove(i);
                        IO.out(chosenChoice.paymentItem + " wurde verbraucht.");
                        break;
                    }
                }
            }

            //Hinzufügen des Belohnungs-Items zum Inventar
            if (chosenChoice.rewardItem != null) {
                if (Data.itemMap.containsKey(chosenChoice.rewardItem)) {
                    Item rewardItem = Data.itemMap.get(chosenChoice.rewardItem);
                    inventory.add(rewardItem);
                    IO.out("Du hast " + rewardItem.name + " erhalten!");
                }
            }

            current = dialogues.get(chosenChoice.nextDialogueID);
        }

        //Entfernen von Einmaligem Dialog
        if (dialogue.onetime) {
            dialogueList.remove(dialogue);
        }
    }

    /**Auswahl einer Choice durch den Player
     * @param maxNumber die Anzahl zur Verfügung stehenden choices**/
    private int askPlayerChoice(int maxNumber) {
        while (true) {
            String in = IO.ask("Wähle eine Option (1-" + maxNumber + "):");
            try {
                int choice = Integer.parseInt(in);
                if (choice >= 1 && choice <= maxNumber) {
                    return choice - 1;
                }
            } catch (NumberFormatException e) {
            }
            IO.out("Ungültige Eingabe.");
        }
    }

    /**Ausgabe des gesamten Inventars**/
    private void showInventory() {
        IO.out("\n--- Inventar ---");
        if (inventory.isEmpty()) {
            IO.out("Leer.");
        } else {
            for (Item i : inventory) {
                IO.out(" - " + i.name);
            }
        }
    }
}
