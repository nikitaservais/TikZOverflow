package be.ac.ulb.infof307.g06.exceptions;

/**
 * used to show an error raised during database access (can't reach the database)
 */
public class DataAccessException extends Exception {
    public DataAccessException(Exception e) {
        super(e);
    }
}
