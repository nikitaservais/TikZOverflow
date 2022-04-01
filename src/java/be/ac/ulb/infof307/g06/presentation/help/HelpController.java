package be.ac.ulb.infof307.g06.presentation.help;

import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class of the help menu
 */
public class HelpController {
    private final Stage primaryStage;
    private final HelpView helpView;

    public HelpController() {
        primaryStage = StageUtils.getStage();
        helpView = new HelpView(this);
    }

    /**
     * Shows the linked view
     */
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Help");
        stage.setScene(helpView.getScene());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.show();
    }

    /**
     * Get the help file content linked to specified path
     * @param filePath of the help file
     * @return the content of the help file as String
     */
    public String getHelpFileContent(String filePath) {
        StringBuilder content = new StringBuilder();
        try {
            URL resource = HelpView.class.getResource(filePath);
            try (BufferedReader read = new BufferedReader(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = read.readLine()) != null) {
                    content.append(line).append('\n');
                }
            }
        } catch (IOException e) {
            new ErrorMessage("An error occurred when reading the help file");
        }
        return content.toString();
    }
}