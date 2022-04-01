package be.ac.ulb.infof307.g06.presentation.manageuser.signup;


import be.ac.ulb.infof307.g06.presentation.manageuser.signup.SignUpController;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the signUpController.
 * Since all the methods we test return booleans we just provide
 * the input and then assert the expected output.
 */
class TestSignUpController extends SignUpController {


    public TestSignUpController(){
        super(true);
    }

    @Test
    void verifyIfEmptyReturnsFalseWhenNothingIsEmpty() {
        assertFalse(verifyIfEmpty("Admin","example@test.be","Jean",
                "Jean","123465789","123465789"));
    }

    @Test
    public void verifyIfEmptyReturnsTrueWhenUsernameIsEmpty() {
        assertTrue(verifyIfEmpty("","example@test.be","Jean",
                "Jean","123465789","123465789"));
    }

    @Test
    public void verifyIfEmptyReturnsTrueWhenEmailIsEmpty() {
        assertTrue(verifyIfEmpty("Admin", "", "Jean",
                "Jean", "123465789", "123465789"));
    }

    @Test
    public void verifyIfEmptyReturnsTrueWhenFirstNameIsEmpty() {
        assertTrue(verifyIfEmpty("Admin","example@test.be","",
                "Jean","123465789","123456789"));
    }
    @Test
    public void verifyIfEmptyReturnsTrueWhenLastNameIsEmpty() {
        assertTrue(verifyIfEmpty("Admin","example@test.be","Jean",
                "","123456789","123456789"));
    }
    @Test
    public void verifyIfEmptyReturnsTrueWhenPasswordIsEmpty() {
        assertTrue(verifyIfEmpty("Admin","example@test.be","Jean",
                "Jean","","123456789"));
    }
    @Test
    public void verifyIfEmptyReturnsTrueWhenConfirmPasswordIsEmpty() {
        assertTrue(verifyIfEmpty("Admin","example@test.be","Jean",
                "Jean","123456789",""));
    }

    @Test
    public void verifyEmailNoAtReturnsFalse(){
        assertFalse(verifyEmail("testmail.jp"));
    }

    @Test
    public void verifyEmailNoDomainReturnsFalse(){
        assertFalse(verifyEmail("test@mail"));
    }

    @Test
    public void verifyEmailNoPrefixReturnsFalse(){
        assertFalse(verifyEmail("@mail.com"));
    }

    @Test
    public void verifyEmailNoPostfixReturnsFalse(){
        assertFalse(verifyEmail("test@.com"));
    }

    @Test
    public void verifyEmailSeveralDotsReturnsFalse(){
        assertFalse(verifyEmail("test@mail...com"));
    }

    @Test void verifyEmailReturnsTrueOnValidEmail(){
        assertTrue(verifyEmail("right@email.de"));
    }

    @Test
    public void verifyUsernameReturnsTrueOnValidInput() {
        assertTrue(verifyUsername("jp"));
    }

    @Test
    public void verifyUsernameReturnsFalseWSpecialChar(){
        assertFalse(verifyUsername("jean@"));
    }

    @Test
    public void verifyUsernameReturnsFalseWUpperCase(){
        assertFalse(verifyUsername("Jeanp"));
    }

    @Test
    public void verifyPasswordAcceptsEqualPwds(){
        assertTrue(verifyPassword("12345678","12345678"));
    }

    @Test
    public void verifyPasswordRejectsEqualPwdsButTooShort(){
        assertFalse(verifyPassword("short","short"));
    }

    @Test
    public void verifyPasswordRejectsDifferentPwds(){
        assertFalse(verifyPassword("longenough","butNotEqual"));
    }

}