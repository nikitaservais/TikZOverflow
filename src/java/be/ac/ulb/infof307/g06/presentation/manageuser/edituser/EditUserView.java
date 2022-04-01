package be.ac.ulb.infof307.g06.presentation.manageuser.edituser;

import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * General user profile editing page.
 * Can modify username, email and password.
 */
public class EditUserView extends AnchorPane {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;

    private final User user;
    private final SceneUtils scene;
    private EditUserListener listener;

    public EditUserView(User user) {
        this.user = user;
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    @FXML
    private void initialize() {
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
    }

    /**
     * when exit button is clicked
     * @param event The mouse event.
     */
    @FXML
    private void exitOnAction(ActionEvent event) {
        listener.exitButtonClicked();
    }

    /**
     * when modifyUsername button is clicked
     * @param event The mouse event.
     */
    @FXML
    private void modifyUsernameOnAction(ActionEvent event) {
        listener.modifyUsernameButtonClicked();
    }

    /**
     * when modifyEmail button is clicked
     * @param event The mouse event.
     */
    @FXML
    private void modifyEmailOnAction(ActionEvent event) {
        listener.modifyEmailButtonClicked();
    }

    /**
     * when modifyPassword is clicked
     * @param event The mouse event.
     */
    @FXML
    private void modifyPasswordOnAction(ActionEvent event) {
        listener.modifyPasswordButtonClicked();
    }

    public void setListener(EditUserListener listener) {
        this.listener = listener;
    }
}
