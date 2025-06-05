package trainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UIHelper {

    public static void printMenu() {
        System.out.println("\nMenü:");
        System.out.println("1. Vokabel hinzufügen");
        System.out.println("2. Vokabeln anzeigen");
        System.out.println("3. Vokabeln abfragen");
        System.out.println("4. Fehler-Quiz starten");
        System.out.println("5. Vokabel suchen");
        System.out.println("6. Statistiken anzeigen");
        System.out.println("7. Beenden");
        System.out.print("Auswahl: ");
    }

    public static String readInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static boolean checkAnswerAndPrint(String givenAnswer, String expectedAnswer, Vocab vocab, StatsManager stats, boolean isDeutschZuAlbanisch, List<Vocab> falschBeantwortet) {
        boolean exactCorrect;
        boolean relaxedCorrect;

        if (isDeutschZuAlbanisch) {
            exactCorrect = normalizeAnswer(givenAnswer).equals(normalizeAnswer(expectedAnswer));
            relaxedCorrect = removeAccents(normalizeAnswer(givenAnswer)).equals(removeAccents(normalizeAnswer(expectedAnswer)));
        } else {
            AnswerCheckResult result = checkAnswerMultiple(givenAnswer, expectedAnswer);
            exactCorrect = result.isCorrect;
            relaxedCorrect = false;

            if (exactCorrect) {
                printCorrectAnswerFeedback(result.otherPossibleAnswers);
                stats.addCorrect(vocab.getDeutsch().toLowerCase() + "->" + vocab.getAlbanisch().toLowerCase());
                return true;
            }
        }

        if (exactCorrect) {
            printCorrectAnswerFeedback(null);
            stats.addCorrect(vocab.getDeutsch().toLowerCase() + "->" + vocab.getAlbanisch().toLowerCase());
            return true;
        } else if (relaxedCorrect) {
            System.out.println("⚠️ Fast richtig, aber achte auf Sonderzeichen!");
            stats.addIncorrect(vocab.getDeutsch().toLowerCase() + "->" + vocab.getAlbanisch().toLowerCase());
            vocab.resetCorrectInErrorQuizCount();
            if (!falschBeantwortet.contains(vocab)) {
                falschBeantwortet.add(vocab);
            }
            return false;
        } else {
            printIncorrectAnswerFeedback(cleanAnswer(expectedAnswer));
            stats.addIncorrect(vocab.getDeutsch().toLowerCase() + "->" + vocab.getAlbanisch().toLowerCase());
            vocab.resetCorrectInErrorQuizCount();
            if (!falschBeantwortet.contains(vocab)) {
                falschBeantwortet.add(vocab);
            }
            return false;
        }
    }

    public static void printCorrectAnswerFeedback(List<String> otherAnswers) {
        System.out.println("✅ Richtig!");
        if (otherAnswers != null && !otherAnswers.isEmpty()) {
            System.out.println("ℹ️ Andere mögliche Antworten wären: " + String.join(", ", otherAnswers));
        }
    }

    public static void printIncorrectAnswerFeedback(String correctAnswer) {
        System.out.println("❌ Falsch! Richtig: " + correctAnswer);
    }

    public static AnswerCheckResult checkAnswerMultiple(String givenAnswer, String possibleAnswersCsv) {
        String[] possibleAnswers = possibleAnswersCsv.split(",");
        String normalizedGiven = normalizeAnswer(givenAnswer);

        boolean correct = Arrays.stream(possibleAnswers).map(UIHelper::normalizeAnswer).anyMatch(ans -> ans.equals(normalizedGiven));

        List<String> others = new ArrayList<>();
        if (correct) {
            others = Arrays.stream(possibleAnswers).map(String::trim).filter(ans -> !normalizeAnswer(ans).equals(normalizedGiven)).toList();
        }

        return new AnswerCheckResult(correct, others);
    }

    public static String cleanAnswer(String s) {
        if (s == null) return "";
        return s.replace("\"", "").trim();
    }

    public static String normalizeAnswer(String s) {
        return cleanAnswer(s).toLowerCase();
    }

    public static String removeAccents(String s) {
        if (s == null) return "";
        return java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }
}