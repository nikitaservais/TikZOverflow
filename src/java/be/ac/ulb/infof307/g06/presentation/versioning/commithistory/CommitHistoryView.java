package be.ac.ulb.infof307.g06.presentation.versioning.commithistory;

import be.ac.ulb.infof307.g06.models.branch.Branch;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import be.ac.ulb.infof307.g06.models.commit.Commit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

/**
 * Class that takes care of the view side of the display the commit history information
 * depending on the branch selected of a given Project mvc.
 */
public class CommitHistoryView extends AnchorPane implements Initializable {
    @FXML
    private ComboBox comboBoxBranchName;
    @FXML
    private TableView tableView;
    private SceneUtils scene;
    private CommitHistoryListener listener;
    private SelectionModel comboBoxBranchNameSelectionModel;
    private List<Branch> branchesInfo;

    /**
     * Constructor of the CommitHistoryView Class.
     */
    public CommitHistoryView(){
        scene = new SceneUtils(this);
        FxmlUtils.loadFxml(this);
        comboBoxBranchNameSelectionModel = comboBoxBranchName.getSelectionModel();
        setTableViewColumns();
    }

    /**
     * Method of the CommitHistoryView Class that set the Listener.
     * @param listener CommitHistoryListener - The listener to associate a CommitHistoryView instance.
     */
    public void setListener(CommitHistoryListener listener) {
        this.listener = listener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Update the commit history table with the commits of the branch selected.
     * @param e ActionEvent - The event that has happened.
     */
    @FXML
    private void comboBoxSelectedOnAction(ActionEvent e){
        Integer selectedIndex = comboBoxBranchNameSelectionModel.getSelectedIndex();
        Integer branchID = branchesInfo.get(selectedIndex).getBranchID();
        fillTableView(branchID);
    }

    /**
     * Method that set's the branches name in the comboBox associated to a given Project.
     */
    public void setComboBoxBranchNames(){
        branchesInfo = listener.getBranchesInfo();
        if (!branchesInfo.isEmpty()) {
            ListIterator<Branch> listOfBranchesInfo = branchesInfo.listIterator();
            while (listOfBranchesInfo.hasNext())
                comboBoxBranchName.getItems().add(listOfBranchesInfo.next().getBranchName());
        }
    }

    /**
     * Method that insert the commit history info of the branch given in parameter of a given Project int the TableView.
     * @param branchId Integer - The branch id of the commit history to display.
     */
    private void fillTableView(Integer branchId){
        tableView.getItems().clear();
        ArrayList<ArrayList<String>> commitsInfo = listener.getCommitsInfo(branchId);
        if(commitsInfo != null) {
            ListIterator<ArrayList<String>> listOfCommitsInfos = commitsInfo.listIterator();
            while(listOfCommitsInfos.hasNext()){
                ArrayList<String> commit = listOfCommitsInfos.next();
                tableView.getItems().add(new Commit(commit.get(4), commit.get(3)));
            }
        }
    }

    /**
     * Method that set the columns of the table view.
     */
    private void setTableViewColumns() {
        TableColumn<String, Commit> column1 = new TableColumn<>("Commit Date");
        column1.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<String, Commit> column2 = new TableColumn<>("Message");
        column2.setCellValueFactory(new PropertyValueFactory<>("message"));
        tableView.getColumns().addAll(column1, column2);
    }
}
