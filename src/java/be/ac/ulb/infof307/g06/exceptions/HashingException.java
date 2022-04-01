package be.ac.ulb.infof307.g06.exceptions;

/**
 * used to show exception that are possibly raised during the
 * usage of the PasswordHasher class.
 * Notably: NoSuchAlgorithmException
 * This is really just for good practices because this exception can't really
 * be raise since we use only one Algorithm (SHA-256) and this one is hard
 * coded and not a variable
 */
public class HashingException extends Exception {
    public HashingException(Exception e) {
        super(e);
    }

}
