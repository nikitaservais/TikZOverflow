package be.ac.ulb.infof307.g06.presentation.versioning.revert;

import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.branch.RevertModel;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.MenuBarEditingControllerListener;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import be.ac.ulb.infof307.g06.utils.StringCompareUtils;
import be.ac.ulb.infof307.g06.models.VersionTextBuilder;
import be.ac.ulb.infof307.g06.presentation.versioning.difference.DifferenceController;
import javafx.stage.Modality;

import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Update the console and canvas after a revert.
 */
public class RevertController implements RevertControllerListener {
    private RevertView revertView;
    private MenuBarEditingControllerListener menuBarEditingControllerListener;
    private int projectID;
    private Stage stage;
    private RevertModel revertModel;

    /**
     * Default constructor for RevertController class.
     * @param projectID of the current project, on which we revet.
     */
    public RevertController(int projectID){
        this.projectID = projectID;
        this.revertView = new RevertView();
        revertView.setListener(this);
    }

    /**
     * When confirm is clicked, uses the selected commitID to revert
     * @param commitId to revert
     */
    public void confirmButtonClicked(int commitId) {
        try {
            if (showRevertDifferences(commitId)) {
                revertModel = new RevertModel(this.getBranch(this.menuBarEditingControllerListener.getBranchName()), projectID);
                String code = revertModel.launchRevert(commitId, projectID, this.menuBarEditingControllerListener.getBranchName());
                this.updateConsoleCanvas(code);
                stage.close();
            }
        } catch (VersioningException | DataAccessException | CommitException | DatabaseConnectionException e) {
            new ErrorMessage("Error while trying to revert.");
        }
    }

    /**
     * show the differences between current content and the reverted commit
     * @param commitId the id of the commit we want revert to
     * @return if the used has agreed on the revert
     */
    private boolean showRevertDifferences(int commitId) throws VersioningException, DataAccessException {
        DifferenceController differenceController = new DifferenceController(stage,commitId);
        String metadataPath = this.getBranch(this.menuBarEditingControllerListener.getBranchName()).getPathToMetaData();
        String currentText = null;
        String revertedText = null;
        try {
            currentText = this.getBranch(this.menuBarEditingControllerListener.getBranchName()).getText();
            VersionTextBuilder versionTextBuilder = new VersionTextBuilder(metadataPath);
            revertedText = versionTextBuilder.getTextFrom(commitId);
        } catch (IOException | DatabaseConnectionException e) {
            throw new VersioningException(e);
        }
        ArrayList<ArrayList<String>> deletedAdded = new ArrayList<>();
        deletedAdded.add(StringCompareUtils.getDeletions(currentText,revertedText));
        deletedAdded.add(StringCompareUtils.getInsertions(currentText,revertedText));
        differenceController.initializeTextArea(StringCompareUtils.splitIntoArrayList(currentText),deletedAdded);
        differenceController.show();
        return differenceController.getState();
    }

    /**
     * Closes on cancel
     */
    public void cancelButtonClicked() {
        stage.close();
    }

    /**
     * Shows a new window to let the user chose a commit to revert to
     */
    public void show() {
        stage = new Stage();
        stage.setScene(revertView.getScene());
        stage.initOwner(StageUtils.getStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    /**
     * provides the chosen branch
     * @return Project completed with all infos expect user's name
     */
    public Project getBranch(String branchName) throws DataAccessException {
        return this.menuBarEditingControllerListener.getBranch(branchName);
    }

    /**
     * Gets the commits of the Project and provides them to the interface
     */
    public void getCommits() throws VersioningException, CommitException {
        this.revertView.setCommitsComboBox(revertModel.getCommits(this.menuBarEditingControllerListener.getBranchName()));
    }

    /**
     *Method to update the console and the canvas after a revert
     * @param content the file content after the revert construction.
     */
    @Override
    public void updateConsoleCanvas(String content){
        menuBarEditingControllerListener.updateConsoleCanvas(content);
    }

    public void setMenuBarEditingControllerListener(MenuBarEditingControllerListener menuBarEditingControllerListener) {
        this.menuBarEditingControllerListener = menuBarEditingControllerListener;
        try {
            String branchName = this.menuBarEditingControllerListener.getBranchName();
            Project branch = this.getBranch(branchName);
            revertModel = new RevertModel(branch, projectID);
        } catch (Exception E) {
            new ErrorMessage("An error occurred while trying to get branches infos");
        }
    }
}
