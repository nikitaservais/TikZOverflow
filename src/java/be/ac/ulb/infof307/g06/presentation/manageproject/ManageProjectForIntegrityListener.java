package be.ac.ulb.infof307.g06.presentation.manageproject;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.models.project.Project;

public interface ManageProjectForIntegrityListener {
    void openEditingControllerForProject(Project project) throws DatabaseConnectionException;
    void returnToMainWindow();
}
