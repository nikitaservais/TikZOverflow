package be.ac.ulb.infof307.g06.presentation;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * As FileChooser is final, the extension is here artificial.
 */
public class FileChooserView {
    private final FileChooser fileChooser;

    public FileChooserView(String title) {
        fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    /**
     * Constructor
     * @param title     of the fileChooser window
     * @param typeFiles to be shown
     * @param extension of files allowed
     */
    public FileChooserView(String title, String typeFiles, String extension) {
        fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(typeFiles, extension));
    }



    /**
     * Displays the fileChooser window
     * @param primaryStage to be displayed in
     * @return the selected file
     */
    public File showSaveDialogInStage(Stage primaryStage) {
        return fileChooser.showSaveDialog(primaryStage);
    }

    /**
     * Displays the fileChooser window
     * @param primaryStage to be displayed in
     * @return the selected file
     */
    public File showOpenDialogInStage(Stage primaryStage) {
        return fileChooser.showOpenDialog(primaryStage);
    }
}
