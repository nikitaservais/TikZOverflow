package be.ac.ulb.infof307.g06.exceptions;

/**
 * used to show an error raised during database process (error in request)
 */
public class DatabaseException extends Exception {
    public DatabaseException(Exception e) {
        super(e);
    }
}
