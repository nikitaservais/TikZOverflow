package be.ac.ulb.infof307.g06.models.user;

import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.models.database.UserDB;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.user.UserManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestUserManager {
    UserDB userDB;
    ProjectDB projectDB;
    UserManager userManager;
    int id;
    String username = "username";
    String name = "Brandlock";
    String given_name = "Emile";
    String email = "test@ulb.ac.be";
    String password ="verYsecure1234";

    @BeforeAll
    void setup()  {
        try {
            userDB = new UserDB();
            projectDB = new ProjectDB();
            userManager = new UserManager();
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void createUser(){
        try {
            id = userManager.signUp(username, name, given_name, email, password);
        } catch (Exception error) {
            fail("Unable to create user");
        }
    }

    @AfterEach
    void deleteUser() {
        try {
            userDB.deleteUserWithID(id);
        } catch (Exception error) {
            fail("Unable to delete User");
        }
    }

    /**
     * To test if the signUp works correctly we test it by signing up a new
     * user and then we try to sign up a new user with a different username
     * and then again with a different mail but the same username as before.
     * This makes sure that all the given failure possibilities are tested.
     */
    @Test
    public void signUpWithExistingUsernameThrowsSignUpException() {
        try {
            userManager.signUp(username, name, given_name, "different@mail.com", password);
            fail("Sign Up Exception has not been thrown");
        } catch (SignUpException e) {
            assertTrue(true);
        }
    }

    @Test
    public void signUpWithExistingEmailThrowsSignUpException() {
        try {
            userManager.signUp("notSameUsername", name, given_name, email, password);
            fail("Sign Up Exception has not been thrown");
        } catch (SignUpException e) {
            assertTrue(true);
        }
    }

    @Test
    public void signInReturnsExpectedId() throws SignInException {
        assertEquals(id, userManager.signIn(username, password));
    }

    @Test
    public void signInWithNonExistentUsernameThrowsSignInException() {
        try {
            userManager.signIn("nonExistentUsername", password);
            fail("Sign In Exception has not been thrown");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void signInWithWrongPasswordSThrowsSignInException() {
        try {
            userManager.signIn(username,"wrongPassword");
            fail("Sign In Exception has not been thrown");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void wrongPasswordAndUsernameRaisesSignInException() {
        try {
            userManager.signIn("wrongUsername","wrongPassword");
            fail("Sign In Exception has not been thrown");
        } catch (Exception e) {
            assertTrue(true); // We got the expected result
        }
    }

    @Test
    public void goodUsernameUpdateReturnsNothing() throws ModifyUserDataException, SignUpException {
        assertEquals("", userManager.updateUsername(id, "camerlynck"));
    }

    @Test
    public void emptyUsernameUpdateReturnsEmptyErrorMessage() throws ModifyUserDataException, SignUpException {
        String message = "Please enter a new username.";
        assertEquals(message , userManager.updateUsername(id, ""));
    }

    @Test
    public void usernameNotAvailableUpdateReturnNotAvailableErrorMessage() throws SignUpException, DatabaseException {
        String newUsername = "testname";
        String newEmail = "charles@degaule.com";
        int newID = userManager.signUp(newUsername, name, given_name, newEmail, password);
        String message = "Username not available.";
        assertEquals(message, userManager.updateUsername(newID, username));
        userDB.deleteUserWithID(newID);
    }

    @Test
    public void badUsernameFormatUpdateReturnsInvalidUsernameMessage() throws SignUpException, ModifyUserDataException {
        String message = "Please enter a valid username (without special characters and upper cases).";
        assertEquals(message , userManager.updateUsername(id, "Camerlynck"));
    }

    @Test
    public void goodEmailUpdateReturnNothing() throws ModifyUserDataException, SignUpException {
        assertEquals("", userManager.updateEmail(id, "rubiks@cube.com"));
    }

    @Test
    public void emptyEmailUpdateReturnsEmptyErrorMessage() throws ModifyUserDataException, SignUpException {
        String message = "Please enter a new email.";
        assertEquals(message , userManager.updateEmail(id, ""));
    }

    @Test
    public void emailNotAvailableUpdateReturnsNotAvailableErrorMessage() throws SignUpException, DatabaseException {
        String _username = "charles";
        String _email = "test2@ulb.ac.be";
        int newID = userManager.signUp(_username, name, given_name, _email, password);
        String message = "Email not available.";
        assertEquals(message, userManager.updateEmail(newID, "test@ulb.ac.be"));
        userDB.deleteUserWithID(newID);
    }

    @Test
    public void badEmailFormatUpdateReturnsInvalidEmailMessage() throws ModifyUserDataException, SignUpException {
        String message = "The email is not valid.";
        assertEquals(message , userManager.updateEmail(id, "carambarhotmail.be"));
    }

    @Test
    public void goodPasswordUpdateReturnsNothing() throws SignUpException, ModifyUserDataException {
        String newPasswordOne = "GoodPassword1435";
        String newPasswordTwo = "GoodPassword1435";
        assertEquals("", userManager.updatePassword(id, password, newPasswordOne, newPasswordTwo));
    }

    @Test
    public void oldPasswordIncorrectUpdateReturnsOldPasswordIncorrectMessage() throws ModifyUserDataException, SignUpException {
        String newPasswordOne = "GoodPassword1435";
        String newPasswordTwo = "GoodPassword1435";
        String message = "Your old password is incorrect.";
        assertEquals(message, userManager.updatePassword(id, "CarottesAuChoux", newPasswordOne, newPasswordTwo));
    }

    @Test
    public void firstPasswordEmptyUpdateReturnsFirstPasswordEmptyErrorMessage() throws SignUpException, ModifyUserDataException {
        String newPasswordOne = "";
        String newPasswordTwo = "GoodPassword1435";
        String message = "Please enter a new password.";
        assertEquals(message, userManager.updatePassword(id, password, newPasswordOne, newPasswordTwo));
    }

    @Test
    public void secondPasswordEmptyUpdateReturnsSecondPasswordNotValidErrorMessage() throws SignUpException, ModifyUserDataException {
        String newPasswordOne = "GoodPassword1435";
        String newPasswordTwo = "";
        String message = "Please confirm your new password.";
        assertEquals(message, userManager.updatePassword(id, password, newPasswordOne, newPasswordTwo));
    }

    @Test
    public void newPasswordNotTheSameUpdateReturnsNotSamePasswordErrorMessage() throws SignUpException, ModifyUserDataException {
        String newPasswordOne = "GoodPassword1435";
        String newPasswordTwo = "BadPassword";
        String message = "Please enter the same password to confirm.";
        assertEquals(message, userManager.updatePassword(id, password, newPasswordOne, newPasswordTwo));
    }

    @Test
    public void newPasswordNotLongEnoughUpdateReturnsMinimumPasswordLengthErrorMessage() throws SignUpException, ModifyUserDataException {
        String newPasswordOne = "short";
        String newPasswordTwo = "short";
        String message = "Please enter a password of at least 8 characters.";
        assertEquals(message, userManager.updatePassword(id, password, newPasswordOne, newPasswordTwo));
    }

}