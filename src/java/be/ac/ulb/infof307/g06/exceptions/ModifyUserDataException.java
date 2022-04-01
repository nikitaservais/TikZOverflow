package be.ac.ulb.infof307.g06.exceptions;

/**
 * used to show an error raised during update of user's info
 */
public class ModifyUserDataException extends DatabaseException {
    public ModifyUserDataException(Exception e) {
        super(e);
    }
}
