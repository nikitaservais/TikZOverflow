package be.ac.ulb.infof307.g06.presentation.versioning.createcommit;

import be.ac.ulb.infof307.g06.exceptions.CommitException;

/**
 * Commit creation button listener
 */
public interface CreateCommitListener {
    void confirmButtonClicked(String message) throws CommitException;
}
