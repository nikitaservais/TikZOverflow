package be.ac.ulb.infof307.g06.presentation.manageproject.renameproject;

import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Class that displays a window to renaming a given file.
 */
public class RenameProjectView extends AnchorPane {
    @FXML
    private TextField textField;
    private Project project;
    private RenameProjectListener listener;
    private final SceneUtils scene;

    public RenameProjectView() {
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    /**
     * Action on okButton clicked
     * @param event the mouse event.
     */
    @FXML
    private void okButtonClicked(ActionEvent event) {
        listener.renameFile(textField.getText(), project);
        listener.closeWindow();
    }

    /**
     * action on cancelButton clicked
     * @param event the mouse event.
     */
    @FXML
    private void cancelButtonClicked(ActionEvent event) {
        listener.closeWindow();
    }

    /**
     * Setter to set the controller associated to RenameProjectView.
     * @param listener RenameProjectController the controller associated to RenameProjectView.
     */
    public void setListener(RenameProjectListener listener) {
        this.listener = listener;
    }

    /**
     * Set the project into the class view and also textfield with the current project name.
     * @param project current project.
     */
    public void setProject(Project project) {
        this.project = project;
        textField.setText(project.getName());
    }
}
