package be.ac.ulb.infof307.g06.exceptions;

/**
 * used to show an error raised because a value is missing
 */
public class ValueNotFoundException extends Exception {
    public ValueNotFoundException(Exception e)  {
        super(e);
    }
}