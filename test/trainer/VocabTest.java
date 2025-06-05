package trainer;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class VocabTest {

    @Test
    public void testVocabInitialization() {
        Vocab v = new Vocab("Haus", "shtëpi");
        assertEquals("Haus", v.getDeutsch());
        assertEquals("shtëpi", v.getAlbanisch());
        assertEquals(0, v.getRepetitions());
        assertEquals(1, v.getInterval());
        assertEquals(2.5, v.getEaseFactor());
        assertEquals(LocalDate.now(), v.getNextReviewDate());
        assertEquals(0, v.getCorrectInErrorQuizCount());
    }

    @Test
    public void testToStringMethods() {
        Vocab v = new Vocab("Auto", "makinë");
        assertEquals("Auto → makinë", v.toString());
        assertTrue(v.toDetailedString().contains("Auto → makinë"));
        assertTrue(v.toDetailedString().contains("Nächste Wiederholung"));
    }

    @Test
    public void testUpdateAfterAnswer_CorrectAnswers() {
        Vocab v = new Vocab("Haus", "shtëpi");

        v.updateAfterAnswer(true);
        assertEquals(1, v.getRepetitions());
        assertEquals(1, v.getInterval());
        assertTrue(v.getEaseFactor() >= 2.5);
        assertEquals(LocalDate.now().plusDays(1), v.getNextReviewDate());

        v.updateAfterAnswer(true);
        assertEquals(2, v.getRepetitions());
        assertEquals(6, v.getInterval());
        assertTrue(v.getEaseFactor() > 2.5);
        assertEquals(LocalDate.now().plusDays(6), v.getNextReviewDate());

        double easeBefore = v.getEaseFactor();
        int intervalBefore = v.getInterval();
        v.updateAfterAnswer(true);
        assertEquals(3, v.getRepetitions());
        assertTrue(v.getInterval() >= (int) Math.round(intervalBefore * easeBefore));
        assertTrue(v.getEaseFactor() >= easeBefore);
        assertEquals(LocalDate.now().plusDays(v.getInterval()), v.getNextReviewDate());
    }

    @Test
    public void testUpdateAfterAnswer_IncorrectAnswerResets() {
        Vocab v = new Vocab("Haus", "shtëpi");

        v.updateAfterAnswer(true);
        v.updateAfterAnswer(true);
        assertTrue(v.getRepetitions() > 0);
        assertTrue(v.getInterval() > 0);
        assertTrue(v.getEaseFactor() >= 1.3);

        double easeBefore = v.getEaseFactor();

        v.updateAfterAnswer(false);
        assertEquals(0, v.getRepetitions());
        assertEquals(1, v.getInterval());
        assertTrue(v.getEaseFactor() <= easeBefore);
        assertTrue(v.getEaseFactor() >= 1.3);
        assertEquals(LocalDate.now().plusDays(1), v.getNextReviewDate());
    }

    @Test
    public void testEaseFactorNotBelowMinimum() {
        Vocab v = new Vocab("Haus", "shtëpi");
        for (int i = 0; i < 20; i++) {
            v.updateAfterAnswer(false);
        }
        assertTrue(v.getEaseFactor() >= 1.3);
    }

    @Test
    public void testErrorQuizCountIncrementAndReset() {
        Vocab v = new Vocab("Haus", "shtëpi");
        assertEquals(0, v.getCorrectInErrorQuizCount());

        v.incrementCorrectInErrorQuizCount();
        assertEquals(1, v.getCorrectInErrorQuizCount());

        v.incrementCorrectInErrorQuizCount();
        assertEquals(2, v.getCorrectInErrorQuizCount());

        v.resetCorrectInErrorQuizCount();
        assertEquals(0, v.getCorrectInErrorQuizCount());
    }
}