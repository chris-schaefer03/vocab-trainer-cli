package trainer;

import java.time.LocalDate;
import java.util.*;

public class QuizManager {
    private final List<Vocab> vocabList;
    private final StatsManager stats;

    public QuizManager(List<Vocab> vocabList, StatsManager stats) {
        this.vocabList = vocabList;
        this.stats = stats;
    }

    public void startRegularQuiz(Scanner scanner) {
        List<Vocab> dueVocabs = getDueVocabs();

        if (dueVocabs.isEmpty()) {
            System.out.println("🎉 Keine fälligen Vokabeln heute. Du bist auf dem neuesten Stand!");
            return;
        }

        final int MAX_QUIZ_SIZE = 20;
        if (dueVocabs.size() > MAX_QUIZ_SIZE) {
            System.out.println("ℹ️ Es sind " + dueVocabs.size() + " Vokabeln fällig.");
            System.out.println("▶️ Es werden zufällig 20 davon abgefragt.\n");
            Collections.shuffle(dueVocabs);
            dueVocabs = dueVocabs.subList(0, MAX_QUIZ_SIZE);
        }

        boolean deutschZuAlbanisch = promptDirectionWithExitHint(scanner);

        stats.reset();

        List<Vocab> falschBeantwortet = new ArrayList<>();

        runQuizLoop(new ArrayList<>(dueVocabs), deutschZuAlbanisch, scanner, falschBeantwortet, true);

        if (!falschBeantwortet.isEmpty()) {
            System.out.println("\n❗ Falsch beantwortete Vokabeln:");
            falschBeantwortet.forEach(v -> System.out.println(v.getDeutsch() + " → " + v.getAlbanisch() + " (Nächste Wiederholung: " + v.getNextReviewDate() + ")"));
        }

        VocabRepository.saveFehlerliste(falschBeantwortet);
    }

    public void startErrorQuiz(Scanner scanner) {
        List<Vocab> fehlerliste = VocabRepository.loadFehlerliste();

        if (fehlerliste.isEmpty()) {
            System.out.println("🎉 Keine Fehler vorhanden – gut gemacht!");
            return;
        }

        boolean deutschZuAlbanisch = promptDirectionWithExitHint(scanner);

        System.out.println("📋 Starte Fehler-Quiz...");

        List<Vocab> falschBeantwortet = new ArrayList<>();

        runQuizLoop(fehlerliste, deutschZuAlbanisch, scanner, falschBeantwortet, true);

        if (!falschBeantwortet.isEmpty()) {
            System.out.println("\n❗ Falsch beantwortete Vokabeln:");
            falschBeantwortet.forEach(v -> System.out.println(v.getDeutsch() + " → " + v.getAlbanisch() + " (Nächste Wiederholung: " + v.getNextReviewDate() + ")"));
        }

        for (Vocab v : falschBeantwortet) {
            if (!fehlerliste.contains(v)) {
                fehlerliste.add(v);
            }
        }

        VocabRepository.saveFehlerliste(fehlerliste);
    }

    private boolean promptDirectionWithExitHint(Scanner scanner) {
        boolean direction = selectDirection(scanner);
        System.out.println("\n💡 Gib \"exit\" ein, um die Session vorzeitig zu beenden.\n");
        return direction;
    }

    boolean selectDirection(Scanner scanner) {
        while (true) {
            System.out.println("In welche Richtung möchtest du das Quiz machen?");
            System.out.println("1. Deutsch → Albanisch");
            System.out.println("2. Albanisch → Deutsch");
            System.out.print("Auswahl: ");
            String input = scanner.nextLine().trim();

            if (input.equals("1")) return true;
            if (input.equals("2")) return false;

            System.out.println("❗ Ungültige Eingabe – bitte '1' oder '2' eingeben.");
        }
    }

    private void printQuizResult(int richtig, int falsch) {
        System.out.println("\n📊 Ergebnis des Quiz:");
        System.out.println("Richtig: " + richtig);
        System.out.println("Falsch: " + falsch);
        if (stats.getTotal() > 0) {
            System.out.printf("Trefferquote: %.2f %%\n", stats.getAccuracyPercent());
        }
    }

    void runQuizLoop(List<Vocab> quizVocabs, boolean deutschZuAlbanisch, Scanner scanner, List<Vocab> falschBeantwortet, boolean printResult) {
        int richtig = 0;
        int falsch = 0;

        Iterator<Vocab> iterator = quizVocabs.iterator();
        while (iterator.hasNext()) {
            Vocab vocab = iterator.next();

            String question = deutschZuAlbanisch ? vocab.getDeutsch() : vocab.getAlbanisch();
            String expected = deutschZuAlbanisch ? vocab.getAlbanisch() : vocab.getDeutsch();

            System.out.print("Was heißt \"" + question + "\" auf " + (deutschZuAlbanisch ? "Albanisch" : "Deutsch") + "? > ");
            String answer = scanner.nextLine().trim();

            if (answer.equalsIgnoreCase("exit")) {
                System.out.println("⏹️ Quiz abgebrochen. Fortschritt wird gespeichert.");
                break;
            }

            boolean correct = UIHelper.checkAnswerAndPrint(answer, expected, vocab, stats, deutschZuAlbanisch, falschBeantwortet);

            if (correct) {
                richtig++;
                vocab.incrementCorrectInErrorQuizCount();
                if (vocab.getCorrectInErrorQuizCount() >= 3) {
                    iterator.remove();
                }
            } else {
                falsch++;
                vocab.resetCorrectInErrorQuizCount();
            }
        }

        if (printResult) {
            printQuizResult(richtig, falsch);
        }
    }

    private List<Vocab> getDueVocabs() {
        List<Vocab> due = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Vocab v : vocabList) {
            if (!today.isBefore(v.getNextReviewDate())) {
                due.add(v);
            }
        }
        return due;
    }
}