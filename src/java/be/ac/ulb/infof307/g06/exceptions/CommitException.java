package be.ac.ulb.infof307.g06.exceptions;

/**
 * used to show exceptions which have been raised during commit operations
 */
public class CommitException extends Exception {
    public CommitException(Exception e) { super(e);}
}