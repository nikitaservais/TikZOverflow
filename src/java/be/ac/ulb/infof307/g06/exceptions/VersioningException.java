package be.ac.ulb.infof307.g06.exceptions;

/**
 * used to show exceptions which have been raised during versioning process
 */
public class VersioningException extends Exception {
    public VersioningException(Exception e) {
        super(e);
    }
}
