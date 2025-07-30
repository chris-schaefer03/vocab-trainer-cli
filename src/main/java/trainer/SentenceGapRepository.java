package trainer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

public class SentenceGapRepository {

    private static final String FILE_PATH = "data/sentence_gaps.json";

    public static Map<String, List<SentenceGap>> loadAll() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type type = new TypeToken<Map<String, List<SentenceGap>>>() {}.getType();
            return new Gson().fromJson(reader, type);
        } catch (Exception e) {
            System.out.println("❌ Fehler beim Laden der Satzübungen: " + e.getMessage());
            return new HashMap<>();
        }
    }
}