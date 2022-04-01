package be.ac.ulb.infof307.g06.presentation.manageuser.edituser.modifyusername;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.ModifyUserDataException;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.models.user.UserManager;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.EditUserController;
import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.ModifyUserInfoListener;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Create a new page to modify the username of the user.
 * Verify the infos before modifying.
 */
public class ModifyUsernameController implements ModifyUserInfoListener {
    private final ModifyUsernameView modifyUsernameView;
    private final Stage primaryStage;
    private final User user;
    private final Stage stage;

    public ModifyUsernameController(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
        modifyUsernameView = new ModifyUsernameView(user);
        modifyUsernameView.setListener(this);
        stage = new Stage();
    }

    @Override
    public void confirmButtonClicked() {
        updatingUsername();
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
     * Show the modifyUsernameView.
     */
    public void show() {
        stage.setScene(modifyUsernameView.getScene());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.show();
    }

    /**
     * Verify if the username is valid and available, then modify it.
     * Return to the edit user page.
     */
    private void updatingUsername() {
        try {
            String newUsername = modifyUsernameView.getUsernameFieldText();
            UserManager account = new UserManager();
            String message = account.updateUsername(user.getId(), newUsername);
            if (message.isEmpty()) {
                user.setUsername(newUsername);
                EditUserController editUserController = new EditUserController(user);
                editUserController.setUser(user);
                editUserController.show();
            } else {
                new ErrorMessage(message);
            }

        } catch (ModifyUserDataException | DatabaseConnectionException e) {
            new ErrorMessage("Error trying to update username");
        }
    }
}
