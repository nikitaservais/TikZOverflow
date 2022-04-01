package be.ac.ulb.infof307.g06.presentation.manageuser.edituser.modifyemail;


import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.ModifyUserInfoListener;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


/**
 * Create a page for modifying the email of the user.
 */
public class ModifyEmailView extends AnchorPane {
    @FXML
    private Label currentEmailLabel;
    @FXML
    private TextField newEmailTextField;
    private final SceneUtils scene;
    private final User user;
    private ModifyUserInfoListener listener;

    public ModifyEmailView(User user) {
        this.user = user;
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    public void setListener(ModifyUserInfoListener listener) {
        this.listener = listener;
    }

    @FXML
    private void initialize() {
        currentEmailLabel.setText(user.getEmail());
        newEmailTextField.setPromptText(user.getEmail());
    }

    @FXML
    private void confirmOnAction() {
        listener.confirmButtonClicked();
    }

    @FXML
    private void cancelOnAction() {
        listener.cancelButtonClicked();
    }
    public String getEmailFieldText() { return newEmailTextField.getText(); }


}
