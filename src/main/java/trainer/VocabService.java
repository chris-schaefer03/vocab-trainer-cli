package trainer;

import java.util.List;
import java.util.stream.Collectors;

public class VocabService {
    private List<Vocab> vocabList;

    public VocabService() {
        vocabList = VocabRepository.load();
    }

    public void saveVocabs() {
        VocabRepository.save(vocabList);
    }

    public List<Vocab> getAllVocabs() {
        return List.copyOf(vocabList);
    }

    public boolean addVocab(String deutsch, String albanisch) {
        boolean exists = vocabList.stream().anyMatch(v -> v.getDeutsch().equalsIgnoreCase(deutsch) && v.getAlbanisch().equalsIgnoreCase(albanisch));
        if (exists) {
            return false;
        }
        vocabList.add(new Vocab(deutsch, albanisch));
        saveVocabs();
        return true;
    }

    public List<Vocab> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.copyOf(vocabList);
        }
        String lowerQuery = query.toLowerCase();
        return vocabList.stream().filter(v -> v.getDeutsch().toLowerCase().contains(lowerQuery) || v.getAlbanisch().toLowerCase().contains(lowerQuery)).collect(Collectors.toList());
    }
}