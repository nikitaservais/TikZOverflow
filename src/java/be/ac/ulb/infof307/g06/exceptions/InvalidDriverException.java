package be.ac.ulb.infof307.g06.exceptions;

/**
 * used to show an error raised because the database drivers are missing or wrong
 */
public class InvalidDriverException extends Exception{
    public InvalidDriverException(Exception e) {
        super(e);
    }
}
