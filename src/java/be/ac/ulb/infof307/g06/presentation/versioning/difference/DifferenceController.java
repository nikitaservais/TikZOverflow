package be.ac.ulb.infof307.g06.presentation.versioning.difference;

import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Invoke the gui showing differences between string.
 * State is used as the indication if the user has chosen to comply with the changes ( merge,revert,...)
 */
public class DifferenceController implements DifferenceListener {
    private  Stage stage;
    private  Boolean state = false;
    private  boolean initialized = false;
    private DifferenceView differenceView;

    /**
     * Create the difference between string for merge, revert, ...
     * @param oldStage the stage on which we get info used for the new stage.
     * @param operation the action performed (merge, revert, ...)
     * @param master the master name.
     * @param branch the branch name from which the operation will be made.
     */
    public DifferenceController(Stage oldStage, String operation, String master, String branch){
        differenceView = new DifferenceView();
        differenceView.setListener(this);
        this.stage = new Stage();
        String title = "? ".replace("?",operation) + branch + " to " + master;
        this.stage.setTitle(title);
        this.stage.setResizable(false);
        this.stage.initOwner(oldStage);
        this.stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(differenceView.getScene());
    }

    public DifferenceController(Stage stage,int id) {
        this(stage, ConstantsUtils.revertString, ConstantsUtils.commitString + id, ConstantsUtils.emptyString);
    }

    /**
     * this function is to be used before showing the stage
     * info : use StringComparator splitIntoArrayList to transform a String to an Array list string
     * @param originalText a simple arraylist of the original text used to be displayed
     * @param newTextData a array list with the different differences between texts
     */
    public void initializeTextArea(ArrayList<String> originalText,
                                   ArrayList<ArrayList<String>> newTextData){
        differenceView.initialize(originalText,newTextData);
        initialized = true;
    }

    public void show(){
        if(initialized) stage.showAndWait();
        else throw new RuntimeException("Window not intialized");
    }

    /**
     * @return get the operation status
     * if the button ok is clicked it means the user agreed on the operation (merge/revert/...)
     * and state is used to gather the information about user intent
     */
    public Boolean getState(){return state;}

    /**
     * @param b indicate if the button ok or cancel was pressed
     */
    @Override
    public void changeState(boolean b) {
        state = b;
        stage.close();
    }
}
