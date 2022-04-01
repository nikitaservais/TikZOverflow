package be.ac.ulb.infof307.g06.presentation.manageproject.createproject;

import be.ac.ulb.infof307.g06.presentation.FileChooserView;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import javafx.stage.Stage;

import java.io.File;

/**
 * This class can be instatiated to show a fileChooserView
 * where the user can choose a directory to save his file in.
 * The filename is then returned by the show method to be
 * verified by the Controller
 */
public class CreateProjectView {
    private File fileToSave;
    private final Stage primaryStage;
    private final FileChooserView fileChooserView;

    public CreateProjectView() {
        primaryStage = new Stage();
        fileChooserView = new FileChooserView("Save In", "All Files", ConstantsUtils.texExtensionString);
    }

    /**
     * Displays the FileChooserView Window
     * @return Filename the user has chosen
     */
    public File display() {
        fileToSave = fileChooserView.showSaveDialogInStage(primaryStage);
        primaryStage.show();
        primaryStage.close(); // Maybe implement onCloseRequest to return an empty path
        return fileToSave;
    }
}
