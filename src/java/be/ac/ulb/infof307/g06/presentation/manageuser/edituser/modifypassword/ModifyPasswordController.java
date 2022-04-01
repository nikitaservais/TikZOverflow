package be.ac.ulb.infof307.g06.presentation.manageuser.edituser.modifypassword;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.HashingException;
import be.ac.ulb.infof307.g06.exceptions.ModifyUserDataException;
import be.ac.ulb.infof307.g06.utils.PasswordUtils;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.models.user.UserManager;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.EditUserController;
import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.ModifyUserInfoListener;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Create a new page to modify the password of the user.
 * Verify the infos before modifying.
 */
public class ModifyPasswordController implements ModifyUserInfoListener {
    private final ModifyPasswordView modifyPasswordView;
    private final Stage primaryStage;
    private final User user;
    private final Stage stage;


    public ModifyPasswordController(User user) {
        primaryStage = StageUtils.getStage();
        this.user = user;
        modifyPasswordView = new ModifyPasswordView();
        modifyPasswordView.setListener(this);
        stage = new Stage();
    }

    @Override
    public void confirmButtonClicked() {
        updatingPassword();
        stage.close();
    }

    /**
     * Return to the edit user page.
     */
    @Override
    public void cancelButtonClicked() {
        stage.close();
    }

    /**
     * Show the modifyEmailView.
     */
    public void show() {
        stage.setScene(modifyPasswordView.getScene());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.show();
    }

    /**
     * Verify if the old password is the same and if the new one is valid, then modify it.
     * Return to the edit user page.
     */
    private void updatingPassword() {
        try {
            String oldPassword = PasswordUtils.hashPassword(modifyPasswordView.getOldPasswordFieldText());
            String newPasswordOne = modifyPasswordView.getNewPasswordFieldOneText();
            String newPasswordTwo = modifyPasswordView.getNewPasswordFieldTwoText();
            UserManager account = new UserManager();

            String message = account.updatePassword(user.getId(), oldPassword, newPasswordOne, newPasswordTwo);
            if (message.isEmpty()) {
                user.setPassword(newPasswordOne);
                EditUserController editUserController = new EditUserController(user);
                editUserController.setUser(user);
                editUserController.show();
            } else {
                new ErrorMessage(message);
            }
        } catch (DatabaseConnectionException | ModifyUserDataException | HashingException e) {
            new ErrorMessage("Error trying to update password");
        }
    }
}
