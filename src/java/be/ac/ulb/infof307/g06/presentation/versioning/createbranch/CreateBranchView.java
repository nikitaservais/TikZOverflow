package be.ac.ulb.infof307.g06.presentation.versioning.createbranch;

import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * view for branch pop-up; allowing the user to enter the name of the branch
 */
public class CreateBranchView extends BorderPane {
    @FXML
    private TextField branchNameTextField;
    @FXML
    private Button confirmButton;

    private SceneUtils scene;
    private CreateBranchListener listener;

    public CreateBranchView() {
        scene = new SceneUtils(this);
        FxmlUtils.loadFxml(this);
    }

    /**
     * called when user clicks on the confirm button
     * @param event the mouse event.
     */
    @FXML
    public void confirmButtonClicked(ActionEvent event){
        String branchName = branchNameTextField.getText();
        listener.confirmButtonClicked(branchName);
    }

    public void setListener(CreateBranchListener listener) {
        this.listener = listener;
    }

}
