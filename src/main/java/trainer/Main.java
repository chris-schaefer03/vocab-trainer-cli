package trainer;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StatsManager stats = StatsManager.load();
    private static final VocabService vocabService = new VocabService();
    private static final QuizManager quizManager = new QuizManager(vocabService.getAllVocabs(), stats);
    private static final UserService userService = new UserService();
    private static User currentUser;

    public static void main(String[] args) {
        runWithScanner(scanner);
    }

    public static void runWithScanner(Scanner scanner) {
        VocabRepository.ensureFehlerlisteExists();
        System.out.println("Willkommen zum Vokabeltrainer (Deutsch ↔ Albanisch)");

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("\n🔐 Bitte wählen:");
            System.out.println("1. Einloggen");
            System.out.println("2. Registrieren");
            System.out.print("Auswahl: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.print("Benutzername: ");
                    String username = scanner.nextLine();
                    System.out.print("Passwort: ");
                    String password = scanner.nextLine();
                    currentUser = userService.login(username, password);
                    if (currentUser != null) {
                        System.out.println("✅ Willkommen, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
                        loggedIn = true;
                    } else {
                        System.out.println("❌ Login fehlgeschlagen – bitte erneut versuchen.");
                    }
                }
                case "2" -> registerUser(scanner);
                default -> System.out.println("❗ Ungültige Eingabe.");
            }
        }

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
                case "8" -> {
                    if (currentUser != null && currentUser.isAdmin()) {
                        showAdminMenu(scanner);
                    } else {
                        System.out.println("🚫 Zugriff verweigert – nur Admins erlaubt.");
                    }
                }
                default -> System.out.println("❗ Ungültige Eingabe – bitte erneut versuchen.");
            }
        }
    }

    private static void addVocab(Scanner scanner, VocabService vocabService) {
        if (currentUser == null) {
            System.out.println("❗ Nicht eingeloggt.");
            return;
        }
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

    private static void registerUser(Scanner scanner) {
        System.out.println("👤 Neuen Benutzer registrieren");
        String username = UIHelper.readInput(scanner, "Benutzername: ");
        String password = UIHelper.readInput(scanner, "Passwort: ");
        String role = UIHelper.readInput(scanner, "Rolle (user/admin): ");

        boolean success = userService.register(username, password, role.toLowerCase());
        if (success) {
            System.out.println("✅ Benutzer erfolgreich registriert.");
        } else {
            System.out.println("❌ Benutzername existiert bereits.");
        }
    }

    private static void showAdminMenu(Scanner scanner) {
        boolean inAdminMenu = true;

        while (inAdminMenu) {
            System.out.println("\n🛠️ Admin-Menü:");
            System.out.println("1. Statistiken zurücksetzen");
            System.out.println("2. Benutzer anzeigen");
            System.out.println("3. Benutzer löschen");
            System.out.println("4. Neuen Benutzer registrieren");
            System.out.println("0. Zurück zum Hauptmenü");
            System.out.print("Auswahl: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    stats.reset();
                    stats.save();
                    System.out.println("🧹 Statistiken wurden zurückgesetzt.");
                }
                case "2" -> {
                    System.out.println("👥 Registrierte Benutzer:");
                    for (User u : userService.getAllUsers()) {
                        System.out.println("- " + u.getUsername() + " (" + u.getRole() + ")");
                    }
                }
                case "3" -> {
                    String userToDelete = UIHelper.readInput(scanner, "👤 Benutzername zum Löschen: ");
                    boolean success = userService.deleteUser(userToDelete, currentUser.getUsername());
                    if (success) {
                        System.out.println("🗑️ Benutzer gelöscht.");
                    } else {
                        System.out.println("❌ Benutzer konnte nicht gelöscht werden (nicht gefunden oder nicht erlaubt).");
                    }
                }
                case "4" -> registerUser(scanner);
                case "0" -> inAdminMenu = false;
                default -> System.out.println("❗ Ungültige Eingabe.");
            }
        }
    }
}