package be.ac.ulb.infof307.g06.utils;

import be.ac.ulb.infof307.g06.models.ThemeModel;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * add a special styling to the scene, with css that can be personally defined
 */
public class SceneUtils extends Scene {
    /**
     * add the styling to the scene
     * @param parent the parent scene on which we add the styling
     */
    public SceneUtils(Parent parent){
        super(parent);
        getStylesheets().add(getClass().getResource("/styling/" + (new ThemeModel().getTheme()) + ".css").toExternalForm());
    }

    /**
     * add the styling to the scene
     * @param parent the parent scene on which we add the styling
     * @param w width of the scene
     * @param h height of the scene
     */
    public SceneUtils(Parent parent, Integer w, Integer h){
        super(parent, w, h);
        getStylesheets().add(getClass().getResource("/styling/" + (new ThemeModel().getTheme()) + ".css").toExternalForm());
    }

}
