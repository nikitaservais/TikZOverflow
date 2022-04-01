package be.ac.ulb.infof307.g06.presentation.versioning.branchchooser;


import be.ac.ulb.infof307.g06.exceptions.CommitException;
import be.ac.ulb.infof307.g06.exceptions.DataAccessException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.ValueNotFoundException;
import be.ac.ulb.infof307.g06.exceptions.VersioningException;
import be.ac.ulb.infof307.g06.models.branch.Branch;
import be.ac.ulb.infof307.g06.models.branch.BranchChooserModel;
import be.ac.ulb.infof307.g06.models.branch.BranchManager;
import be.ac.ulb.infof307.g06.models.branch.MergeModel;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.MenuBarEditingControllerListener;
import be.ac.ulb.infof307.g06.presentation.versioning.difference.DifferenceController;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import be.ac.ulb.infof307.g06.utils.StringCompareUtils;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class controls the window to let the user select a branch in which the current
 * branch should be merged in.
 */
public class BranchChooserController implements BranchChooserListener {
    private final Stage primaryStage;
    String chosenBranchName;
    Project project;
    String actualBranchName;
    BranchChooserView branchChooserView;
    BranchChooserModel branchChooserModel;
    Stage stage;
    BranchManager branchManager;
    MenuBarEditingControllerListener menuBarEditingControllerListener;

    /**
     * Create the controller for the branch choose.
     * @param project          current project, on which we create the branch.
     * @param parentView       specific name, to set the view according to what we want.
     *                         "Merge" to show the merge part.
     *                         "Branch" to show to branch change part
     * @param actualBranchName name of the actual branch
     * @throws DatabaseConnectionException exception gotten when database connection fails
     */
    public BranchChooserController(Project project, String parentView, String actualBranchName) throws DatabaseConnectionException {
        primaryStage = StageUtils.getStage();
        this.project = project;
        this.actualBranchName = actualBranchName;
        chosenBranchName = ConstantsUtils.emptyString;
        branchChooserModel = new BranchChooserModel(project.getId());
        branchChooserView = new BranchChooserView();
        branchChooserView.setParentView(parentView);
        branchChooserView.setListener(this);
        branchManager = new BranchManager();
        displayBranches(parentView);
    }

    @Override
    public void cancelButtonClicked() {
        stage.close();
    }

    /**
     * Gets the selected branch to merge, then closes the window.
     */
    @Override
    public void mergeButtonClicked() {
        chosenBranchName = branchChooserView.getChosenBranchName();
        try {
            MergeModel mergeModel = new MergeModel(getBranch(actualBranchName), getBranch(chosenBranchName));
            boolean choice = showDifferences(chosenBranchName, mergeModel.getDeletedAndInsertedLines());
            if (choice) {
                mergeModel.update();
                String projectContent = branchManager.getTextFromBranch(project.getId(), actualBranchName);
                menuBarEditingControllerListener.updateConsoleCanvas(projectContent);
                menuBarEditingControllerListener.updateBranchID(actualBranchName);
                stage.close();
            }
        } catch (VersioningException | CommitException | ValueNotFoundException | DataAccessException | IOException | DatabaseConnectionException e) {
            new ErrorMessage("Error while merging branches.");
        }
    }

    /**
     * Gets the selected branch to change branch, then closes the window.
     */
    @Override
    public void branchButtonClicked() {
        chosenBranchName = branchChooserView.getChosenBranchName();
        try {
            String projectContent = branchManager.getTextFromBranch(project.getId(), chosenBranchName);
            menuBarEditingControllerListener.updateConsoleCanvas(projectContent);
            menuBarEditingControllerListener.updateBranchID(chosenBranchName);
        } catch (VersioningException | ValueNotFoundException e) {
            new ErrorMessage("Error while switching branches.");
        }
        stage.close();
    }

    /**
     * Asks to the view to display the branch of the current Project
     * @param parentView The parent view.
     */
    public void displayBranches(String parentView) {
        List<Branch> branches = project.getBranches();
        for (int i = 0; i < branches.size(); i++) {
            if (branches.get(i).getBranchName().equals(actualBranchName)) {
                branches.remove(i);
            }
        }
        branchChooserView.displayBranches(branches, parentView);
    }

    /**
     * Shows a new window to let the user chose the branch for merging
     */
    public void show() {
        stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(branchChooserView.getScene());
        stage.show();
    }

    /**
     * Provides the chosen branch.
     * @param branchName the branch name.
     * @return Project completed with all infos expect user's name
     * @throws DataAccessException
     * @throws DatabaseConnectionException
     */
    public Project getBranch(String branchName) throws DataAccessException, DatabaseConnectionException {
        return branchChooserModel.getBranch(branchName);
    }

    /**
     * Initialize and show the difference View and depending if the user will chose ok or cancel it will merge or not
     * @param chosenBranchName the name of the branch select for the merge into the current one
     * @param deletedInserted the deletes inserted.
     * @return a boolean indicating if the merge was accepted
     */
    private boolean showDifferences(String chosenBranchName, ArrayList<ArrayList<String>> deletedInserted) {
        String currentProjectContent = null;
        try {
            currentProjectContent = branchManager.getTextFromBranch(project.getId(), actualBranchName);
        } catch (VersioningException | ValueNotFoundException e) {
            new ErrorMessage("Error while retrieving differences.");
        }
        DifferenceController differenceController = new DifferenceController(stage, ConstantsUtils.mergeString, actualBranchName, chosenBranchName);
        differenceController.initializeTextArea(StringCompareUtils.splitIntoArrayList(currentProjectContent), deletedInserted);
        differenceController.show();
        return differenceController.getState();
    }

    public Integer getProjectID() {
        return project.getId();
    }

    /**
     * Set a listener to communicate with the editing controller.
     * @param menuBarEditingControllerListener the the editing controller.
     */
    public void setMenuBarEditingControllerListener(MenuBarEditingControllerListener menuBarEditingControllerListener) {
        this.menuBarEditingControllerListener = menuBarEditingControllerListener;
    }

}
