package trainer;

import java.util.*;

public class Main {
    private static List<Vocab> vocabList = VocabRepository.load();
    private static Scanner scanner = new Scanner(System.in);
    private static StatsManager stats = new StatsManager();

    public static void main(String[] args) {
        System.out.println("Willkommen zum Vokabeltrainer (Deutsch ↔ Albanisch)");

        boolean running = true;
        while (running) {
            System.out.println("\nMenü:");
            System.out.println("1. Vokabel hinzufügen");
            System.out.println("2. Vokabeln anzeigen");
            System.out.println("3. Vokabeln abfragen");
            System.out.println("4. Fehler-Quiz starten");
            System.out.println("5. Vokabel suchen");
            System.out.println("6. Beenden");
            System.out.print("Auswahl: ");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    addVocab();
                    break;
                case "2":
                    listVocabs();
                    break;
                case "3":
                    quiz();
                    break;
                case "4":
                    startFehlerQuiz(vocabList, scanner);
                    break;
                case "5":
                    searchVocab();
                    break;
                case "6":
                    VocabRepository.save(vocabList);
                    System.out.println("📦 Vokabeln gespeichert. Programm beendet.");
                    running = false;
                    break;
                default:
                    System.out.println("❗ Ungültige Eingabe – bitte erneut versuchen.");
            }
        }
    }

    private static void addVocab() {
        System.out.print("Deutsch: ");
        String deutsch = scanner.nextLine();
        System.out.print("Albanisch: ");
        String albanisch = scanner.nextLine();

        boolean exists = vocabList.stream().anyMatch(v -> v.getDeutsch().equalsIgnoreCase(deutsch) && v.getAlbanisch().equalsIgnoreCase(albanisch));

        if (exists) {
            System.out.println("⚠️ Diese Vokabel existiert bereits.");
            return;
        }

        vocabList.add(new Vocab(deutsch, albanisch));
        VocabRepository.save(vocabList);
        System.out.println("✅ Vokabel gespeichert!");
    }

    private static void listVocabs() {
        if (vocabList.isEmpty()) {
            System.out.println("Noch keine Vokabeln gespeichert.");
        } else {
            System.out.println("Gespeicherte Vokabeln:");
            for (Vocab v : vocabList) {
                System.out.println(v);
            }
        }
    }

    private static void quiz() {
        Random random = new Random();
        if (vocabList.isEmpty()) {
            System.out.println("Keine Vokabeln vorhanden.");
            return;
        }

        System.out.println("\nWelche Richtung möchtest du üben?");
        System.out.println("1. Deutsch → Albanisch");
        System.out.println("2. Albanisch → Deutsch");
        System.out.println("3. Zufällig");
        System.out.print("Auswahl: ");
        String richtung = scanner.nextLine();

        int modus = switch (richtung) {
            case "1" -> 1;
            case "2" -> 2;
            default -> 3;
        };

        Collections.shuffle(vocabList);
        stats.reset();
        List<Vocab> falschBeantwortet = new ArrayList<>();

        for (Vocab v : vocabList) {
            boolean direction;
            if (modus == 1) {
                direction = true; // Deutsch → Albanisch
            } else if (modus == 2) {
                direction = false; // Albanisch → Deutsch
            } else {
                direction = random.nextBoolean();
            }

            String question = direction ? v.getDeutsch() : v.getAlbanisch();
            String expected = direction ? v.getAlbanisch() : v.getDeutsch();

            System.out.print("Was heißt \"" + question + "\" auf " + (direction ? "Albanisch" : "Deutsch") + "? ");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase(expected)) {
                System.out.println("✅ Richtig!");
                stats.addCorrect();
            } else {
                System.out.println("❌ Falsch! Richtig: " + expected);
                stats.addIncorrect();
                falschBeantwortet.add(v);
            }
        }

        System.out.println("\n📊 Ergebnis:");
        System.out.println("✅ Richtig: " + stats.getCorrect());
        System.out.println("❌ Falsch: " + stats.getIncorrect());
        System.out.printf("🎯 Trefferquote: %.2f %%\n", stats.getAccuracyPercent());

        if (!falschBeantwortet.isEmpty()) {
            System.out.println("\nFalsch beantwortete Vokabeln:");
            for (Vocab v : falschBeantwortet) {
                System.out.println(v);
            }
            schreibeFehlerliste(falschBeantwortet);
        } else {
            System.out.println("\nAlle Vokabeln richtig! 🎉");
        }
    }

    private static void schreibeFehlerliste(List<Vocab> fehler) {
        try (java.io.FileWriter writer = new java.io.FileWriter("fehlerliste.txt")) {
            writer.write("Falsch beantwortete Vokabeln:\n\n");
            for (Vocab v : fehler) {
                writer.write(v.getDeutsch() + "," + v.getAlbanisch() + "\n");
            }
            System.out.println("❗ Fehlerliste wurde gespeichert in 'fehlerliste.txt'");
        } catch (Exception e) {
            System.out.println("Fehler beim Schreiben der Fehlerliste: " + e.getMessage());
        }
    }

    public static void startFehlerQuiz(List<Vocab> vocabList, Scanner scanner) {
        List<Vocab> fehlerliste = VocabRepository.loadFehlerliste();

        if (fehlerliste.isEmpty()) {
            System.out.println("🎉 Keine Fehler vorhanden – gut gemacht!");
            return;
        }

        System.out.println("📋 Starte Fehler-Quiz...");
        int richtig = 0;
        int falsch = 0;

        for (Vocab vocab : fehlerliste) {
            System.out.print("Was heißt \"" + vocab.getDeutsch() + "\" auf Albanisch? > ");
            String antwort = scanner.nextLine().trim();

            if (antwort.equalsIgnoreCase(vocab.getAlbanisch())) {
                System.out.println("✅ Richtig!");
                richtig++;
            } else {
                System.out.println("❌ Falsch! Richtig wäre: " + vocab.getAlbanisch());
                falsch++;
            }
        }

        System.out.println("\n📊 Ergebnis des Fehler-Quiz:");
        System.out.println("Richtig: " + richtig);
        System.out.println("Falsch: " + falsch);
    }

    private static void searchVocab() {
        System.out.print("🔍 Suchbegriff (Deutsch oder Albanisch): ");
        String query = scanner.nextLine().toLowerCase();

        List<Vocab> treffer = new ArrayList<>();
        for (Vocab v : vocabList) {
            if (v.getDeutsch().toLowerCase().contains(query) || v.getAlbanisch().toLowerCase().contains(query)) {
                treffer.add(v);
            }
        }

        if (treffer.isEmpty()) {
            System.out.println("❌ Keine Vokabeln gefunden.");
        } else {
            System.out.println("✅ Gefundene Vokabeln:");
            for (Vocab v : treffer) {
                System.out.println("- " + v);
            }
        }
    }
}