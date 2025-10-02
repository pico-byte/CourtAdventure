import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Game {
    ArrayList<Location> locations;
    Location currentLocation;

    HashMap<String, Dialogue> dialogues;
    ArrayList<Item> inventory;

    Scanner scanner;

    public Game() {
        locations = Data.locations;
        dialogues = Data.dialogues;
        inventory = new ArrayList<>();
        scanner = new Scanner(System.in);


        currentLocation = locations.getFirst();
    }

    public void play() {
        IO.out("Willkommen zum Text-Adventure!");
        while (true) {
            showLocation();
            String input = IO.ask("\nBefehl? (move {Ort}, inspect {Objekt}, talk {NPC/Objekt}, take {Item}, inv, quit)");
            handleCommand(input);
        }
    }

    private void showLocation() {
        IO.out("\n --- Standort: " + currentLocation.name + " ---");
        IO.out(currentLocation.description);


        IO.out("Items an diesem Ort:");
        for (Item i : currentLocation.items) {
            IO.out(" - " + i.name);
        }

        if (!currentLocation.stationaries.isEmpty()) {
            IO.out("Menschen und andere Objekte:");
            for (Stationary s : currentLocation.stationaries) {
                IO.out(" - " + s.name);
            }
        }

        IO.out("Mögliche Orte:");
        for (Location l : locations) {
            if (l != currentLocation) {
                IO.out(" - " + l.name);
            }
        }
    }

    private void handleCommand(String input) {
        String[] parts = input.split(" ", 2);
        String cmd = parts[0].toLowerCase();

        switch (cmd) {
            case "move", "goto":
                if (parts.length > 1) moveTo(parts[1]);
                else IO.out("Wohin möchtest du gehen?");
                break;
            case "inspect":
                if (parts.length > 1) inspect(parts[1]);
                else IO.out("Wen oder was willst du inspizieren?");
                break;
            case "take":
                if (parts.length > 1) takeItem(parts[1]);
                else IO.out("Welches Item willst du nehmen?");
                break;
            case "talk":
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
            default:
                IO.out("Unbekannter Befehl!");
        }
    }

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

    private void inspect(String name) {
        for (Item i : currentLocation.items) {
            if (i.name.equalsIgnoreCase(name)) {
                IO.out(i.inspect());
                return;
            }
        }
        for (Stationary s : currentLocation.stationaries) {
            if (s.name.equalsIgnoreCase(name)) {
                IO.out(s.inspect());
                return;
            }
        }
        IO.out("Nichts gefunden mit dem Namen: " + name);
    }

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

    private void talk(String name) {
        for (Stationary s : currentLocation.stationaries) {
            if (s.name.equalsIgnoreCase(name) && !s.dialogue.isEmpty()) {
                Dialogue d = pickDialogue(s.dialogue);
                if (d != null) {
                    runDialogue(d, s.dialogue);
                } else {
                    IO.out(s.name + " ist gerade nicht ansprechbar.");
                }
                return;
            }
        }
        IO.out("Hier gibt es niemanden mit dem Namen: " + name);
    }

    private Dialogue pickDialogue(ArrayList<Dialogue> dialogues) {
        for (Dialogue dialogue : dialogues) {
            // Check if at least one choice is available (either no condition or condition is met)
            boolean hasAvailableChoice = false;

            if (dialogue.choices == null || dialogue.choices.isEmpty()) {
                // If dialogue has no choices, it's always available
                hasAvailableChoice = true;
            } else {
                // Check if at least one choice is available
                for (Choice choice : dialogue.choices) {
                    if (choice.conditionItem == null) {
                        // Choice has no condition, so it's available
                        hasAvailableChoice = true;
                        break;
                    } else {
                        // Check if player has the required item for this choice
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

            //Hinzufügen des Belohnungs-Items zur Inventar
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
