package trainer;

import com.google.gson.*;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class StatsManager {
    private int totalCorrect = 0;
    private int totalIncorrect = 0;

    private Map<String, Integer> wrongCountPerVocab = new HashMap<>();

    private Map<LocalDate, Integer> correctPerDay = new HashMap<>();

    private static final String STATS_FILE = "data/stats.json";

    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).setPrettyPrinting().create();

    public void addCorrect(String vocabKey) {
        totalCorrect++;
        LocalDate today = LocalDate.now();
        correctPerDay.put(today, correctPerDay.getOrDefault(today, 0) + 1);
    }

    public void addIncorrect(String vocabKey) {
        totalIncorrect++;
        wrongCountPerVocab.put(vocabKey, wrongCountPerVocab.getOrDefault(vocabKey, 0) + 1);
    }

    public int getTotalCorrect() {
        return totalCorrect;
    }

    public int getTotalIncorrect() {
        return totalIncorrect;
    }

    public int getTotal() {
        return totalCorrect + totalIncorrect;
    }

    public double getAccuracyPercent() {
        int total = getTotal();
        return total == 0 ? 0.0 : (totalCorrect * 100.0) / total;
    }

    public Map<String, Integer> getWrongCountPerVocab() {
        return wrongCountPerVocab;
    }

    public Map<LocalDate, Integer> getCorrectPerDay() {
        return correctPerDay;
    }

    public void reset() {
        totalCorrect = 0;
        totalIncorrect = 0;
        wrongCountPerVocab.clear();
        correctPerDay.clear();
    }

    public void save() {
        try (Writer writer = new FileWriter(STATS_FILE)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Statistiken: " + e.getMessage());
        }
    }

    public static StatsManager load() {
        try (Reader reader = new FileReader(STATS_FILE)) {
            StatsManager stats = gson.fromJson(reader, StatsManager.class);
            return stats != null ? stats : new StatsManager();
        } catch (IOException e) {
            return new StatsManager();
        }
    }
}
