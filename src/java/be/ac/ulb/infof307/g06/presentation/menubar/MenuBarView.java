package be.ac.ulb.infof307.g06.presentation.menubar;

import be.ac.ulb.infof307.g06.exceptions.CommitException;
import be.ac.ulb.infof307.g06.exceptions.VersioningException;
import be.ac.ulb.infof307.g06.presentation.FileChooserView;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * The menu displayed on the top of the edition window
 */
public class MenuBarView extends MenuBar {
    @FXML
    private MenuItem openProjectMenuItem;

    @FXML
    private MenuItem saveProjectMenuItem;

    @FXML
    private MenuItem closeProjectMenuItem;

    @FXML
    private MenuItem importProjectMenuItem;

    @FXML
    private MenuItem exportProjectMenuItem;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private MenuItem menuVersionControl;

    private MenuBarListener listener;

    public MenuBarView() {
        FxmlUtils.loadFxml(this);
    }

    /**
     * when exit button is clicked
     * @param event the mouse event.
     */
    @FXML
    public void exitMenuItemOnClick(ActionEvent event) {
        listener.exitMenuItemClicked();
    }

    /**
     * when help button is clicked
     * @param event the mouse event.
     */
    @FXML
    public void helpMenuItemOnClick(ActionEvent event) {
        listener.helpMenuItemClicked();
    }

    /**
     * when theme button is clicked
     * @param event the mouse event.
     */
    @FXML
    public void themeMenuItemOnClick(ActionEvent event) {
        listener.themeMenuItemClicked();
    }

    /**
     * when close Project is clicked
     * @param event the mouse event.
     */
    @FXML
    public void closeProjectMenuItemOnClick(ActionEvent event) {
        listener.closeProject();
    }

    /**
     * when open Project is clicked
     * @param event the mouse event.
     */
    @FXML
    public void openProjectMenuItemOnClick(ActionEvent event) {

    }

    /**
     * when save Project is clicked
     * @param event the mouse event.
     */
    @FXML
    public void saveProjectMenuItemOnClick(ActionEvent event) {
        listener.saveProject();
    }

    /**
     * when import Project is clicked
     * @param event the mouse event.
     */
    @FXML
    public void importProjectMenuItemOnClick(ActionEvent event){
        listener.importProjectButtonClicked();
    }

    /**
     * when exporting Project is clicked
     * @param event the mouse event.
     */
    @FXML
    public void exportProjectMenuItemOnClick(ActionEvent event) {
        listener.exportProjectButtonClicked();
    }

    @FXML
    public void commitOnAction(ActionEvent event) {
        listener.commit();
    }

    @FXML
    public void revertCommitOnAction(ActionEvent event) throws CommitException, VersioningException {
        listener.revertCommit();
    }

    @FXML
    public void createNewBranchOnAction(ActionEvent event) { listener.createNewBranch(); }

    @FXML
    public void chooseExistingBranchOnAction(ActionEvent event) { listener.chooseExistingBranch(); }

    @FXML
    public void historyOnAction(ActionEvent event) {
        listener.history();
    }

    @FXML
    public void mergeOnAction(ActionEvent event) { listener.merge(); }

    @FXML
    public void commitContentOnAction(ActionEvent event) { listener.displayCommitContent(); }

    /**
     * Asks the user a path
     * @param primaryStage to show the fileChooser window
     * @return the selected path
     */
    public String getPath(Stage primaryStage) {
        return new FileChooserView("Export In", "All Files", "*.*").showSaveDialogInStage(primaryStage).getAbsolutePath();
    }

    /**
     * set listener
     * @param listener the instance of the controller related to this class.
     */
    public void setListener(MenuBarListener listener) {
        this.listener = listener;
    }
}
