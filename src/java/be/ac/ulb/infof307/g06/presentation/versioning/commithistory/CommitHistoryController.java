package be.ac.ulb.infof307.g06.presentation.versioning.commithistory;

import be.ac.ulb.infof307.g06.exceptions.CommitException;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.models.commit.CommitHistoryModel;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import be.ac.ulb.infof307.g06.models.branch.Branch;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes care of the controller side of the display the commit history information
 * depending on the branch selected of a given Project mvc.
 */
public class CommitHistoryController implements CommitHistoryListener {
    private final Stage primaryStage;
    private Stage stage;
    private CommitHistoryView view;
    private Project project;
    private Integer projectId;
    private CommitHistoryModel commitHistoryModel;

    /**
     * Constructor of the CommitHistoryController Class.
     * @param project The Project of the commit history info to display.
     */
    public CommitHistoryController(Project project){
        this.project = project;
        this.projectId = project.getId();
        this.primaryStage = StageUtils.getStage();

        try {
            this.commitHistoryModel = new CommitHistoryModel(this.projectId);
        } catch (CommitException e) {
            new ErrorMessage("Error while retrieving commit content.");
        }
        view = new CommitHistoryView();
        view.setListener(this);
        view.setComboBoxBranchNames();
    }

    /**
     * Method of the CommitHistoryController Class needed by the view to get the info of the branches of a given
     * Project.
     * @return ArrayList of Branch - A list of branch data of a given Project.
     */
    @Override
    public List<Branch> getBranchesInfo(){
        return project.getBranches();
    }

    /**
     * Method of the CommitHistoryController Class needed by the view to get the info of the commits of the branch
     * given in parameter of a given Project.
     * @param branchId Integer - The branch id of the Project to display the commit history.
     * @return ArrayList of ArrayList of String - The list of info of the commits.
     */
    @Override
    public ArrayList<ArrayList<String>> getCommitsInfo(Integer branchId) {
        return commitHistoryModel.getBranchInfo(branchId);
    }

    /**
     * Method that displays the view.
     */
    public void show(){
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(view.getScene());
        stage.setTitle("Commit history");
        stage.show();
    }
}
