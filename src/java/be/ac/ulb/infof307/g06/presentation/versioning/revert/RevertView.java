package be.ac.ulb.infof307.g06.presentation.versioning.revert;

import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class RevertView extends AnchorPane {
    @FXML
    private ComboBox<Integer> commitComboBox;
    @FXML
    private Button confirmButton;

    private SceneUtils scene;
    private RevertController listener;

    public RevertView() {
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    /**
     * called when user clicks on the confirm button
     */
    @FXML
    public void confirmButtonClicked(ActionEvent event){
        listener.confirmButtonClicked(commitComboBox.getValue());
    }

    /**
     * Closes on cancel
     */
    @FXML
    public void cancelButtonClicked() {
        this.listener.cancelButtonClicked();
    }

    /**
     * Sets the commit combobox with the commit numbers
     * @param commits
     */
    public void setCommitsComboBox(List<Integer> commits) {
        this.commitComboBox.setItems(FXCollections.observableArrayList(commits));
    }
    
    public Integer getCommitId() {
        return commitComboBox.getValue();
    }

    public void setListener(RevertController listener) {
        this.listener = listener;
    }
}
