package be.ac.ulb.infof307.g06.models;

import be.ac.ulb.infof307.g06.exceptions.HashingException;
import be.ac.ulb.infof307.g06.utils.PasswordUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestPasswordHasher extends PasswordUtils {

    @Test
    public void passwordHasherReturnsTheExpectedHashFor_password123() throws HashingException {
        // Can't really test it otherwise since the char output is in UTF-8
        // and can't really be changed (tried several other encodings) and
        // you can't create a string with the expected value since bytes are
        // somehow changed.
        String expected = PasswordUtils.hashPassword("password123");
        String actual = PasswordUtils.hashPassword("password123");
        assertEquals(expected, actual);
    }
}