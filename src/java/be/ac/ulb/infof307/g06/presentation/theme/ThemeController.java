package be.ac.ulb.infof307.g06.presentation.theme;

import be.ac.ulb.infof307.g06.models.ThemeModel;
import javafx.stage.Stage;

/**
 * Allows the user to choose a theme
 */
public class ThemeController implements ThemeListener {
    private ThemeView themeView;
    private ThemeModel themeModel;
    private Stage stage;
    public ThemeController() {
        this.themeModel = new ThemeModel();
        this.themeView = new ThemeView(this);
        this.themeView.setThemes(this.themeModel.getThemes());
    }

    /**
     * Asks to the model to change the theme
     * @param theme the selected theme
     */
    @Override
    public void chooseTheme(String theme){
        this.themeModel.setTheme(theme);
        stage.close();
    }

    /**
     * Closes the window
     */
    @Override
    public void cancelButtonClicked() {
        stage.close();
    }

    /**
     * Shows a new window to let the user chose a commit to revert to
     */
    public void show() {
        stage = new Stage();
        stage.setScene(themeView.getScene());
        stage.show();
    }
}
