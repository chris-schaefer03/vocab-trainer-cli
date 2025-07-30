package trainer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VocabRepository {
    private static String filePath = "data/vocab_data.json";
    private static String fehlerlistePath = "data/fehlerliste.json";

    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).setPrettyPrinting().create();

    public static void setFilePath(String path) {
        filePath = path;
    }

    public static void setFehlerlistePath(String path) {
        fehlerlistePath = path;
    }

    public static void save(List<Vocab> vocabList) {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(vocabList, writer);
        } catch (IOException e) {
            System.out.println("❌ Fehler beim Speichern: " + e.getMessage());
        }
    }

    public static List<Vocab> load() {
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<List<Vocab>>() {
            }.getType();
            List<Vocab> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("📁 Keine gespeicherten Vokabeln gefunden. Starte mit leerer Liste.");
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println("❌ Fehler beim Laden der Vokabeln: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveFehlerliste(List<Vocab> fehlerliste) {
        try (FileWriter writer = new FileWriter(fehlerlistePath)) {
            gson.toJson(fehlerliste, writer);
        } catch (IOException e) {
            System.out.println("❌ Fehler beim Speichern der Fehlerliste: " + e.getMessage());
        }
    }

    public static List<Vocab> loadFehlerliste() {
        try (FileReader reader = new FileReader(fehlerlistePath)) {
            Type listType = new TypeToken<List<Vocab>>() {
            }.getType();
            List<Vocab> fehlerliste = gson.fromJson(reader, listType);
            return fehlerliste != null ? fehlerliste : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("⚠️ Fehler beim Laden der Fehlerliste: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void ensureFehlerlisteExists() {
        File fehlerdatei = new File(fehlerlistePath);
        if (!fehlerdatei.exists()) {
            try (FileWriter writer = new FileWriter(fehlerdatei)) {
                writer.write("[]");
                System.out.println("⚡ Fehlerliste-JSON wurde initial angelegt.");
            } catch (IOException e) {
                System.out.println("❌ Fehler beim Anlegen der Fehlerliste: " + e.getMessage());
            }
        }
    }
}