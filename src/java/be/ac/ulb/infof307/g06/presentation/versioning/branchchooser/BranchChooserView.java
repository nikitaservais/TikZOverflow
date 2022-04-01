package be.ac.ulb.infof307.g06.presentation.versioning.branchchooser;

import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import be.ac.ulb.infof307.g06.models.branch.Branch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.List;

/**
 * This anchorpane is a window to let the user choose a branch.
 */
public class BranchChooserView extends AnchorPane {
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Label mergeLabel;
    @FXML
    private Label branchLabel;
    @FXML
    private Button mergeButton;
    @FXML
    private Button branchButton;
    private final SceneUtils scene;
    private BranchChooserListener listener;

    public BranchChooserView() {
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    @FXML
    private void initialize(){

    }

    /**
     * Displays the given branches
     * @param branches to display
     * @param parentView the parent view.
     */
    public void displayBranches(List<Branch> branches, String parentView) {
        if (parentView.equals(ConstantsUtils.mergeString)) {
            branchLabel.setVisible(false);
        } else {
            mergeLabel.setVisible(false);
        }
        if (!branches.isEmpty()) {
            for (Branch branch : branches) {
                comboBox.getItems().add(branch.getBranchName());
            }
            comboBox.setValue(branches.get(0).getBranchName());
        } else {
            comboBox.setValue("No branch available");
            mergeButton.setDisable(true);
            branchButton.setDisable(true);
        }
    }

    /**
     * Asks the listener to stop the choosing
     * @param event the click
     */
    @FXML
    private void cancelOnAction(ActionEvent event) {
        listener.cancelButtonClicked();
    }

    /**
     * Asks the listener to accept the selected branch
     * @param event the click
     */
    @FXML
    private void mergeOnAction(ActionEvent event) {
        listener.mergeButtonClicked();
    }

    /**
     * Asks the listener to change to the selected branch.
     * @param event the click
     */
    @FXML
    private void branchOnAction(ActionEvent event) {
        listener.branchButtonClicked();
    }

    /**
     * Returns the selected branch
     * @return the value of the comboBox.
     */
    public String getChosenBranchName() {
        return comboBox.getValue();
    }

    /**
     * Changes the listener
     * @param branchChooserListener to be linked to the view
     */
    public void setListener(BranchChooserListener branchChooserListener) {
        listener = branchChooserListener;
    }

    /**
     * set what is visible depending on what was called
     * @param mode indication on what was called
     */
    public void setParentView(String mode) {
        if (mode.equals(ConstantsUtils.mergeString)) {
            branchButton.setVisible(false);
        } else {
            mergeButton.setVisible(false);
        }
    }
}
