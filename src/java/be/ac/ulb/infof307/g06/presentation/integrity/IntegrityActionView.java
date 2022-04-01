package be.ac.ulb.infof307.g06.presentation.integrity;

import be.ac.ulb.infof307.g06.utils.SceneUtils;
import be.ac.ulb.infof307.g06.models.ThemeModel;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Displays possibilities for the user between continuing from last commit or deleting the corrupted project.
 */
public class IntegrityActionView extends AnchorPane {
    private SceneUtils scene;
    Stage stage;
    private IntegrityActionListener integrityActionListener;

    /**
     * Constructor of the  IntegrityActionView class.
     * @param integrityActionListener the controller.
     */
    public IntegrityActionView(IntegrityActionListener integrityActionListener){
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
        this.getScene().getStylesheets().add(getClass().getResource("/styling/" + (new ThemeModel()).getTheme() +".css").toExternalForm());
        this.integrityActionListener = integrityActionListener;
    }

    /**
     * Ask the controller to open the project.
     */
    @FXML
    private void continueFromLastCommitClicked(){
        this.integrityActionListener.continueFromLastCommit();
        stage.close();
    }

    /**
     * Ask the controller to delete the project.
     */
    @FXML
    private void deletedProjectClicked(){
        this.integrityActionListener.deletedProject();
        stage.close();
    }

    /**
     * Displays the window.
     */
    public void display(){
        stage = new Stage();
        stage.setScene(this.scene);
        stage.setTitle("Integrity issue");
        stage.initOwner(StageUtils.getStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }
}
