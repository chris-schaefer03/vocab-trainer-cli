package trainer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class UserService {

    private final List<User> users = new ArrayList<>();
    private String userFile = "data/users.json";
    private boolean testMode = false;

    public UserService(String filePath, boolean testMode) {
        this.userFile = filePath;
        this.testMode = testMode;
        loadUsers();
    }

    public UserService() {
        loadUsers();
    }

    public void setUserFile(String filePath) {
        this.userFile = filePath;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public boolean register(String username, String password, String role) {
        if (findUser(username) != null) {
            return false;
        }

        String hash = hash(password);
        users.add(new User(username, hash, role));
        saveUsers();
        return true;
    }

    public User login(String username, String password) {
        User user = findUser(username);
        if (user == null) return null;

        String hash = hash(password);
        return user.getPasswordHash().equals(hash) ? user : null;
    }

    public boolean deleteUser(String usernameToDelete, String performedBy) {
        if (usernameToDelete.equals(performedBy)) {
            return false;
        }

        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User u = iterator.next();
            if (u.getUsername().equalsIgnoreCase(usernameToDelete)) {
                iterator.remove();
                saveUsers();
                return true;
            }
        }
        return false;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    private User findUser(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    public void saveUsers() {
        if (testMode) return;

        try (Writer writer = new FileWriter(userFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.out.println("❌ Fehler beim Speichern der Benutzerdatei: " + e.getMessage());
        }
    }

    private void loadUsers() {
        File file = new File(userFile);

        if (!file.exists()) {
            if (!testMode) {
                users.add(new User("admin", hash("admin123"), "admin"));
                users.add(new User("max", hash("max123"), "user"));
                saveUsers();
            }
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> loaded = gson.fromJson(reader, listType);
            if (loaded != null) {
                users.clear();
                users.addAll(loaded);
            }
        } catch (IOException e) {
            System.out.println("❌ Fehler beim Laden der Benutzerdatei: " + e.getMessage());
        }
    }

    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : encoded) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("❌ SHA-256 nicht verfügbar", e);
        }
    }
}