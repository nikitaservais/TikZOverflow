package be.ac.ulb.infof307.g06.presentation.manageproject.renameproject;

import be.ac.ulb.infof307.g06.models.project.Project;

/**
 *  Renaming Project button listener
 */
public interface RenameProjectListener {
    void renameFile(String newName, Project project);
    void closeWindow();
}
