package trainer;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VocabRepositoryTest {

    private static final String TEST_VOKABELN = "test_vokabeln.csv";
    private static final String TEST_VOCAB_DATA = "test_vocab_data.json";
    private static final String TEST_FEHLERLISTE = "test_fehlerliste.txt";

    @BeforeEach
    public void setup() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_VOKABELN))) {
            writer.write("shtëpi,haus\n");
            writer.write("makinë,auto\n");
        }
        Files.copy(Paths.get(TEST_VOKABELN), Paths.get("vokabeln.csv"), StandardCopyOption.REPLACE_EXISTING);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_VOCAB_DATA))) {
            writer.write("[]");
        }
        Files.deleteIfExists(Paths.get(TEST_FEHLERLISTE));
    }

    @AfterEach
    public void teardown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_VOKABELN));
        Files.deleteIfExists(Paths.get(TEST_VOCAB_DATA));
        Files.deleteIfExists(Paths.get(TEST_FEHLERLISTE));
        Files.deleteIfExists(Paths.get("vokabeln.csv"));
        Files.deleteIfExists(Paths.get("fehlerliste.txt"));
    }

    @Test
    public void testLoadDefaultVocabs() throws IOException {
        List<Vocab> vocabs = VocabRepository.getDefaultVocabs();

        assertEquals(2, vocabs.size());

        assertEquals("haus", vocabs.get(0).getDeutsch());
        assertEquals("shtëpi", vocabs.get(0).getAlbanisch());

        assertEquals("auto", vocabs.get(1).getDeutsch());
        assertEquals("makinë", vocabs.get(1).getAlbanisch());
    }

    @Test
    public void testSaveAndLoad() {
        List<Vocab> vocabs = List.of(
                new Vocab("haus", "shtëpi"),
                new Vocab("auto", "makinë")
        );

        VocabRepository.save(vocabs);
        List<Vocab> loaded = VocabRepository.load();

        assertEquals(2, loaded.size());
        assertEquals("haus", loaded.get(0).getDeutsch());
        assertEquals("shtëpi", loaded.get(0).getAlbanisch());
    }

    @Test
    public void testLoadFehlerliste() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("fehlerliste.txt"))) {
            writer.write("haus,shtëpi\nauto,makinë\n");
        }

        List<Vocab> fehler = VocabRepository.loadFehlerliste();
        assertEquals(2, fehler.size());
    }

    @Test
    public void testLoadMalformedVocabDataFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_VOCAB_DATA))) {
            writer.write("INVALID_JSON");
        }

        List<Vocab> vocabs = VocabRepository.load();

        assertFalse(vocabs.isEmpty(), "Standardliste sollte geladen werden, Liste darf nicht leer sein");
        assertEquals("haus", vocabs.get(0).getDeutsch());
    }

    @Test
    public void testLoadNonExistingVocabDataFile() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_VOCAB_DATA));

        List<Vocab> vocabs = VocabRepository.load();

        assertFalse(vocabs.isEmpty(), "Standardliste sollte geladen werden, Liste darf nicht leer sein");
        assertEquals("haus", vocabs.get(0).getDeutsch());
    }
}