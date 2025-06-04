package trainer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VocabTest {

    @Test
    public void testVocabInitialization() {
        Vocab v = new Vocab("Haus", "Shtëpi");
        assertEquals("Haus", v.getDeutsch());
        assertEquals("Shtëpi", v.getAlbanisch());
    }

    @Test
    public void testVocabToString() {
        Vocab v = new Vocab("Auto", "Makinë");
        assertEquals("Auto → Makinë", v.toString());
    }
}