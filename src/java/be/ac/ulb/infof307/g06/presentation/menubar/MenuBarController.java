package be.ac.ulb.infof307.g06.presentation.menubar;

import be.ac.ulb.infof307.g06.exceptions.CommitException;
import be.ac.ulb.infof307.g06.exceptions.DataAccessException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.exceptions.VersioningException;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.MenuBarEditingControllerListener;
import be.ac.ulb.infof307.g06.presentation.help.HelpController;
import be.ac.ulb.infof307.g06.presentation.mainmenu.MainMenuController;
import be.ac.ulb.infof307.g06.presentation.theme.ThemeController;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import be.ac.ulb.infof307.g06.presentation.versioning.branchchooser.BranchChooserController;
import be.ac.ulb.infof307.g06.presentation.versioning.createbranch.CreateBranchController;
import be.ac.ulb.infof307.g06.presentation.versioning.revert.RevertController;
import be.ac.ulb.infof307.g06.presentation.versioning.commitcontent.CommitContentController;
import be.ac.ulb.infof307.g06.presentation.versioning.commithistory.CommitHistoryController;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller of the top's Bar Window
 * Call all the project management, help and version control.
 */
public class MenuBarController implements MenuBarListener {
    private final Stage primaryStage;
    private final MenuBarView menuBarView;
    private final Project project;
    private MenuBarEditingControllerListener menuBarEditingControllerListener;

    public MenuBarController(Project project) {
        this.primaryStage = StageUtils.getStage();
        this.project = project;
        menuBarView = new MenuBarView();
        menuBarView.setListener(this);
    }

    /**
     * Exits the program
     */
    @Override
    public void exitMenuItemClicked() {
        Platform.exit();
    }

    /**
     * Displays the help menu
     */
    @Override
    public void helpMenuItemClicked() {
        HelpController helpController = new HelpController();
        helpController.show();
    }

    /**
     * Displays the theme menu
     */
    @Override
    public void themeMenuItemClicked() {
        ThemeController themeController = new ThemeController();
        themeController.show();
    }

    /**
     * Saves the current Project
     */
    @Override
    public void saveProject() {
        menuBarEditingControllerListener.saveProject();
    }

    /**
     * Closes the current Project
     */
    @Override
    public void closeProject() {
        MainMenuController mainMenuController = new MainMenuController(project.getUser());
        primaryStage.setMaximized(false);
        mainMenuController.show();
    }

    /**
     * Imports the Project from .tar.gz
     */
    @Override
    public void importProjectButtonClicked() {
        menuBarEditingControllerListener.importProjectButtonClicked();
    }

    /**
     * Exports the Project in .tar.gz
     */
    @Override
    public void exportProjectButtonClicked() {
        menuBarEditingControllerListener.exportProjectButtonClicked();
    }

    @Override
    public void commit() {
        menuBarEditingControllerListener.createCommit();
    }

    /**
     * Call the correct controller to revert a commit.
     */
    @Override
    public void revertCommit() {
        RevertController revertController = new RevertController(project.getId());
        revertController.setMenuBarEditingControllerListener(menuBarEditingControllerListener);
        try {
            revertController.getCommits();
        } catch (VersioningException | CommitException e) {
            new ErrorMessage("Error while reverting the last commit.");
        }
        revertController.show();
    }

    /**
     * Call the correct controller to create a new branch.
     */
    @Override
    public void createNewBranch() {
        CreateBranchController createBranchController = new CreateBranchController(project);
        createBranchController.setMenuBarEditingControllerListener(menuBarEditingControllerListener);
        createBranchController.show();
    }

    /**
     * Call the correct controller to change to another branch.
     */
    @Override
    public void chooseExistingBranch() {
        try {
            String branchName = menuBarEditingControllerListener.getBranchName();
            BranchChooserController branchChooserController = new BranchChooserController(project, ConstantsUtils.branchString, branchName);
            branchChooserController.setMenuBarEditingControllerListener(menuBarEditingControllerListener);
            branchChooserController.show();
        } catch (VersioningException | DatabaseConnectionException e) {
            new ErrorMessage("Error while choosing an existing branch");
        }
    }

    /**
     * Call the correct controller to display the commit history.
     */
    @Override
    public void history() {
        CommitHistoryController commitHistoryController = new CommitHistoryController(project);
        commitHistoryController.show();
    }

    /**
     * Call the correct controller to merge a specific branch to master.
     */
    @Override
    public void merge() {
        try {
            String branchName = menuBarEditingControllerListener.getBranchName();
            BranchChooserController branchChooserController = new BranchChooserController(project, ConstantsUtils.mergeString, branchName);
            branchChooserController.setMenuBarEditingControllerListener(menuBarEditingControllerListener);
            branchChooserController.show();
        } catch (VersioningException | DatabaseConnectionException e) {
            new ErrorMessage("Error while merging");
        }

    }

    /**
     * Call the correct controller to display the commit content.
     */
    @Override
    public void displayCommitContent() {
        try {
            project.updateCommits();
            CommitContentController commitContentController = new CommitContentController(project);
            commitContentController.show();
        } catch (DatabaseConnectionException | DataAccessException |IOException e) {
            new ErrorMessage("An Error has occurred while trying to display your commit content.");
        }
    }

    /**
     * Asks the user a path to exporting the Project
     * @return the selected path
     */
    @Override
    public String getPath() {
        return menuBarView.getPath(primaryStage);
    }

    /**
     * @return the associated view
     */
    public MenuBarView getMenuBarView() {
        return menuBarView;
    }

    /**
     * @param menuBarEditingControllerListener the link between view and controller
     */
    public void setMenuBarEditingControllerListener(MenuBarEditingControllerListener menuBarEditingControllerListener) {
        this.menuBarEditingControllerListener = menuBarEditingControllerListener;
    }
}