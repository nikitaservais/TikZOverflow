package be.ac.ulb.infof307.g06.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

/**
 * Load a special FXML file.
 */
public final class FxmlUtils {
    public static <T extends Parent> void loadFxml(T component) {
        String fileName = component.getClass().getSimpleName() + ".fxml";
        FXMLLoader loader = new FXMLLoader(component.getClass().getResource(fileName));
        loader.setRoot(component);
        loader.setControllerFactory(theClass -> component);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error occurred during the window loading",e);
        }
    }
}
