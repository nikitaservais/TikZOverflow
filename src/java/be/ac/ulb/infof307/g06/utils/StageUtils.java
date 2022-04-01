package be.ac.ulb.infof307.g06.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Manage the stage for all the code, so it always uses the same stage and don't need to pass it
 * as an argument every time.
 */
public final class StageUtils {
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        StageUtils.stage = stage;
    }

    public static void showScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }
}
