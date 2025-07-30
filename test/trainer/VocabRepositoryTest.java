package trainer;

import trainer.Vocab;
import trainer.VocabRepository;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VocabRepositoryTest {

    private static final String TEST_VOCAB_DATA = "vocab_data_test.json";
    private static final String TEST_FEHLERLISTE = "fehlerliste_test.json";

    @BeforeEach
    public void setup() throws IOException {
        VocabRepository.setFilePath(TEST_VOCAB_DATA);
        VocabRepository.setFehlerlistePath(TEST_FEHLERLISTE);

        Files.deleteIfExists(Paths.get(TEST_VOCAB_DATA));
        Files.deleteIfExists(Paths.get(TEST_FEHLERLISTE));
    }

    @AfterEach
    public void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_VOCAB_DATA));
        Files.deleteIfExists(Paths.get(TEST_FEHLERLISTE));
    }

    @Test
    public void testSaveAndLoadVocabs() {
        List<Vocab> vocabs = List.of(new Vocab("Haus", "shtëpi"), new Vocab("Auto", "makinë"));

        VocabRepository.save(vocabs);
        List<Vocab> loaded = VocabRepository.load();

        assertEquals(2, loaded.size());
        assertEquals("Haus", loaded.get(0).getDeutsch());
        assertEquals("shtëpi", loaded.get(0).getAlbanisch());
    }

    @Test
    public void testSaveAndLoadFehlerliste() {
        List<Vocab> fehlerliste = List.of(new Vocab("Haus", "shtëpi"), new Vocab("Baum", "pemë"));

        VocabRepository.saveFehlerliste(fehlerliste);
        List<Vocab> loaded = VocabRepository.loadFehlerliste();

        assertEquals(2, loaded.size());
        assertEquals("Haus", loaded.get(0).getDeutsch());
        assertEquals("pemë", loaded.get(1).getAlbanisch());
    }

    @Test
    public void testLoadNonExistingVocabsFileReturnsEmptyList() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_VOCAB_DATA));
        List<Vocab> loaded = VocabRepository.load();

        assertNotNull(loaded);
        assertTrue(loaded.isEmpty());
    }

    @Test
    public void testLoadNonExistingFehlerlisteReturnsEmptyList() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FEHLERLISTE));
        List<Vocab> loaded = VocabRepository.loadFehlerliste();

        assertNotNull(loaded);
        assertTrue(loaded.isEmpty());
    }

    @Test
    public void testEnsureFehlerlisteExistsCreatesFile() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FEHLERLISTE));
        VocabRepository.ensureFehlerlisteExists();

        File file = new File(TEST_FEHLERLISTE);
        assertTrue(file.exists(), "Fehlerliste Datei sollte existieren.");
    }
}