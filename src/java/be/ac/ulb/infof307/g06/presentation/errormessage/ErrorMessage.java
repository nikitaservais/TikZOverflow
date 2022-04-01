package be.ac.ulb.infof307.g06.presentation.errormessage;

import be.ac.ulb.infof307.g06.models.ThemeModel;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.geometry.Pos;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class that displays a pop up message to inform the user of a error.
 */
public class ErrorMessage {
    private final Stage stage;

    /**
     * Constructor of the ErrorMessage class
     * @param errorString The message to display in the error pop up.
     */
    public ErrorMessage(String errorString) {
        stage = new Stage();
        stage.initOwner(StageUtils.getStage());
        stage.initModality(Modality.WINDOW_MODAL);
        Label text = new Label();
        text.setText(errorString);
        text.setTextAlignment(TextAlignment.CENTER);
        Button ok = new Button("OK");
        VBox vbox = new VBox(2, text);
        ok.setMinWidth(70);
        vbox.getChildren().add(ok);
        vbox.setMinSize(300, 150);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        SceneUtils scene = new SceneUtils(vbox, 300, 150);
        stage.setScene(scene);
        stage.setTitle("Error Message");
        stage.show();
        stage.getScene().getStylesheets().add(getClass().getResource("/styling/" + (new ThemeModel()).getTheme()+ ".css").toExternalForm());

        ok.setOnMouseClicked(event -> stage.close());
    }
}
