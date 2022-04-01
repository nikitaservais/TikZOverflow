package be.ac.ulb.infof307.g06.presentation.manageproject.renameproject;

import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.models.project.ManageProjectModel;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Control the renaming of a project action.
 */
public class RenameProjectController implements RenameProjectListener {
    private final Stage primaryStage;
    private final Stage stage;
    private final RenameProjectView renameProjectView;
    private final ManageProjectModel model;

    /**
     * Main constructor
     * @param model ManageProjectModel used to call his rename method.
     * @param project current project that will be renamed.
     */
    public RenameProjectController(ManageProjectModel model, Project project) {
        this.primaryStage = StageUtils.getStage();
        this.model = model;
        renameProjectView = new RenameProjectView();
        renameProjectView.setProject(project);
        renameProjectView.setListener(this);
        stage = new Stage();
    }

    /**
     * Call the model to renaming the file.
     * @param newName  new name.
     * @param project absolute path of the old name.
     */
    @Override
    public void renameFile(String newName, Project project) {
        boolean success = model.renameProject(newName, project);
        if (!success) {
            new ErrorMessage("Couldn't rename the project.");
        }
    }

    /**
     * Close renaming stage
     */
    @Override
    public void closeWindow() {
        stage.close();
    }

    /**
     * Displays the associated view
     */
    public void show() {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        stage.setTitle("Rename Project");
        stage.setResizable(false);
        stage.setScene(renameProjectView.getScene());
        stage.showAndWait();
    }
}
