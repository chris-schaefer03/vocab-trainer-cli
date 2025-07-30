package trainer;

import trainer.Vocab;
import trainer.VocabRepository;
import trainer.VocabService;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VocabServiceTest {

    private VocabService vocabService;

    @BeforeEach
    public void setUp() {
        VocabRepository.setFilePath("vocab_data_test.json");
        File f = new File("vocab_data_test.json");
        if (f.exists()) f.delete();

        vocabService = new VocabService();
    }

    @AfterEach
    public void tearDown() {
        File f = new File("vocab_data_test.json");
        if (f.exists()) f.delete();
    }

    @Test
    public void testAddVocab() {
        boolean added = vocabService.addVocab("Testdeutsch", "Testalbanisch");
        assertTrue(added, "Vokabel sollte hinzugefügt werden");

        List<Vocab> allVocabs = vocabService.getAllVocabs();
        assertTrue(allVocabs.stream().anyMatch(v -> v.getDeutsch().equalsIgnoreCase("Testdeutsch") && v.getAlbanisch().equalsIgnoreCase("Testalbanisch")));
    }

    @Test
    public void testAddDuplicateVocab() {
        vocabService.addVocab("Haus", "shtëpi");
        boolean addedAgain = vocabService.addVocab("Haus", "shtëpi");
        assertFalse(addedAgain, "Duplikate sollten nicht hinzugefügt werden");
    }

    @Test
    public void testSearchExactMatch() {
        vocabService.addVocab("Haus", "shtëpi");
        List<Vocab> results = vocabService.search("Haus");
        assertEquals(1, results.size());
        assertEquals("Haus", results.get(0).getDeutsch());
    }

    @Test
    public void testSearchPartialMatch() {
        vocabService.addVocab("Haus", "shtëpi");
        vocabService.addVocab("Auto", "makinë");
        List<Vocab> results = vocabService.search("au");
        assertTrue(results.size() >= 2);
        assertTrue(results.stream().anyMatch(v -> v.getDeutsch().equalsIgnoreCase("Haus")));
        assertTrue(results.stream().anyMatch(v -> v.getDeutsch().equalsIgnoreCase("Auto")));
    }

    @Test
    public void testSearchNoMatch() {
        vocabService.addVocab("Haus", "shtëpi");
        List<Vocab> results = vocabService.search("xyz");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchCaseInsensitive() {
        vocabService.addVocab("Apfel", "mollë");
        List<Vocab> results = vocabService.search("mOLLë");
        assertEquals(1, results.size());
        assertEquals("mollë", results.get(0).getAlbanisch());
    }

    @Test
    public void testSearchEmptyQueryReturnsAll() {
        vocabService.addVocab("Haus", "shtëpi");
        vocabService.addVocab("Baum", "pemë");

        List<Vocab> results = vocabService.search("");
        assertEquals(vocabService.getAllVocabs().size(), results.size());
    }

    @Test
    public void testSearchOnlySpacesReturnsAll() {
        vocabService.addVocab("Haus", "shtëpi");
        vocabService.addVocab("Baum", "pemë");

        List<Vocab> results = vocabService.search("   ");
        assertEquals(vocabService.getAllVocabs().size(), results.size());
    }

    @Test
    public void testSearchNullQueryReturnsAll() {
        vocabService.addVocab("Haus", "shtëpi");
        vocabService.addVocab("Baum", "pemë");

        List<Vocab> results = vocabService.search(null);
        assertEquals(vocabService.getAllVocabs().size(), results.size());
    }

    @Test
    public void testSearchWithSpecialCharacters() {
        vocabService.addVocab("Haus", "shtëpi");
        List<Vocab> results = vocabService.search("shtëpi");
        assertEquals(1, results.size());
        assertEquals("shtëpi", results.get(0).getAlbanisch());
    }

    @Test
    public void testSearchOnEmptyVocabList() {
        List<Vocab> results = vocabService.search("haus");
        assertTrue(results.isEmpty());
    }
}