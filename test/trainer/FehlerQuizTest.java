package trainer;

import trainer.Vocab;
import trainer.VocabRepository;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FehlerQuizTest {

    @BeforeEach
    public void setup() {
        VocabRepository.setFilePath("vocab_data_test.json");

        File f = new File("vocab_data_test.json");
        if (f.exists()) f.delete();

    }

    @AfterEach
    public void cleanup() {
        File f = new File("vocab_data_test.json");
        if (f.exists()) f.delete();
    }

    private List<Vocab> fehlerliste;

    @BeforeEach
    public void initVocabList() {
        fehlerliste = new ArrayList<>();
        fehlerliste.add(new Vocab("Haus", "shtëpi"));
        fehlerliste.add(new Vocab("Baum", "pemë"));
    }

    @Test
    public void testCorrectAnswerIncrementsCountAndRemovesAfterThree() {
        Vocab vocab = fehlerliste.get(0);
        vocab.resetCorrectInErrorQuizCount();

        vocab.incrementCorrectInErrorQuizCount();
        assertEquals(1, vocab.getCorrectInErrorQuizCount());

        vocab.incrementCorrectInErrorQuizCount();
        assertEquals(2, vocab.getCorrectInErrorQuizCount());

        vocab.incrementCorrectInErrorQuizCount();
        assertEquals(3, vocab.getCorrectInErrorQuizCount());
    }

    @Test
    public void testResetCountOnWrongAnswer() {
        Vocab vocab = fehlerliste.get(0);
        vocab.incrementCorrectInErrorQuizCount();
        vocab.incrementCorrectInErrorQuizCount();
        assertTrue(vocab.getCorrectInErrorQuizCount() > 0);

        vocab.resetCorrectInErrorQuizCount();
        assertEquals(0, vocab.getCorrectInErrorQuizCount());
    }
}