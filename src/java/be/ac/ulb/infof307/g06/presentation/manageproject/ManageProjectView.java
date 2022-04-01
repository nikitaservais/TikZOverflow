package be.ac.ulb.infof307.g06.presentation.manageproject;

import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * Class that displays the management option for the files of the user.
 */
public class ManageProjectView extends AnchorPane {
    private final SceneUtils scene;
    private ManageProjectListener listener;
    @FXML
    private TableView tableView;
    private TableView.TableViewSelectionModel selectionModel;
    private HBox manageProjectHBox;
    @FXML
    private TableColumn<String, Project> column1;
    @FXML
    private TableColumn<Project, String> column2;
    @FXML
    private TableColumn<String, Project> column3;

    /**
     * constructor of the ManageProjectView class.
     */
    public ManageProjectView() {
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    /**
     * Initialize the cells value of the tableView.
     */
    @FXML
    private void initialize() {
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));
        column2.setCellValueFactory(cellData -> cellData.getValue().getUser().usernameProperty());
        column3.setCellValueFactory(new PropertyValueFactory<>("lastModification"));
    }

    /**
     * Method that collect all the files of the user and displays them in a table.
     */
    public void setFileTable() {
        tableView.getItems().clear();
        List<Project> listOfFiles = listener.getUserFileInfo();
        for (Project listOfFile : listOfFiles) {
            tableView.getItems().add(listOfFile);
        }
        selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);

    }

    /**
     * PopUpWindow that is shown if various files are selected when renaming or edit file is clicked.
     */
    public void showAlertMoreThanOneFile() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Please select only one file!");
        alert.showAndWait();
    }

    /**
     * Initiate buttons and add them to HBox layout.
     */
    private void initLayout() {
        manageProjectHBox = new HBox();
        Button openProject = new Button("Open");
        openProject.setId("manageButton");
        openProject.setOnAction(this::openFile);
        Button copyButton = new Button("Copy");
        copyButton.setId("manageButton");
        copyButton.setOnAction(this::copyFile);
        Button deleteButton = new Button("Delete");
        deleteButton.setId("manageButton");
        deleteButton.setOnAction(this::deleteFile);
        Button newDirButton = new Button("Move");
        newDirButton.setId("manageButton");
        newDirButton.setOnAction(this::changeFileDir);
        Button renameButton = new Button("Rename");
        renameButton.setId("manageButton");
        renameButton.setOnAction(this::renameFile);
        Button returnMenuButton = new Button("Return");
        returnMenuButton.setId("manageButton");
        returnMenuButton.setOnAction(this::returnMenu);
        manageProjectHBox.getChildren().addAll(openProject, copyButton, deleteButton, newDirButton, renameButton, returnMenuButton);
        manageProjectHBox.setSpacing(0);
    }

    /**
     * Manage the copyFile button clicked
     * @param event mouse event.
     */
    @FXML
    private void copyFile(ActionEvent event) {
        listener.copyProject();
        selectionModel.clearSelection();
    }
    /**
     * Manage the deleteFile button clicked
     * @param event mouse event.
     */
    @FXML
    private void deleteFile(ActionEvent event) {
        listener.deleteProject();
        selectionModel.clearSelection();
    }
    /**
     * Manage the changeFileDir button clicked
     * @param event mouse event.
     */
    @FXML
    private void changeFileDir(ActionEvent event) {
        listener.changeDirectory();
        selectionModel.clearSelection();
    }
    /**
     * Manage the renameFile button clicked
     * @param event mouse event.
     */
    @FXML
    private void renameFile(ActionEvent event) {
        listener.renameProject();
        selectionModel.clearSelection();
    }
    /**
     * Manage the openFile button clicked
     * @param event mouse event.
     */
    @FXML
    private void openFile(ActionEvent event) {
        listener.openProject();
        selectionModel.clearSelection();
    }
    /**
     * Manage the returnMenu button clicked
     * @param event mouse event.
     */
    @FXML
    private void returnMenu(ActionEvent event) {
        listener.returnToMainWindow();
    }

    /**
     * Getter to get the current index selected.
     * @return int the current index selected.
     */
    public int getCurrentSelectedIndex() {
        return tableView.getSelectionModel().getSelectedIndex();
    }

    /**
     * Getter to get the Project instances selected on the table.
     * @return ObservableList of Project, the Project instances selected on the table.
     */
    public ObservableList<Project> getSelectedItems() {
        return selectionModel.getSelectedItems();
    }

    /**
     * Set the instance of the controller related to this class.
     * @param listener the instance of the controller related to this class.
     */
    public void setListener(ManageProjectListener listener) {
        this.listener = listener;
    }
}
