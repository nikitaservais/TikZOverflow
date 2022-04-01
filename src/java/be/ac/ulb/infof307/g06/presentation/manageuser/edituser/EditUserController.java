package be.ac.ulb.infof307.g06.presentation.manageuser.edituser;

import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.modifyemail.ModifyEmailController;
import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.modifypassword.ModifyPasswordController;
import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.modifyusername.ModifyUsernameController;
import be.ac.ulb.infof307.g06.presentation.mainmenu.MainMenuController;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.stage.Stage;

/**
 * Create a new page to modify the infos of the user.
 */
public class EditUserController implements EditUserListener {
    private final EditUserView editUserView;
    private final Stage primaryStage;
    private User user;

    public EditUserController(User user) {
        primaryStage = StageUtils.getStage();
        this.user = user;
        editUserView = new EditUserView(user);
        editUserView.setListener(this);
    }

    /**
     * Return to the main menu.
     */
    @Override
    public void exitButtonClicked() {
        MainMenuController mainMenuController = new MainMenuController(user);
        mainMenuController.show();
    }

    /**
     * Open the modify username page.
     */
    @Override
    public void modifyUsernameButtonClicked() {
        ModifyUsernameController modifyUsernameController = new ModifyUsernameController(primaryStage, user);
        modifyUsernameController.show();
    }

    /**
     * Open the modify email page.
     */
    @Override
    public void modifyEmailButtonClicked() {
        ModifyEmailController modifyEmailController = new ModifyEmailController(user);
        modifyEmailController.show();
    }

    /**
     * Open the modify password page.
     */
    @Override
    public void modifyPasswordButtonClicked() {
        ModifyPasswordController modifyPasswordController = new ModifyPasswordController(user);
        modifyPasswordController.show();
    }

    /**
     * Show the editUserView.
     */
    public void show() {
        primaryStage.setScene(editUserView.getScene());
        primaryStage.show();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
