package be.ac.ulb.infof307.g06.presentation.manageproject.editing.console;

import javafx.scene.paint.Color;

/**
 * ConsoleController button listener
 */
public interface ConsoleControllerListener {
    void drawRectangle(String type, Color color, Integer size, Double[][] coordinate);
    void drawCircle(Color color, Integer size, Double[][] coordinate);
    void drawShape(String type, Color color, Integer size, Double[] firstCoordinate, Double[] secondCoordinate);
    void fillRectangle(Color colorFill, Integer size, Double[][] coordinate);
    void fillCircle(Color colorFill, Integer size, Double[][] coordinates);
}
