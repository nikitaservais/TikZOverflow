package be.ac.ulb.infof307.g06.presentation.manageproject;

import be.ac.ulb.infof307.g06.models.project.Project;

import java.util.List;

/**
 *  Project management button listener
 */
public interface ManageProjectListener {
    void copyProject();
    void deleteProject();
    void changeDirectory();
    void renameProject();
    void openProject();
    void returnToMainWindow();
    List<Project> getUserFileInfo();
}
