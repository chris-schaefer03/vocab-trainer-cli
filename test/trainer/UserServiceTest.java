package trainer;

import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static final String TEST_FILE = "test_users.json";
    private UserService userService;

    @BeforeEach
    void setup() throws Exception {
        Files.writeString(Paths.get(TEST_FILE), "[]", StandardCharsets.UTF_8);

        userService = new UserService(TEST_FILE, true);

        userService.register("admin", "admin123", "admin");
        userService.register("max", "max123", "user");
    }

    @AfterEach
    void cleanup() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testRegisterAndLogin() {
        User admin = userService.login("admin", "admin123");
        assertNotNull(admin);
        assertEquals("admin", admin.getUsername());
        assertEquals("admin", admin.getRole());

        User wrong = userService.login("admin", "falsch");
        assertNull(wrong);
    }

    @Test
    void testDuplicateRegistrationFails() {
        boolean result = userService.register("admin", "neu", "admin");
        assertFalse(result);
    }

    @Test
    void testDeleteUser() {
        boolean deleted = userService.deleteUser("max", "admin");
        assertTrue(deleted);

        User deletedUser = userService.login("max", "max123");
        assertNull(deletedUser);
    }

    @Test
    void testCannotDeleteSelf() {
        boolean deleted = userService.deleteUser("admin", "admin");
        assertFalse(deleted);
    }

    @Test
    void testGetAllUsersContainsExpected() {
        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());

        boolean containsAdmin = users.stream().anyMatch(u -> u.getUsername().equals("admin"));
        boolean containsMax = users.stream().anyMatch(u -> u.getUsername().equals("max"));

        assertTrue(containsAdmin);
        assertTrue(containsMax);
    }
}