package be.ac.ulb.infof307.g06.models.database;

import be.ac.ulb.infof307.g06.models.database.UserDB;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.exceptions.ValueNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestUserDB {
    UserDB userDB;

    int userID;
    String username = "test";
    String name = "testName";
    String givenName = "testGivenName";
    String email = "testEmail";
    String password = "testPassword";
    String newEmail = "TristanEmail";
    String newPassword = "TristanEmail";
    String newUsername = "TristanUsername";

    @BeforeAll
    void createUser() throws DatabaseException {
        userDB = new UserDB();
        userID =  userDB.createUser(username, name, givenName, email, password);
    }

    @AfterAll
    void deleteUser() throws DatabaseException  {
        userDB.deleteUserWithID(userID);
    }

    @Test
    void userIsCorrectlyCreated() throws DatabaseException, InvalidDriverException {
        int newUserId = userDB.createUser("testUserNametmp", "newNametmp", "newGivenNametmp", "testMailAdresstmp", "newPasswordtmp");
        String[] userInfo = userDB.getInfoUser(newUserId);
        assertEquals("testUserNametmp", userInfo[0]);
        assertEquals("newNametmp", userInfo[1]);
        assertEquals("newGivenNametmp", userInfo[2]);
        assertEquals("testMailAdresstmp", userInfo[3]);
        assertEquals("newPasswordtmp", userInfo[4]);
        userDB.deleteUserWithID(newUserId);
    }

    @Test
    void getUserIDFromExistentUsernameReturnsRightID() throws DatabaseException, ValueNotFoundException {
        int testUserID = userDB.getUserID(username);
        assertEquals(userID, testUserID);
    }

    @Test
    void getUserIDFromNonExistentUserThrowsValueNotFoundException() throws DatabaseException {
        try {
            userDB.getUserID("Antoine");
            fail("getUserID with wrong username didn't throw ValueNotfoundException");
        }
        catch (ValueNotFoundException e){
            assertTrue(true);
        }
    }

    @Test
    void isUsernameAvailableWithExistentUsernameReturnsFalse() throws DatabaseException {
        boolean isAvailable = userDB.isUsernameAvailable(username);
        assertFalse(isAvailable);
    }

    @Test
    void isUsernameAvailableWithNonExistentUsernameReturnsTrue() throws DatabaseException {
        assertTrue(userDB.isUsernameAvailable("Antoine"));
    }

    @Test
    void isEmailAvailableWithExistentEmailReturnsFalse() throws DatabaseException {
        boolean isAvailable = userDB.isEmailAvailable(email);
        assertFalse(isAvailable);
    }

    @Test
    void isEmailAvailableWithNonExistentEmailReturnsTrue() throws DatabaseException {
        boolean isAvailable = userDB.isEmailAvailable("AntoineEmail");
        assertTrue(isAvailable);
    }

    @Test
    void emailIsCorrectlyModified() throws DatabaseException, InvalidDriverException {
        userDB.modifyEmail(userID, newEmail);
        String[] userInfo = userDB.getInfoUser(userID);
        assertEquals(newEmail, userInfo[3]);
    }

    @Test
    void passwordIsCorrectlyModified() throws DatabaseException, InvalidDriverException {
        userDB.modifyPassword(userID, newPassword);
        String[] userInfo = userDB.getInfoUser(userID);
        assertEquals(newPassword, userInfo[4]);
    }

    @Test
    void usernameIsCorrectlyModified() throws DatabaseException, InvalidDriverException {
        userDB.modifyUsername(userID, newUsername);
        String[] userInfo = userDB.getInfoUser(userID);
        assertEquals(newUsername, userInfo[0]);
        userDB.modifyUsername(userID, username);
    }
}