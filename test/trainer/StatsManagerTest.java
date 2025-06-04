package trainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StatsManagerTest {

    private StatsManager stats;

    @BeforeEach
    public void setUp() {
        stats = new StatsManager();
    }

    @Test
    public void testInitialCounts() {
        assertEquals(0, stats.getCorrect());
        assertEquals(0, stats.getIncorrect());
        assertEquals(0, stats.getTotal());
        assertEquals(0.0, stats.getAccuracyPercent());
    }

    @Test
    public void testAddCorrect() {
        stats.addCorrect();
        assertEquals(1, stats.getCorrect());
        assertEquals(0, stats.getIncorrect());
        assertEquals(1, stats.getTotal());
        assertEquals(100.0, stats.getAccuracyPercent());
    }

    @Test
    public void testAddIncorrect() {
        stats.addIncorrect();
        assertEquals(0, stats.getCorrect());
        assertEquals(1, stats.getIncorrect());
        assertEquals(1, stats.getTotal());
        assertEquals(0.0, stats.getAccuracyPercent());
    }

    @Test
    public void testReset() {
        stats.addCorrect();
        stats.addIncorrect();
        stats.reset();
        assertEquals(0, stats.getCorrect());
        assertEquals(0, stats.getIncorrect());
        assertEquals(0, stats.getTotal());
        assertEquals(0.0, stats.getAccuracyPercent());
    }

    @Test
    public void testAccuracyCalculation() {
        stats.addCorrect();
        stats.addCorrect();
        stats.addIncorrect();
        double expectedAccuracy = (2.0 / 3.0) * 100;
        assertEquals(expectedAccuracy, stats.getAccuracyPercent(), 0.0001);
    }
}