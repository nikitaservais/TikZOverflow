package be.ac.ulb.infof307.g06.presentation.versioning.createcommit;

import be.ac.ulb.infof307.g06.exceptions.CommitException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.models.commit.CommitManager;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Controls the creation of commits for the Project
 */
public class CreateCommitController implements CreateCommitListener {
    private final Stage primaryStage;
    private CommitManager commitManager;
    private CreateCommitView createCommitView;
    private Stage stage;

    /**
     * Class constructor
     * @param projectID id of the Project
     * @param branchID id of the branch
     * @param newLines added lines in commit
     * @param removedLines removed lines in commit
     * @throws DatabaseConnectionException
     */
    public CreateCommitController(int projectID, int branchID, ArrayList<String> newLines, ArrayList<String> removedLines) throws DatabaseConnectionException {
        this.primaryStage = StageUtils.getStage();
        commitManager = new CommitManager(projectID, branchID, newLines, removedLines);
        createCommitView = new CreateCommitView();
        createCommitView.setListener(this);
    }

    public void show () {
        stage = new Stage();
        stage.setScene(createCommitView.getScene());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setTitle("New Commit");
        stage.showAndWait();
    }

    /**
     * Creates a commit with the given message
     * @param message of the commit
     */
    @Override
    public void confirmButtonClicked(String message)  {
        if (!message.isEmpty()) {
            try {
                commitManager.createNewCommit(message);
                stage.close();
            }
            catch (CommitException e) {
                new ErrorMessage("Commit failed");
            }
        }
        else {
            new ErrorMessage( "Please enter a commit message");
        }
    }
}


