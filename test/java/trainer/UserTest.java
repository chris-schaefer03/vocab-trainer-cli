package java.trainer;

import trainer.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testIsAdmin() {
        User adminUser = new User("admin", "hash", "admin");
        User normalUser = new User("max", "hash", "user");

        assertTrue(adminUser.isAdmin());
        assertFalse(normalUser.isAdmin());
    }
}