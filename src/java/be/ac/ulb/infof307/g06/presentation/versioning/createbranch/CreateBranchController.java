package be.ac.ulb.infof307.g06.presentation.versioning.createbranch;

import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.MenuBarEditingControllerListener;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import be.ac.ulb.infof307.g06.models.branch.BranchManager;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class that controls the creation of branches for the Project
 */
public class CreateBranchController implements CreateBranchListener {
    private final Stage primaryStage;
    private CreateBranchView view;
    private Stage stage;
    private Project project;
    private MenuBarEditingControllerListener menuBarEditingControllerListener;
    private int branchID;

    /**
     * Class constructor
     * @param project the instance of the current project.
     */
    public CreateBranchController(Project project) {
        this.primaryStage = StageUtils.getStage();
        view = new CreateBranchView();
        view.setListener(this);
        this.project = project;
        this.branchID = 0;
    }

    public void show () {
        stage = new Stage();
        stage.setScene(view.getScene());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setTitle("Create branch");
        stage.show();
    }

    /**
     * Creates a branch when button clicked
     * @param branchName name of the branch
     */
    @Override
    public void confirmButtonClicked(String branchName) {
        if (!branchName.isEmpty()) {
            try {
                branchID = new BranchManager().newBranch(project.getId(), branchName);
                menuBarEditingControllerListener.commitAfterNewBranch(new BranchManager().getBranch(project.getId(),branchID));
            } catch (VersioningException | DatabaseException | IOException | DataAccessException | InvalidDriverException | CommitException e) {
                new ErrorMessage("Error while creating new branch.");
            }
            stage.close();
        }
        else {
            new ErrorMessage("Please enter a branch name");
        }
    }

    /**
     * Sets the class listener
     * @param menuBarEditingControllerListener listener
     */
    public void setMenuBarEditingControllerListener(MenuBarEditingControllerListener menuBarEditingControllerListener) {
        this.menuBarEditingControllerListener = menuBarEditingControllerListener;
    }

    /**
     * Gets the id of the branch created
     * @return the branchID
     */
    public int getBranchID() {
        return branchID;
    }

    /**
     * sets the ID of the branch
     * @param branchID id of the branch
     */
    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }
}


