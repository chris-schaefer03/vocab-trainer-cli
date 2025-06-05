package trainer;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UIHelperTest {

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    public void setup() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void teardown() {
        System.setOut(originalOut);
    }

    @Test
    public void testCleanAnswer() {
        assertEquals("", UIHelper.cleanAnswer(null));
        assertEquals("test", UIHelper.cleanAnswer("\"test\""));
        assertEquals("test", UIHelper.cleanAnswer(" test "));
    }

    @Test
    public void testNormalizeAnswer() {
        assertEquals("", UIHelper.normalizeAnswer(null));
        assertEquals("test", UIHelper.normalizeAnswer("\"Test\""));
        assertEquals("test", UIHelper.normalizeAnswer("  Test  "));
    }

    @Test
    public void testRemoveAccents() {
        assertEquals("shtepi", UIHelper.removeAccents("shtëpi"));
        assertEquals("makine", UIHelper.removeAccents("makinë"));
        assertEquals("", UIHelper.removeAccents(null));
    }

    @Test
    public void testCheckAnswerMultiple_CorrectAndOthers() {
        AnswerCheckResult result = UIHelper.checkAnswerMultiple("auto", "Auto, Wagen, Fahrzeug");
        assertTrue(result.isCorrect);
        assertTrue(result.otherPossibleAnswers.contains("Wagen"));
        assertTrue(result.otherPossibleAnswers.contains("Fahrzeug"));

        AnswerCheckResult wrongResult = UIHelper.checkAnswerMultiple("bus", "Auto, Wagen, Fahrzeug");
        assertFalse(wrongResult.isCorrect);
        assertTrue(wrongResult.otherPossibleAnswers.isEmpty());
    }

    @Test
    public void testPrintCorrectAnswerFeedback_WithAndWithoutOthers() {
        UIHelper.printCorrectAnswerFeedback(List.of("Antwort1", "Antwort2"));
        String output = outContent.toString();
        assertTrue(output.contains("✅ Richtig!"));
        assertTrue(output.contains("Andere mögliche Antworten wären: Antwort1, Antwort2"));

        outContent.reset();
        UIHelper.printCorrectAnswerFeedback(null);
        output = outContent.toString();
        assertTrue(output.contains("✅ Richtig!"));
        assertFalse(output.contains("Andere mögliche Antworten"));
    }

    @Test
    public void testPrintIncorrectAnswerFeedback() {
        UIHelper.printIncorrectAnswerFeedback("korrekt");
        String output = outContent.toString();
        assertTrue(output.contains("❌ Falsch! Richtig: korrekt"));
    }

    @Test
    public void testCheckAnswerAndPrint_ExactCorrect() {
        Vocab vocab = new Vocab("Haus", "shtëpi");
        StatsManager stats = new StatsManager();
        List<Vocab> falsch = new ArrayList<>();

        boolean result = UIHelper.checkAnswerAndPrint("shtëpi", "shtëpi", vocab, stats, true, falsch);
        assertTrue(result);
        assertTrue(outContent.toString().contains("✅ Richtig!"));
        assertEquals(1, stats.getTotalCorrect());
        assertTrue(falsch.isEmpty());
    }

    @Test
    public void testCheckAnswerAndPrint_RelaxedCorrect() {
        Vocab vocab = new Vocab("Haus", "shtëpi");
        StatsManager stats = new StatsManager();
        List<Vocab> falsch = new ArrayList<>();

        boolean result = UIHelper.checkAnswerAndPrint("shtepi", "shtëpi", vocab, stats, true, falsch);
        assertFalse(result);
        String output = outContent.toString();
        assertTrue(output.contains("⚠️ Fast richtig"));
        assertEquals(1, stats.getTotalIncorrect());
        assertTrue(falsch.contains(vocab));
    }

    @Test
    public void testCheckAnswerAndPrint_Incorrect() {
        Vocab vocab = new Vocab("Haus", "shtëpi");
        StatsManager stats = new StatsManager();
        List<Vocab> falsch = new ArrayList<>();

        boolean result = UIHelper.checkAnswerAndPrint("falsch", "shtëpi", vocab, stats, true, falsch);
        assertFalse(result);
        String output = outContent.toString();
        assertTrue(output.contains("❌ Falsch! Richtig: shtëpi"));
        assertEquals(1, stats.getTotalIncorrect());
        assertTrue(falsch.contains(vocab));
    }
}