package trainer;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class QuizManagerTest {

    private List<Vocab> vocabList;
    private StatsManager stats;
    private QuizManager quizManager;

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    public void setup() {
        vocabList = new ArrayList<>();
        vocabList.add(new Vocab("Haus", "shtëpi"));
        vocabList.add(new Vocab("Baum", "pemë"));

        stats = new StatsManager();
        quizManager = new QuizManager(vocabList, stats);

        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void teardown() {
        System.setOut(originalOut);
    }

    @Test
    public void testStartRegularQuiz_AllCorrect() {
        String input = "1\n" + vocabList.get(0).getAlbanisch() + "\n" + vocabList.get(1).getAlbanisch() + "\nexit\n";
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))) {
            quizManager.startRegularQuiz(scanner);
        }

        String output = outContent.toString();
        assertTrue(output.contains("✅ Richtig!"));
        assertEquals(vocabList.size(), stats.getTotalCorrect());
        assertEquals(0, stats.getTotalIncorrect());
    }

    @Test
    public void testStartRegularQuiz_WithWrongAnswers() {
        String input = "1\nfalsch\nfalsch\nexit\n";
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))) {
            quizManager.startRegularQuiz(scanner);
        }

        String output = outContent.toString();
        assertTrue(output.contains("❌ Falsch!"));
        assertEquals(0, stats.getTotalCorrect());
        assertTrue(stats.getTotalIncorrect() > 0);
    }

    @Test
    public void testStartErrorQuiz_AllCorrect_RemoveVocabAfter3() {
        Vocab vocab = vocabList.get(0);
        vocab.resetCorrectInErrorQuizCount();
        vocab.incrementCorrectInErrorQuizCount();
        vocab.incrementCorrectInErrorQuizCount();

        List<Vocab> errorList = new ArrayList<>(List.of(vocab));
        VocabRepository.saveFehlerliste(errorList);

        String input = "1\n" + vocab.getAlbanisch() + "\n" + vocab.getAlbanisch() + "\n" + vocab.getAlbanisch() + "\nexit\n";

        try (Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))) {
            quizManager.startErrorQuiz(scanner);
        }

        List<Vocab> updatedErrorList = VocabRepository.loadFehlerliste();

        assertFalse(updatedErrorList.contains(vocab), "Vocab sollte nach 3 korrekten Antworten aus Fehlerliste entfernt sein");
    }

    @Test
    public void testStartErrorQuiz_WithWrongAnswer_ResetsCount() {
        Vocab vocab = vocabList.get(0);
        vocab.incrementCorrectInErrorQuizCount(); // count=1

        List<Vocab> errorList = new ArrayList<>(List.of(vocab));
        VocabRepository.saveFehlerliste(errorList);

        String input = "1\nfalscheAntwort\nexit\n";
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))) {
            quizManager.startErrorQuiz(scanner);
        }

        List<Vocab> updatedErrorList = VocabRepository.loadFehlerliste();

        Vocab updatedVocab = updatedErrorList.stream().filter(v -> v.getDeutsch().equals(vocab.getDeutsch()) && v.getAlbanisch().equals(vocab.getAlbanisch())).findFirst().orElseThrow(() -> new AssertionError("Vocab nicht in Fehlerliste gefunden"));

        assertEquals(0, updatedVocab.getCorrectInErrorQuizCount(), "Count sollte bei falscher Antwort zurückgesetzt werden");
    }

    @Test
    public void testStartErrorQuiz_EmptyErrorList() {
        VocabRepository.saveFehlerliste(new ArrayList<>());

        try (Scanner scanner = new Scanner(new ByteArrayInputStream("1\n".getBytes(StandardCharsets.UTF_8)))) {
            quizManager.startErrorQuiz(scanner);
        }
        String output = outContent.toString();
        assertTrue(output.contains("Keine Fehler vorhanden"));
    }

    @Test
    public void testSelectDirectionWithInvalidInputs() {
        String input = "abc\n3\n1\n";
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))) {
            QuizManager quizManager = new QuizManager(List.of(), new StatsManager());
            boolean direction = quizManager.selectDirection(scanner);
            assertTrue(direction);
        }
    }
}