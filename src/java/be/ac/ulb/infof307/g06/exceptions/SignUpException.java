package be.ac.ulb.infof307.g06.exceptions;

/**
 * used to show an error raised during signing up a user
 */
public class SignUpException extends Exception {
    public SignUpException(Throwable e) {
        super(e);
    }
}
