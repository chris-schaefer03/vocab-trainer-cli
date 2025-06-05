package trainer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class QuizLogicTest {

    private List<Vocab> sampleVocabs = List.of(new Vocab("haus", "shtëpi"), new Vocab("auto", "makinë"));

    private List<Vocab> quiz(List<Vocab> vocabs, int modus, List<String> answers) {
        List<Vocab> falsch = new ArrayList<>();
        Random random = new Random(0);

        for (int i = 0; i < vocabs.size(); i++) {
            Vocab v = vocabs.get(i);

            boolean direction;
            if (modus == 1) {
                direction = true;
            } else if (modus == 2) {
                direction = false;
            } else {
                direction = random.nextBoolean();
            }

            String expected = direction ? v.getAlbanisch() : v.getDeutsch();
            String userAnswer = answers.get(i);

            if (!userAnswer.equalsIgnoreCase(expected)) {
                falsch.add(v);
            }
        }

        return falsch;
    }

    @Test
    public void testAllCorrect() {
        List<String> answers = List.of("shtëpi", "makinë");
        List<Vocab> falsch = quiz(sampleVocabs, 1, answers);
        assertTrue(falsch.isEmpty(), "Alle Antworten sollten korrekt sein.");
    }

    @Test
    public void testOneWrong() {
        List<String> answers = List.of("shtëpi", "auto");
        List<Vocab> falsch = quiz(sampleVocabs, 1, answers);
        assertEquals(1, falsch.size(), "Eine Antwort sollte falsch sein.");
        assertEquals("auto", falsch.get(0).getDeutsch(), "Falsche Vokabel sollte 'auto' sein.");
    }

    @Test
    public void testRandomMode() {
        List<String> answers = List.of("haus", "makinë");
        List<Vocab> falsch = quiz(sampleVocabs, 3, answers);
        assertFalse(falsch.isEmpty(), "Im Zufallsmodus sollten Fehler möglich sein.");
    }

    @Test
    public void testEmptyVocabList() {
        List<Vocab> emptyList = Collections.emptyList();
        List<String> answers = Collections.emptyList();
        List<Vocab> falsch = quiz(emptyList, 1, answers);
        assertTrue(falsch.isEmpty(), "Keine Vokabeln, kein Fehler.");
    }

    @Test
    public void testEmptyAnswersList() {
        List<String> emptyAnswers = Collections.emptyList();
        assertThrows(IndexOutOfBoundsException.class, () -> quiz(sampleVocabs, 1, emptyAnswers));
    }

    @Test
    public void testNullAnswersList() {
        assertThrows(NullPointerException.class, () -> quiz(sampleVocabs, 1, null));
    }

    @Test
    public void testAnswersListShorterThanVocabs() {
        List<String> shortAnswers = List.of("shtëpi");
        assertThrows(IndexOutOfBoundsException.class, () -> quiz(sampleVocabs, 1, shortAnswers));
    }

    @Test
    public void testAnswersListLongerThanVocabs() {
        List<String> longAnswers = List.of("shtëpi", "makinë", "extra");
        List<Vocab> falsch = quiz(sampleVocabs, 1, longAnswers);
        assertTrue(falsch.isEmpty(), "Extra Antworten sollten ignoriert werden und keine Fehler erzeugen.");
    }
}