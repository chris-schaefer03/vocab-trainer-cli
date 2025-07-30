package java.trainer;

import trainer.Main;
import trainer.VocabRepository;
import trainer.VocabService;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private File testVocabFile = new File("vocab_data_test.json");
    private File testStatsFile = new File("stats.json");

    @BeforeEach
    public void setup() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        VocabRepository.setFilePath("vocab_data_test.json");
        if (testVocabFile.exists()) testVocabFile.delete();
        if (testStatsFile.exists()) testStatsFile.delete();
    }

    @AfterEach
    public void teardown() {
        System.setOut(originalOut);
        if (testVocabFile.exists()) testVocabFile.delete();
        if (testStatsFile.exists()) testStatsFile.delete();
    }

    @Test
    public void testAddVocab() {
        String input = "1\nTestdeutsch\nTestalbanisch\n7\n";
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))) {
            Main.runWithScanner(scanner);
        }

        String output = outContent.toString();
        assertTrue(output.contains("✅ Vokabel gespeichert!"));

        VocabService vocabService = new VocabService();
        assertTrue(vocabService.getAllVocabs().stream().anyMatch(v -> v.getDeutsch().equalsIgnoreCase("Testdeutsch") && v.getAlbanisch().equalsIgnoreCase("Testalbanisch")));
    }

    @Test
    public void testListVocabsEmpty() {
        String input = "2\n7\n";
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))) {
            Main.runWithScanner(scanner);
        }
        String output = outContent.toString();
        assertTrue(output.contains("Noch keine Vokabeln gespeichert."));
    }

    @Test
    public void testMenuOptionSearchFound() {
        VocabService vocabService = new VocabService();
        vocabService.addVocab("Haus", "shtëpi");

        String input = "5\nHaus\n7\n";
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))) {
            Main.runWithScanner(scanner);
        }

        String output = outContent.toString();
        assertTrue(output.contains("✅ Gefundene Vokabeln:"));
        assertTrue(output.contains("Haus → shtëpi"));
        assertTrue(output.contains("Programm beendet") || output.contains("📦 Vokabeln und Statistiken gespeichert"));
    }

    @Test
    public void testMenuOptionSearchNotFound() {
        String input = "5\nNichtVorhanden\n7\n";
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))) {
            Main.runWithScanner(scanner);
        }
        String output = outContent.toString();
        assertTrue(output.contains("❌ Keine Vokabeln gefunden."));
    }

    @Test
    public void testMenuInvalidInput() {
        String input = "abc\n7\n";
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))) {
            Main.runWithScanner(scanner);
        }
        String output = outContent.toString();
        assertTrue(output.contains("❗ Ungültige Eingabe"));
    }

    @Test
    public void testExitFromMenu() {
        String input = "7\n";
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))) {
            Main.runWithScanner(scanner);
        }
        String output = outContent.toString();
        assertTrue(output.contains("📦 Vokabeln und Statistiken gespeichert. Programm beendet."));
    }
}