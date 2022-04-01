package be.ac.ulb.infof307.g06.exceptions;

/**
 * used to show an error raised during database connection (wrong information)
 */
public class DatabaseConnectionException extends DatabaseException {
    public DatabaseConnectionException(Exception e) {
        super(e);
    }
}
