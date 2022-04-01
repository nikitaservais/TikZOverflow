package be.ac.ulb.infof307.g06.presentation.versioning.difference;

import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * View for the differences between 2 branch, depending on the operation performed.
 */
public class DifferenceView extends VBox {
    private final SceneUtils scene;
    private DifferenceListener listener;
    @FXML
    private VBox originalTextVBox;
    @FXML
    private VBox newTextVBox;
    @FXML
    private Text insertedLinesText;
    @FXML
    private Text deletedLinesText;

    public DifferenceView(){
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    /**
     * Info : use StringComparator split into ArrayList to transform a String to an Array list string
     * @param originalText Array list of string containing the current displayed text
     * @param differenceArray Array list of array list containing the differences between the 2 texts
     */
    public void initialize(ArrayList<String> originalText,
                           ArrayList<ArrayList<String>> differenceArray) {
        //Color init
        ArrayList<Color> colorPalette = new ArrayList<>();
        colorPalette.add(Color.BLACK);
        colorPalette.add(Color.RED);
        colorPalette.add(Color.GREEN);
        //Display of the number of lines
        insertedLinesText.setText(insertedLinesText.getText().replace("?",Integer.toString(differenceArray.get(1).size())));
        deletedLinesText.setText(deletedLinesText.getText().replace("?",Integer.toString(differenceArray.get(0).size())));
        Text insertedText;
        int index = 0;
        for(String line : originalText){
            insertedText = new Text(line);
            this.originalTextVBox.getChildren().add(insertedText);
        }

        for(String line : originalText){
           if(differenceArray.get(0).contains(line)){
                index = 1;
                differenceArray.get(0).remove(line);
            }
            insertedText = new Text(line);
            insertedText.setFill(colorPalette.get(index));
            this.newTextVBox.getChildren().add(insertedText);
            index = 0;
        }
        //showing added lines at the end
        for(String line : differenceArray.get(1)){
            insertedText = new Text(line);
            insertedText.setFill(colorPalette.get(2));
            this.newTextVBox.getChildren().add(insertedText);
        }

    }

    public void setListener(DifferenceListener listener) {
        this.listener = listener;
    }

    /**
     * Change the state to false.
     * The changes will not be made.
     * @param event the action event.
     */
    @FXML
    void changeStateFalse(ActionEvent event) {
        this.listener.changeState(false);
    }

    /**
     * Change the state to true.
     * The changes will be made.
     * @param event the action event.
     */
    @FXML
    void changeStateTrue(ActionEvent event) {
        this.listener.changeState(true);
    }
}
