package be.ac.ulb.infof307.g06.presentation.manageproject.editing.copypaste;

import be.ac.ulb.infof307.g06.models.shapes.DiagramShapeModel;

import java.util.ArrayList;

public interface CopyPasteListener {
    void setCode(String code);
    void setSelectionMode(Boolean value);
    void setFirstSelectionCoordinates(double x, double y);
    void setSecondSelectionCoordinates(double x, double y);
    Double[] getFirstCoordinates();
    Double[] getSecondCoordinates();
    ArrayList<DiagramShapeModel> determineShapesInSelection();
    Double[] getUpperLeftCoordinates();
    Double[] getDownRightCoordinates();
    void setCopy();
    ArrayList<DiagramShapeModel> getNewPastedShapes(Double[] destination);
}
