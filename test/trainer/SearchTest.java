package trainer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchTest {

    private List<Vocab> sampleVocabs = List.of(new Vocab("haus", "shtëpi"), new Vocab("auto", "makinë"), new Vocab("buch", "libër"), new Vocab("apfel", "mollë"));

    private List<Vocab> searchVocabs(List<Vocab> vocabs, String query) {
        if (query == null) return Collections.emptyList();

        String lowerQuery = query.trim().toLowerCase();
        if (lowerQuery.isEmpty()) return vocabs;

        return vocabs.stream().filter(v -> v.getDeutsch().toLowerCase().contains(lowerQuery) || v.getAlbanisch().toLowerCase().contains(lowerQuery)).collect(Collectors.toList());
    }

    @Test
    public void testSearchExactMatch() {
        List<Vocab> results = searchVocabs(sampleVocabs, "haus");
        assertEquals(1, results.size());
        assertEquals("haus", results.get(0).getDeutsch());
    }

    @Test
    public void testSearchPartialMatch() {
        List<Vocab> results = searchVocabs(sampleVocabs, "au");
        assertEquals(2, results.size()); // "haus" und "auto"
    }

    @Test
    public void testSearchNoMatch() {
        List<Vocab> results = searchVocabs(sampleVocabs, "xyz");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchCaseInsensitive() {
        List<Vocab> results = searchVocabs(sampleVocabs, "mOLLë");
        assertEquals(1, results.size());
        assertEquals("mollë", results.get(0).getAlbanisch());
    }

    @Test
    public void testSearchEmptyQuery() {
        List<Vocab> results = searchVocabs(sampleVocabs, "");
        assertEquals(sampleVocabs.size(), results.size());
    }

    @Test
    public void testSearchOnlySpaces() {
        List<Vocab> results = searchVocabs(sampleVocabs, "   ");
        assertEquals(sampleVocabs.size(), results.size());
    }

    @Test
    public void testSearchWithSpecialCharacters() {
        List<Vocab> results = searchVocabs(sampleVocabs, "shtëpi");
        assertEquals(1, results.size());
        assertEquals("shtëpi", results.get(0).getAlbanisch());
    }

    @Test
    public void testSearchNullQuery() {
        List<Vocab> results = searchVocabs(sampleVocabs, null);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchOnEmptyVocabList() {
        List<Vocab> results = searchVocabs(Collections.emptyList(), "haus");
        assertTrue(results.isEmpty());
    }
}