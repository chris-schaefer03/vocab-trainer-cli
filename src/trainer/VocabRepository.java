package trainer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VocabRepository {
    private static final String FILE_PATH = "vocab_data.json";
    private static final Gson gson = new Gson();

    public static void save(List<Vocab> vocabList) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(vocabList, writer);
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern: " + e.getMessage());
        }
    }

    public static List<Vocab> load() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Vocab>>() {
            }.getType();
            List<Vocab> list = gson.fromJson(reader, listType);
            return list != null ? list : getDefaultVocabs();
        } catch (IOException e) {
            System.out.println("⚠️ Keine gespeicherten Vokabeln gefunden – lade Standardliste.");
            return getDefaultVocabs();
        } catch (JsonSyntaxException e) {
            System.out.println("⚠️ Ungültiges JSON in '" + FILE_PATH + "' – lade Standardliste.");
            return getDefaultVocabs();
        }
    }

    public static List<Vocab> getDefaultVocabs() {
        List<Vocab> list = new ArrayList<>();

        try (Scanner scanner = new Scanner(new java.io.File("vokabeln.csv"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String albanisch = parts[0].trim();
                    String deutsch = parts[1].trim();
                    list.add(new Vocab(deutsch, albanisch));
                }
            }
            System.out.println("📥 Standard-Vokabeln aus 'vokabeln.csv' geladen.");
        } catch (Exception e) {
            System.out.println("⚠️ Fehler beim Laden von 'vokabeln.csv': " + e.getMessage());
        }

        return list;
    }

    public static List<Vocab> loadFehlerliste() {
        List<Vocab> fehlerliste = new ArrayList<>();

        try (Scanner scanner = new Scanner(new java.io.File("fehlerliste.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(",")) {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        String deutsch = parts[0].trim();
                        String albanisch = parts[1].trim();
                        fehlerliste.add(new Vocab(deutsch, albanisch));
                    }
                }
            }
            System.out.println("📂 Fehlerliste geladen.");
        } catch (IOException e) {
            System.out.println("⚠️ Fehler beim Laden der Fehlerliste: " + e.getMessage());
        }

        return fehlerliste;
    }
}