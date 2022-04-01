package be.ac.ulb.infof307.g06.models;

import be.ac.ulb.infof307.g06.models.shapes.DiagramShapeModel;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;

import java.util.ArrayList;

/**
 *  updates coordinates of a shape selection for a copy paste
 */
public class CopyPasteModel {
    private ArrayList<DiagramShapeModel> diagramShapes;
    private Double[] origin;

    protected CopyPasteModel() {
        this.diagramShapes = null;
        this.origin = null;
    }

    /**
     * constructor setting class attributes
     * @param diagramShapes the diagram shape model list
     * @param origin the origin of the shapes in coordinate x, y
     */
    public CopyPasteModel(ArrayList<DiagramShapeModel> diagramShapes, Double[] origin) {
        this.diagramShapes = diagramShapes;
        this.origin = origin;
    }

    /**
     * Updates the coordinates of the shapes to copy paste
     * @param destination the coordinate of the destination.
     * @return the shapes with new coordinates
     */
    public ArrayList<DiagramShapeModel> getPastedShapesCoordinates(Double[] destination) {
        ArrayList<DiagramShapeModel> newDiagramShapes =  new ArrayList<>();
        for(DiagramShapeModel diagram : diagramShapes){
            newDiagramShapes.add(diagram.clone());
        }
        Double[] firstCoordinates, secondCoordinates;
        String type;
        for (int shape = 0; shape < newDiagramShapes.size(); shape++) {
            type = newDiagramShapes.get(shape).getType();
            if (type.equals(ConstantsUtils.circleString)) {
                firstCoordinates = newDiagramShapes.get(shape).getCoordinates();
                newDiagramShapes.get(shape).setFirstCoordinates(calculateCoordinates(firstCoordinates,destination));
            }
            else {
                firstCoordinates = newDiagramShapes.get(shape).getFirstCoordinates();
                secondCoordinates = newDiagramShapes.get(shape).getSecondCoordinates();
                newDiagramShapes.get(shape).setFirstCoordinates(calculateCoordinates(firstCoordinates,destination));
                if(secondCoordinates != null)
                newDiagramShapes.get(shape).setSecondCoordinates(calculateCoordinates(secondCoordinates,destination));
            }
        }
        return newDiagramShapes;
    }

    /**
     * Calculates new coordinates for a relative placement
     * @param coordinates of the shape to translate
     * @param destination the coodinate of the destination x, y.
     * @return new coordinates
     */
     protected Double[] calculateCoordinates(Double[] coordinates,Double[] destination) {
         coordinates[0] = coordinates[0] - origin[0] + destination[0];
         coordinates[1] = coordinates[1] - origin[1] + destination[1];
         return coordinates;
    }

    public void setDiagramShapes(ArrayList<DiagramShapeModel> diagramShapes) { this.diagramShapes = diagramShapes; }
    public void setOrigin(Double[] origin) { this.origin = origin; }
}