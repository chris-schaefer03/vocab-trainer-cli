package trainer;

import trainer.StatsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StatsManagerTest {

    private StatsManager stats;

    @BeforeEach
    public void setUp() {
        stats = new StatsManager();
    }

    @Test
    public void testInitialCounts() {
        assertEquals(0, stats.getTotalCorrect());
        assertEquals(0, stats.getTotalIncorrect());
        assertEquals(0, stats.getTotal());
        assertEquals(0.0, stats.getAccuracyPercent());
        assertTrue(stats.getWrongCountPerVocab().isEmpty());
        assertTrue(stats.getCorrectPerDay().isEmpty());
    }

    @Test
    public void testAddCorrect() {
        stats.addCorrect("Haus->shtëpi");
        assertEquals(1, stats.getTotalCorrect());
        assertEquals(0, stats.getTotalIncorrect());
        assertEquals(1, stats.getTotal());
        assertEquals(100.0, stats.getAccuracyPercent());

        Map<LocalDate, Integer> correctPerDay = stats.getCorrectPerDay();
        assertTrue(correctPerDay.containsKey(LocalDate.now()));
        assertEquals(1, correctPerDay.get(LocalDate.now()).intValue());
    }

    @Test
    public void testAddIncorrect() {
        stats.addIncorrect("Haus->shtëpi");
        assertEquals(0, stats.getTotalCorrect());
        assertEquals(1, stats.getTotalIncorrect());
        assertEquals(1, stats.getTotal());
        assertEquals(0.0, stats.getAccuracyPercent());

        Map<String, Integer> wrongCount = stats.getWrongCountPerVocab();
        assertTrue(wrongCount.containsKey("Haus->shtëpi"));
        assertEquals(1, wrongCount.get("Haus->shtëpi").intValue());
    }

    @Test
    public void testMultipleAddIncorrect() {
        stats.addIncorrect("Haus->shtëpi");
        stats.addIncorrect("Haus->shtëpi");
        stats.addIncorrect("Baum->pemë");

        Map<String, Integer> wrongCount = stats.getWrongCountPerVocab();
        assertEquals(2, wrongCount.get("Haus->shtëpi").intValue());
        assertEquals(1, wrongCount.get("Baum->pemë").intValue());
    }

    @Test
    public void testReset() {
        stats.addCorrect("Haus->shtëpi");
        stats.addIncorrect("Baum->pemë");
        stats.reset();

        assertEquals(0, stats.getTotalCorrect());
        assertEquals(0, stats.getTotalIncorrect());
        assertEquals(0, stats.getTotal());
        assertEquals(0.0, stats.getAccuracyPercent());
        assertTrue(stats.getWrongCountPerVocab().isEmpty());
        assertTrue(stats.getCorrectPerDay().isEmpty());
    }

    @Test
    public void testAccuracyCalculation() {
        stats.addCorrect("Haus->shtëpi");
        stats.addCorrect("Haus->shtëpi");
        stats.addIncorrect("Baum->pemë");

        double expectedAccuracy = (2.0 / 3.0) * 100;
        assertEquals(expectedAccuracy, stats.getAccuracyPercent(), 0.0001);
    }
}