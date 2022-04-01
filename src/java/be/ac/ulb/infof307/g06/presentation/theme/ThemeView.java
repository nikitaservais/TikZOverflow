package be.ac.ulb.infof307.g06.presentation.theme;

import be.ac.ulb.infof307.g06.utils.SceneUtils;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import java.util.ArrayList;

/**
 * Allows the user to choose a theme
 */
public class ThemeView extends AnchorPane {
    private SceneUtils scene;
    private ThemeListener themeListener;
    @FXML
    private ComboBox<String> themesComboBox;

    /**
     ** The constructor
     * @param listener is the controller
     */
    public ThemeView(ThemeListener listener) {
        this.themeListener = listener;
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);

    }

    /**
     * Asks the controller to apply the changes
     */
    @FXML
    public void confirmButtonClicked() {
        this.themeListener.chooseTheme(themesComboBox.getValue());
    }

    /**
     * Asks the controller to close the window
     */
    @FXML
    public void cancelButtonClicked() {
        this.themeListener.cancelButtonClicked();
    }

    /**
     * Defines the available themes
     * @param themes availables
     */
    public void setThemes(ArrayList<String> themes) {
        this.themesComboBox.setItems(FXCollections.observableArrayList(themes));
        this.themesComboBox.setValue(themes.get(0));
    }
}
