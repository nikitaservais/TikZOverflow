package be.ac.ulb.infof307.g06.presentation.mainmenu;

import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * This class displays the first window of the program, where the user
 * can select to load a Project or create a new one
 */
public class MainMenuView extends AnchorPane {
    private final SceneUtils scene;
    private MainMenuListener listener;

    public MainMenuView() { // temp for now views are passed as argument, will change when fxml is refacto for all view
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    @FXML
    private void exitOnAction(ActionEvent event) {
        listener.exitButtonClicked();
    }

    /**
     * use the controller to handle this event, it will create a new Project
     * @param event on user click
     */
    @FXML
    private void createProjectOnAction(ActionEvent event) {
        listener.createProjectButtonClicked();
    }

    /**
     * Controller method used to initLayout a new View for Project management
     * @param event on user click
     */
    @FXML
    private void openProjectOnAction(ActionEvent event) {
        listener.openProjectButtonClicked();
    }

    /**
     * Open a new scene with able to edit the current user logged in.
     * @param event on user click.
     */
    @FXML
    private void editUserOnAction(ActionEvent event) {
        listener.editUserButtonClicked();
    }
    /**
     * Open the help window.
     * @param event on user click.
     */
    @FXML
    private void helpOnAction(ActionEvent event) {
        listener.help();
    }

    /**
     * Set the instance of the controller of this class.
     * @param listener The instance of the controller of this class.
     */
    public void setListener(MainMenuListener listener) {
        this.listener = listener;
    }
}
