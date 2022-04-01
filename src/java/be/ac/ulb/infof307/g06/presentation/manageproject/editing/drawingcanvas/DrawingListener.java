package be.ac.ulb.infof307.g06.presentation.manageproject.editing.drawingcanvas;

import be.ac.ulb.infof307.g06.models.shapes.DiagramShapeModel;
import javafx.scene.paint.Color;

/**
 *  Drawing button and item info listener
 */
public interface DrawingListener {
    void addTikZCodeFrom(DiagramShapeModel shape);
    DiagramShapeModel createShape(String lineType, Color shapeColor, Integer size, Double[][] coordinates);
    DiagramShapeModel createShape(String lineType, Color shapeColor, Integer size, Double[] coordinates);
    DiagramShapeModel createShape(String lineType, Color shapeColor, Integer size, Double[] firstCoordinates, Double[] secondCoordinates);
    DiagramShapeModel createShape(Color shapeColor, Integer size, Double[] coordinates);
    String getShapeType();
    Color getShapeColor();
    Integer getShapeSize();
    void setSelectionMode(Boolean value);
    Boolean isSelectedModeActivated();
    void setFirstSelectionCoordinates(double x, double y);
    void setSecondSelectionCoordinates(double x, double y);
    DiagramShapeModel extractInformationFromCodeLine(String instruction, String[] instructionOptions, Double[][] coordinates);
    void copy();
    void paste(Double[] destination);
}
