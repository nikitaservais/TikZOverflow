package be.ac.ulb.infof307.g06.presentation.versioning.createcommit;

import be.ac.ulb.infof307.g06.exceptions.CommitException;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 * View for commit pop-up; allowing the user to enter a message for the commit
 */
public class CreateCommitView extends BorderPane {
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Button confirmButton;

    private SceneUtils scene;
    private CreateCommitListener listener;

    public CreateCommitView() {
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    /**
     * called when user clicks on the confirm button
     * @param event the mouse event.
     * @throws CommitException
     */
    @FXML
    public void confirmButtonClicked(ActionEvent event) throws CommitException {
        String message = messageTextArea.getText();
        listener.confirmButtonClicked(message);
    }

    public void setListener(CreateCommitListener listener) {
        this.listener = listener;
    }

}
