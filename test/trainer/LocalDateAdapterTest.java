package trainer;

import com.google.gson.*;
import trainer.LocalDateAdapter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LocalDateAdapterTest {

    private final LocalDateAdapter adapter = new LocalDateAdapter();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, adapter).create();

    @Test
    public void testSerialize() {
        LocalDate date = LocalDate.of(2025, 6, 5);
        JsonElement json = adapter.serialize(date, LocalDate.class, null);
        assertEquals("2025-06-05", json.getAsString());
    }

    @Test
    public void testDeserialize() {
        JsonPrimitive json = new JsonPrimitive("2025-06-05");
        LocalDate date = adapter.deserialize(json, LocalDate.class, null);
        assertEquals(LocalDate.of(2025, 6, 5), date);
    }

    @Test
    public void testGsonIntegration() {
        LocalDate date = LocalDate.of(2025, 6, 5);
        String json = gson.toJson(date);
        assertEquals("\"2025-06-05\"", json);

        LocalDate parsed = gson.fromJson(json, LocalDate.class);
        assertEquals(date, parsed);
    }
}