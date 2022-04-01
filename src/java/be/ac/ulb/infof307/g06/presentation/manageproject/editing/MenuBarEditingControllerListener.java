package be.ac.ulb.infof307.g06.presentation.manageproject.editing;

import be.ac.ulb.infof307.g06.exceptions.DataAccessException;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.exceptions.CommitException;
import be.ac.ulb.infof307.g06.exceptions.VersioningException;
import be.ac.ulb.infof307.g06.models.branch.Branch;

/**
 *  Editing MenuBar button listener
 */
public interface MenuBarEditingControllerListener {
    void saveProject();
    void importProjectButtonClicked();
    void exportProjectButtonClicked();
    void createCommit();
    int getProjectID();
    void updateBranchID(String branchName) throws VersioningException;
    void updateConsoleCanvas(String projectContent);
    void commitAfterNewBranch(Branch branch) throws CommitException;
    String getBranchName() throws VersioningException;
    Project getBranch(String branchName) throws DataAccessException;
}
