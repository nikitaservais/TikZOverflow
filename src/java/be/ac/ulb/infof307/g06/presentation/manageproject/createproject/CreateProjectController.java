package be.ac.ulb.infof307.g06.presentation.manageproject.createproject;

import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.integrity.IntegrityModel;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.models.project.ProjectManager;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.EditingController;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.presentation.FileChooserView;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Controller of the create Project context
 */
public class CreateProjectController {
    private final Stage primaryStage;
    private File projectFile;
    private FileChooserView fileChooserView;
    private final User user;

    public CreateProjectController(User user) {
        this.primaryStage = StageUtils.getStage();
        this.user = user;
    }

    /**
     * Shows the fileChooser
     */
    public void show() {
        chooseFile();
    }

    /**
     * Displays the fileChooser and then displays the edition
     */
    private void chooseFile() {
        fileChooserView = new FileChooserView("Save in","TEX", ConstantsUtils.texExtensionString);
        try {
            projectFile = fileChooserView.showSaveDialogInStage(primaryStage);
            if (projectFile != null) {
                if (!projectFile.exists()) {projectFile.createNewFile();}
                Project project = new ProjectManager().createProject(user, projectFile.getName(), projectFile.getPath());
                IntegrityModel integrityModel = new IntegrityModel();
                integrityModel.updateHashProject(project.getId(), project.getUser());
                EditingController editingController = new EditingController(project);
                editingController.show();
            }
        } catch (IOException | DatabaseException | DataAccessException | VersioningException | CommitException e) {
            new ErrorMessage("An error occurred while trying to open the Project");
        }
    }
}

