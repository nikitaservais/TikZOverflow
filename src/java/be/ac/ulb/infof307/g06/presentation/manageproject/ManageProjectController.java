package be.ac.ulb.infof307.g06.presentation.manageproject;

import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.integrity.IntegrityActionController;
import be.ac.ulb.infof307.g06.models.integrity.IntegrityModel;
import be.ac.ulb.infof307.g06.models.project.ManageProjectModel;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.presentation.mainmenu.MainMenuController;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.EditingController;
import be.ac.ulb.infof307.g06.models.project.ProjectManager;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.presentation.manageproject.renameproject.RenameProjectController;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Link the ManageProjectView and the ManageProjectModel.
 * Verify that the right number of info is selected in the GUI.
 */
public class ManageProjectController implements ManageProjectListener, ManageProjectForIntegrityListener{
    private final ManageProjectView manageProjectView;
    private final ManageProjectModel manageProjectModel;
    private final Stage primaryStage;
    private final User user;

    /**
     * Constructor of ManageProjectController class.
     * @param user the instance of the current user of the application.
     * @throws DatabaseConnectionException Error from database.
     */
    public ManageProjectController(User user) throws DatabaseConnectionException {
        this.primaryStage = StageUtils.getStage();
        this.user = user;
        this.manageProjectModel = new ManageProjectModel(user);
        try {
            this.manageProjectModel.updateUserFileInfo();
        } catch (DataAccessException | DatabaseException e) {
            new ErrorMessage("Error while trying to update user files info");
        }
        manageProjectView = new ManageProjectView();
        manageProjectView.setListener(this);
        manageProjectView.setFileTable();
    }

    /**
     * Connect to copy in model.
     * Create a new file that is the same as the copied one.
     * The new name is "name_copy".
     */
    @Override
    public void copyProject() {
        if (manageProjectView.getCurrentSelectedIndex() != -1) {
            try{
                for (Project project : manageProjectView.getSelectedItems()) {
                    try {
                        manageProjectModel.copyProject(project);
                    } catch (VersioningException | InvalidDriverException | DatabaseException e) {
                        new ErrorMessage("An error occcured while copying project");
                    }
                }
            } catch (IOException | DataAccessException e){
                new ErrorMessage("An error has occurred during the file creation process.\n" +
                        "Error : " + e);
            }
        }
        manageProjectView.setFileTable();
    }

    /**
     * Connect to delete in model.
     * Delete the file.
     */
    @Override
    public void deleteProject() {
        try {
            if (manageProjectView.getCurrentSelectedIndex() != -1) {
                for (Project project : manageProjectView.getSelectedItems()) {
                    manageProjectModel.deleteProject(project);
                }
            }
            manageProjectView.setFileTable();
        } catch (DataAccessException e) {
            new ErrorMessage("Error while trying to delete project");
        }
    }

    /**
     * Connect to newDir in model.
     * Move the file in a new directory.
     */
    @Override
    public void changeDirectory() {
        if (manageProjectView.getCurrentSelectedIndex() != -1) {
            for (Project project : manageProjectView.getSelectedItems()) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File newDirectory = directoryChooser.showDialog(this.primaryStage);
                try {
                    manageProjectModel.changeDirectory(project,newDirectory.getAbsolutePath());
                } catch (IOException | DataAccessException e) {
                    new ErrorMessage("Could not move the file to another directory.\n error meesage :"+e);
                }

            }
        }
    }

    /**
     * Connect to renaming in model.
     * Change the name of the file.
     */
    @Override
    public void renameProject() {
        if (manageProjectView.getCurrentSelectedIndex() != -1) {
            if (manageProjectView.getSelectedItems().size() == 1) {
                Project project = manageProjectView.getSelectedItems().get(0);
                RenameProjectController renameProjectController = new RenameProjectController(manageProjectModel, project);
                renameProjectController.show();
            } else {
                manageProjectView.showAlertMoreThanOneFile();
            }
        }
        manageProjectView.setFileTable();
    }

    /**
     * Connect to edit in model.
     * Open the drawing view and start editing the file.
     */
    @Override
    public void openProject() {
        if (manageProjectView.getCurrentSelectedIndex() != -1) {
            if (manageProjectView.getSelectedItems().size() == 1) {
                boolean isNotCorrupted = true;
                try {
                    IntegrityModel integrityModel = new IntegrityModel();
                    Project project = manageProjectView.getSelectedItems().get(0);
                    isNotCorrupted = integrityModel.verifyChecksum(project);
                    if (!isNotCorrupted) {
                        IntegrityActionController integrityActionController = new IntegrityActionController(project, this);
                        integrityActionController.show();
                    }
                    if(isNotCorrupted) {
                        openEditingControllerForProject(project);
                    }
                } catch (IOException | DataAccessException | VersioningException | DatabaseException | InvalidDriverException e) {
                    new ErrorMessage("Error while opening the project");
                }
            } else {
                manageProjectView.showAlertMoreThanOneFile();
            }
        }
    }

    /**
     * Opens the given project in the editing controller.
     * @param project the project to open.
     */
    public void openEditingControllerForProject(Project project) throws DatabaseConnectionException {
        EditingController editingController = new EditingController(project);
        editingController.show();
    }

    /**
     * Close the Project management window, and open the main menu window.
     */
    @Override
    public void returnToMainWindow() {
        //todo replace userid
        MainMenuController mainMenuController = new MainMenuController(user);
        mainMenuController.show();
    }

    /**
     * @return the user infos
     */
    @Override
    public List<Project> getUserFileInfo() {
        List<Project> allProjects = new ArrayList<>();
        try {
            allProjects = new ProjectManager().getAllProjects(user);
        } catch (DatabaseConnectionException | DataAccessException e) {
            new ErrorMessage("Error while getting all projects");
        }
        return allProjects;
    }

    /**
     * Displays the linked view
     */
    public void show() {
        primaryStage.setScene(manageProjectView.getScene());
        primaryStage.show();
    }
}
