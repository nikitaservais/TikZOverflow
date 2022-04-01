package be.ac.ulb.infof307.g06.presentation.manageuser.edituser.modifyemail;


import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.ModifyUserDataException;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.models.user.UserManager;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.EditUserController;
import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.ModifyUserInfoListener;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Create a new page to modify the email of the user.
 * Verify the infos before modifying.
 */
public class ModifyEmailController implements ModifyUserInfoListener {
    private final ModifyEmailView modifyEmailView;
    private final Stage primaryStage;
    private final User user;
    private final Stage stage;

    public ModifyEmailController(User user) {
        primaryStage = StageUtils.getStage();
        this.user = user;
        modifyEmailView = new ModifyEmailView(user);
        modifyEmailView.setListener(this);
        stage = new Stage();
    }

    /**
     * Try to modify the email.
     */
    @Override
    public void confirmButtonClicked() {
        updatingEmail();
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
        stage.setScene(modifyEmailView.getScene());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.show();
    }

    /**
     * Verify if the email is valid and available, then modify it.
     * Return to the edit user page.
     */
    private void updatingEmail() {
        try {
            String newEmail = modifyEmailView.getEmailFieldText();
            String message = new UserManager().updateEmail(user.getId(), newEmail);
            if (message.isEmpty()) {
                user.setEmail(newEmail);
                EditUserController editUserController = new EditUserController(user);
                editUserController.setUser(user);
                editUserController.show();
            } else {
                new ErrorMessage(message);
            }
        } catch (ModifyUserDataException | DatabaseConnectionException e) {
            new ErrorMessage("Error while updating mail");
        }
    }
}
