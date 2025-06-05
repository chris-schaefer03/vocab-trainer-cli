package trainer;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StatsManager stats = StatsManager.load();
    private static final VocabService vocabService = new VocabService();
    private static final QuizManager quizManager = new QuizManager(vocabService.getAllVocabs(), stats);

    public static void main(String[] args) {
        runWithScanner(scanner);
    }

    public static void runWithScanner(Scanner scanner) {
        VocabRepository.ensureFehlerlisteExists();
        System.out.println("Willkommen zum Vokabeltrainer (Deutsch ↔ Albanisch)");

        StatsManager stats = StatsManager.load();
        VocabService vocabService = new VocabService();
        QuizManager quizManager = new QuizManager(vocabService.getAllVocabs(), stats);

        boolean running = true;
        while (running) {
            UIHelper.printMenu();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> addVocab(scanner, vocabService);
                case "2" -> listVocabs(vocabService);
                case "3" -> quizManager.startRegularQuiz(scanner);
                case "4" -> quizManager.startErrorQuiz(scanner);
                case "5" -> searchVocabs(scanner, vocabService);
                case "6" -> showStats(stats);
                case "7" -> {
                    vocabService.saveVocabs();
                    stats.save();
                    System.out.println("📦 Vokabeln und Statistiken gespeichert. Programm beendet.");
                    running = false;
                }
                default -> System.out.println("❗ Ungültige Eingabe – bitte erneut versuchen.");
            }
        }
    }

    private static void addVocab(Scanner scanner, VocabService vocabService) {
        String deutsch = UIHelper.readInput(scanner, "Deutsch: ");
        String albanisch = UIHelper.readInput(scanner, "Albanisch: ");

        boolean added = vocabService.addVocab(deutsch, albanisch);
        if (added) {
            System.out.println("✅ Vokabel gespeichert!");
        } else {
            System.out.println("⚠️ Diese Vokabel existiert bereits.");
        }
    }

    private static void listVocabs(VocabService vocabService) {
        List<Vocab> vocabs = vocabService.getAllVocabs();
        if (vocabs.isEmpty()) {
            System.out.println("Noch keine Vokabeln gespeichert.");
        } else {
            System.out.println("Gespeicherte Vokabeln:");
            vocabs.forEach(System.out::println);
        }
    }

    private static void searchVocabs(Scanner scanner, VocabService vocabService) {
        String query = UIHelper.readInput(scanner, "🔍 Suchbegriff (Deutsch oder Albanisch): ").toLowerCase();
        List<Vocab> results = vocabService.search(query);

        if (results.isEmpty()) {
            System.out.println("❌ Keine Vokabeln gefunden.");
        } else {
            System.out.println("✅ Gefundene Vokabeln:");
            results.forEach(v -> System.out.println("- " + v));
        }
    }

    private static void showStats(StatsManager stats) {
        System.out.println("📊 Gesamtstatistiken:");
        System.out.println("Richtig beantwortet: " + stats.getTotalCorrect());
        System.out.println("Falsch beantwortet: " + stats.getTotalIncorrect());
        System.out.printf("Trefferquote: %.2f %%\n", stats.getAccuracyPercent());

        System.out.println("\n🔍 Häufigste Fehler:");
        stats.getWrongCountPerVocab().entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).limit(5).forEach(e -> System.out.println(e.getKey() + ": " + e.getValue() + " Fehler"));

        System.out.println("\n📅 Korrekte Antworten pro Tag:");
        stats.getCorrectPerDay().entrySet().stream().sorted(java.util.Map.Entry.comparingByKey()).forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }
}