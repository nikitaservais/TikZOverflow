package be.ac.ulb.infof307.g06.presentation.versioning.commitcontent;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.models.commit.CommitContentModel;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controlling the content of the commits
 */
public class CommitContentController {
    private final Stage primaryStage;
    private final CommitContentView view;
    private final CommitContentModel commitContentModel;
    private Project project;
    private Integer projectID;
    private Stage stage;

    /**
     * Constructor of the CommitContentController
     * @param project the current opened project
     * @throws DatabaseConnectionException
     */
    public CommitContentController(Project project) throws DatabaseConnectionException {
        this.project = project;
        this.projectID = project.getId();
        primaryStage = StageUtils.getStage();
        commitContentModel = new CommitContentModel(project);
        view = new CommitContentView();
        initView();
    }

    /**
     * Shows a new window to let the user chose the branch for merging
     */
    public void show() {
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(view.getScene());
        stage.setTitle("Display commit content");
        stage.show();
    }

    private void initView() {
            view.initComboBox(project.getBranches());
    }

    /**
     * Gets the id of the current Project
     * @return projectID
     */
    public Integer getProjectID() {
        return projectID;
    }

    /**
     * Sets the value of the current Project
     * @param projectID the project id.
     */
    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }
}
