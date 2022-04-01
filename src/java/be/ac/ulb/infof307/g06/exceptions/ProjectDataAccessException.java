package be.ac.ulb.infof307.g06.exceptions;

/**
 * used to show an error raised during accessing the project data or information : can't reach the project
 */
public class ProjectDataAccessException extends Exception {
    public ProjectDataAccessException(Exception e) {
        super(e);
    }
}
