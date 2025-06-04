package trainer;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @BeforeEach
    public void setup() throws IOException {
        Files.deleteIfExists(Paths.get("vocab_data.json"));
        Files.deleteIfExists(Paths.get("fehlerliste.txt"));
    }

    @AfterEach
    public void teardown() throws IOException {
        Files.deleteIfExists(Paths.get("vocab_data.json"));
        Files.deleteIfExists(Paths.get("fehlerliste.txt"));
    }

    @Test
    public void testAddVocab() {
        List<Vocab> vocabList = VocabRepository.load();
        int originalSize = vocabList.size();
        Vocab newVocab = new Vocab("testdeutsch", "testalbanisch");
        vocabList.add(newVocab);
        VocabRepository.save(vocabList);
        List<Vocab> loaded = VocabRepository.load();
        assertEquals(originalSize + 1, loaded.size());
        assertTrue(loaded.stream().anyMatch(v -> v.getDeutsch().equals("testdeutsch") && v.getAlbanisch().equals("testalbanisch")));
    }
}