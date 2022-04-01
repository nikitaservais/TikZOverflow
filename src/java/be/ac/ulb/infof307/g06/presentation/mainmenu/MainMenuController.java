package be.ac.ulb.infof307.g06.presentation.mainmenu;

import be.ac.ulb.infof307.g06.presentation.manageuser.edituser.EditUserController;
import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.manageproject.createproject.CreateProjectController;
import be.ac.ulb.infof307.g06.presentation.manageproject.ManageProjectController;
import be.ac.ulb.infof307.g06.presentation.help.HelpController;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Control MainMenuView
 */
public class MainMenuController implements MainMenuListener {
    private final MainMenuView mainMenuView;
    private final Stage primaryStage;
    private final User user;

    /**
     * Class constructor
     * @param user The instance of the current user.
     */
    public MainMenuController(User user) {
        primaryStage = StageUtils.getStage();
        this.user = user;
        mainMenuView = new MainMenuView();
        mainMenuView.setListener(this);
    }

    /**
     * Displays the fileManager. The user can then navigate his files to find
     * a place to create a new Project.
     */
    @Override
    public void createProjectButtonClicked() {
        CreateProjectController createProjectController = new CreateProjectController(user);
        createProjectController.show();
    }

    /**
     * Opens the Project manager to renaming/delete etc. projects
     */
    @Override
    public void openProjectButtonClicked() {
        try {
            ManageProjectController manageProjectController = new ManageProjectController(user);
            manageProjectController.show();
        } catch (DatabaseConnectionException e) {
            new ErrorMessage("Error while opening the project");
        }
    }

    /**
     * Opens the user edition menu
     */
    @Override
    public void editUserButtonClicked() {
        EditUserController editUserController = new EditUserController(user);
        editUserController.show();
    }

    /**
     * Exits the program
     */
    @Override
    public void exitButtonClicked() {
        Platform.exit();
    }

    /**
     * Calls help controller to display help window.
     */
    @Override
    public void help() {
        HelpController helpController = new HelpController();
        helpController.show();
    }

    /**
     * Displays the linked view
     */
    public void show() {
        primaryStage.setScene(mainMenuView.getScene());
        primaryStage.setTitle("Main Menu");
        primaryStage.show();
    }
}

