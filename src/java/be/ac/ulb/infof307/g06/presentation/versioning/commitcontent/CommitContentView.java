package be.ac.ulb.infof307.g06.presentation.versioning.commitcontent;

import be.ac.ulb.infof307.g06.presentation.manageproject.editing.console.ConsoleView;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import be.ac.ulb.infof307.g06.models.ThemeModel;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import be.ac.ulb.infof307.g06.models.tikz.TikzHighlight;
import be.ac.ulb.infof307.g06.models.branch.Branch;
import be.ac.ulb.infof307.g06.models.commit.Commit;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for the GUI of the commit content
 */
public class CommitContentView extends AnchorPane {
    private final SceneUtils scene;
    private CodeArea addLineCodeArea;
    private CodeArea removeLineCodeArea;
    @FXML
    private VBox addLineVBox;
    @FXML
    private VBox removeLineVBox;
    @FXML
    private ComboBox<Commit> commitComboBox;
    @FXML
    private ComboBox<Branch> branchesComboBox;

    /**
     * CommitContentView constructor
     */
    public CommitContentView() {
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    /**
     * Displays the new lines from the commit on the GUI
     * @param newLines the new lines.
     */
    public void displayNewLines(ArrayList<String> newLines) {
        StringBuilder newLinesString = new StringBuilder();
        for (String line : newLines) {
            newLinesString.append(line).append('\n');
        }
        addLineCodeArea.replaceText(newLinesString.toString());
    }

    /**
     * Displays the removed lines from the commit on the GUI
     * @param removedLines the removed lines.
     */
    public void displayRemovedLines(ArrayList<String> removedLines) {
        StringBuilder removedLinesString = new StringBuilder();
        for (String line : removedLines) {
            removedLinesString.append(line).append('\n');
        }
        removeLineCodeArea.replaceText(removedLinesString.toString());
    }

    /**
     * Sets the branch combobox with the branch numbers
     * @param branches List of branches
     */
    public void initComboBox(List<Branch> branches) {
        branchesComboBox.getItems().setAll(branches);
        branchesComboBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            // called when a branch is selected
            commitComboBox.getItems().setAll(newValue.getCommits());
            commitComboBox.getSelectionModel().selectFirst();
        }));
        branchesComboBox.getSelectionModel().selectFirst();
        commitComboBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            // called when a branch is selected
            if (newValue != null) {
                displayNewLines(newValue.getNewLines());
                displayRemovedLines(newValue.getRemovedLines());
            }
        }));
        commitComboBox.getSelectionModel().selectFirst();
        Commit selectedItem = commitComboBox.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            displayNewLines(selectedItem.getNewLines());
            displayRemovedLines(selectedItem.getRemovedLines());
        }
    }

    /**
     * Initialize the code area for all the lines added in the commit.
     * Adds all the styling and properties.
     */
    private void initAddLineCodeArea() {
        addLineCodeArea = new CodeArea();
        addLineCodeArea.setEditable(false);
        VirtualizedScrollPane virtualizedScrollPane = new VirtualizedScrollPane<>(addLineCodeArea);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(virtualizedScrollPane);
        addLineVBox.getChildren().add(stackPane);
        VBox.setVgrow(stackPane, Priority.ALWAYS);
        getStylesheets().add(getClass().getResource("/styling/" + (new ThemeModel()).getTheme() + ".css").toExternalForm());
        addLineCodeArea.setParagraphGraphicFactory(LineNumberFactory.get(addLineCodeArea));
        addLineCodeArea.textProperty().addListener((obs, oldText, newText) -> addLineCodeArea.setStyleSpans(0, new TikzHighlight().highlightText(newText)));
    }

    /**
     * Initialize the code area for all the lines removed in the commit.
     * Adds all the styling and properties.
     */
    private void initRemoveLineCodeArea() {
        removeLineCodeArea = new CodeArea();
        removeLineCodeArea.setEditable(false);
        VirtualizedScrollPane virtualizedScrollPane = new VirtualizedScrollPane<>(removeLineCodeArea);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(virtualizedScrollPane);
        removeLineVBox.getChildren().add(stackPane);
        VBox.setVgrow(stackPane, Priority.ALWAYS);
        getStylesheets().add(ConsoleView.class.getResource("/styling/" + (new ThemeModel()).getTheme() + ".css").toExternalForm());
        removeLineCodeArea.setParagraphGraphicFactory(LineNumberFactory.get(removeLineCodeArea));
        removeLineCodeArea.textProperty().addListener((obs, oldText, newText) -> removeLineCodeArea.setStyleSpans(0, new TikzHighlight().highlightText(newText)));
    }

    /**
     * Call the initialisations for the two code areas.
     */
    @FXML
    private void initialize() {
        initAddLineCodeArea();
        initRemoveLineCodeArea();
    }

}
