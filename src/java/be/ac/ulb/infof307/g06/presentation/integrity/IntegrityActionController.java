package be.ac.ulb.infof307.g06.presentation.integrity;

import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.integrity.IntegrityActionModel;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.manageproject.ManageProjectForIntegrityListener;

/**
 * Handles the choice of the user between continuing from last commit or deleting the corrupted project.
 */
public class IntegrityActionController implements IntegrityActionListener{
    private Project project;
    private IntegrityActionView integrityActionView;
    private IntegrityActionModel integrityActionModel;
    private ManageProjectForIntegrityListener manageProjectForIntegrityListener;

    /**
     *Constructor of IntegrityActionController.
     * @param project the corrupted project.
     * @param manageProjectForIntegrityListener The creator of the instance of this class.
     */
    public IntegrityActionController(Project project, ManageProjectForIntegrityListener manageProjectForIntegrityListener) {
        this.project = project;
        this.manageProjectForIntegrityListener = manageProjectForIntegrityListener;
        integrityActionView = new IntegrityActionView(this);
        integrityActionModel = new IntegrityActionModel(project);
    }

    /**
     * Displays the view.
     */
    public void show() {
        this.integrityActionView.display();
    }

    /**
     * Open the project a the last commit.
     */
    @Override
    public void continueFromLastCommit() {
        try {
            this.manageProjectForIntegrityListener.openEditingControllerForProject(project);
        } catch (DatabaseConnectionException e) {
            new ErrorMessage("Unable to open project at the last commit.");
        }
    }

    /**
     * Remove the project from the computer and the database and returns to project chooser window.
     */
    @Override
    public void deletedProject() {
        try {
            integrityActionModel.deleteProject();
        } catch (DataAccessException e) {
            new ErrorMessage("Unable to delete project.");
        } catch (DatabaseException | InvalidDriverException e) {
            new ErrorMessage("Unable to remove project from database.");
        }
        this.manageProjectForIntegrityListener.returnToMainWindow();
    }
}
