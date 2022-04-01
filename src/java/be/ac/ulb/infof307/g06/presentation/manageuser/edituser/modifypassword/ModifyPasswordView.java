package be.ac.ulb.infof307.g06.presentation.manageuser.edituser.modifypassword;

import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.ModifyUserInfoListener;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;

/**
 * Create a page for modifying the password of the user.
 */
public class ModifyPasswordView extends AnchorPane {
    private final SceneUtils scene;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    private ModifyUserInfoListener listener;

    public ModifyPasswordView() {
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    @FXML
    private void confirmOnAction() {
        listener.confirmButtonClicked();
    }

    @FXML
    private void cancelOnAction() {
        listener.cancelButtonClicked();
    }

    public String getOldPasswordFieldText() {
        return oldPasswordField.getText();
    }

    public String getNewPasswordFieldOneText() {
        return newPasswordField.getText();
    }

    public String getNewPasswordFieldTwoText() {
        return confirmPasswordField.getText();
    }

    public void setListener(ModifyUserInfoListener listener) {
        this.listener = listener;
    }


}
