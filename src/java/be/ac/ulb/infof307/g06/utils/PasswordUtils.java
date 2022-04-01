package be.ac.ulb.infof307.g06.utils;

import be.ac.ulb.infof307.g06.exceptions.HashingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * object that allows the hashing of a password
 */
public class PasswordUtils {
    /**
     * Hashes a password using the SHA-256 Algorithm and then returns the hash as a string.
     * @param password
     * @return hashed password
     */
    public static String hashPassword(String password) throws HashingException {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new HashingException(e);
        }
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        String hashStr = new String(hash, StandardCharsets.UTF_8);
        return hashStr;
    }
}
