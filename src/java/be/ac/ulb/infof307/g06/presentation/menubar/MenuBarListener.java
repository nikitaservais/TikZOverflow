package be.ac.ulb.infof307.g06.presentation.menubar;

import be.ac.ulb.infof307.g06.exceptions.CommitException;
import be.ac.ulb.infof307.g06.exceptions.VersioningException;

/**
 *  MenuBar button listener
 */
public interface MenuBarListener {
    void exitMenuItemClicked();
    void helpMenuItemClicked();
    void saveProject();
    void closeProject();
    void importProjectButtonClicked();
    void exportProjectButtonClicked();
    void commit();
    void revertCommit() throws CommitException, VersioningException;
    void createNewBranch();
    void chooseExistingBranch();
    void history();
    void merge();
    void displayCommitContent();
    void themeMenuItemClicked();
    String getPath();
}
