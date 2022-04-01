package be.ac.ulb.infof307.g06.presentation.manageuser.edituser.modifyusername;

import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.ModifyUserInfoListener;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Create a page for modifying the username of the user.
 */
public class ModifyUsernameView extends AnchorPane {
    private final User user;
    private final SceneUtils scene;
    @FXML
    private Label currentUsernameLabel;
    @FXML
    private TextField newUsernameTextField;
    private ModifyUserInfoListener listener;

    public ModifyUsernameView(User user) {
        this.user = user;
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    @FXML
    private void initialize() {
        currentUsernameLabel.setText(user.getUsername());
        newUsernameTextField.setPromptText(user.getUsername());
    }

    @FXML
    private void confirmOnAction() {
        listener.confirmButtonClicked();
    }

    @FXML
    private void cancelOnAction() {
        listener.cancelButtonClicked();
    }

    public String getUsernameFieldText() {
        return newUsernameTextField.getText();
    }

    public void setListener(ModifyUserInfoListener listener) {
        this.listener = listener;
    }

}
