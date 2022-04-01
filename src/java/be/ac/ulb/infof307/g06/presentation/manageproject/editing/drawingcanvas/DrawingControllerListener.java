package be.ac.ulb.infof307.g06.presentation.manageproject.editing.drawingcanvas;

import be.ac.ulb.infof307.g06.models.shapes.DiagramShapeModel;
import javafx.scene.paint.Color;

/**
 *  DrawingCanvasController item info listener
 */
public interface DrawingControllerListener {
    void addTikZCodeFrom(DiagramShapeModel shape);
    Integer getShapeSize();
    Color getShapeColor();
    String getShapeType();
    String getCode();
    DiagramShapeModel extractInformationFromCodeLine(String instruction, String[] instOp, Double[][] coord);
    void translate();
}
