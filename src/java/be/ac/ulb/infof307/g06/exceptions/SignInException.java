package be.ac.ulb.infof307.g06.exceptions;

/**
 * used to show an error raised during signing in a user
 */
public class SignInException extends Exception {
    public SignInException(Throwable e) {
        super(e);
    }
}
