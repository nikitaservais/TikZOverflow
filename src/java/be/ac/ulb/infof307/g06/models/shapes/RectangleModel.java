package be.ac.ulb.infof307.g06.models.shapes;

import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static java.lang.Math.abs;

/**
 * Manage the rectangle model.
 * Create a rectangle with 2 of its opposites nodes.
 */
public class RectangleModel extends DiagramShapeModel {

    /**
     * Constructor for the drag and drop feature, only need the second coordinate because we know the first
     * coordinates when the user release the rectangle.
     * @param type of the shape, here is a rectangle
     * @param color of the rectangle.
     * @param size of the rectangle.
     * @param coordinates second coordinates of the rectangle.
     */
    public RectangleModel(String type, Color color, Integer size, Double[] coordinates) {
        super(type, color, size, coordinates, 4);
        this.setSecondCoordinates(determineSecondNode());
    }

    /**
     * Constructor for the point and click feature, need a first click and a second one to know from where to where
     * the rectangle needs to be constructed.
     * @param type of the shape, here is a rectangle
     * @param color of the rectangle.
     * @param size of the rectangle.
     * @param coordinates first and second coordinates of the rectangle.
     */
    public RectangleModel(String type, Color color, Integer size, Double[][] coordinates) {
        super(type, color, size, coordinates[0], coordinates[1], 4);
        firstCoordinates = coordinates[0];
        secondCoordinates = coordinates[1];
    }

    /**
     * Calculate the coordinates for drawing the rectangle
     */
    @Override
    public void draw(GraphicsContext graphicsContext) {
        if(type== ConstantsUtils.dashedString){
            graphicsContext.setLineDashes(6., 4.);
        }
        Double sizeH = abs(firstCoordinates[0] - secondCoordinates[0]);
        Double sizeV = abs(firstCoordinates[1] - secondCoordinates[1]);
        Double firstcoord = firstCoordinates[0];
        Double secondcoord = firstCoordinates[1];
        graphicsContext.setStroke(color);
        if (firstCoordinates[0] > secondCoordinates[0])
            firstcoord = secondCoordinates[0];
        if (firstCoordinates[1] > secondCoordinates[1])
            secondcoord = secondCoordinates[1];
        graphicsContext.strokeRect(firstcoord, secondcoord, sizeH, sizeV);
        if(type== ConstantsUtils.dashedString){
            graphicsContext.setLineDashes(1., 0.);
        }
    }

    /**
     * Fill the rectangle.
     * @param graphicsContext the instance of graphicsContext related.
     */
    public void fill(GraphicsContext graphicsContext) {
        Double sizeH = firstCoordinates[0] - secondCoordinates[0];
        Double sizeV = firstCoordinates[1] - secondCoordinates[1];
        Double firstcoord = firstCoordinates[0];
        Double secondcoord = firstCoordinates[1];
        graphicsContext.setStroke(color);
        graphicsContext.setFill(color);
        if (firstCoordinates[0] > secondCoordinates[0])
            firstcoord = secondCoordinates[0];
        else sizeH = secondCoordinates[0] - firstCoordinates[0];
        if (firstCoordinates[1] > secondCoordinates[1])
            secondcoord = secondCoordinates[1];
        else sizeV = secondCoordinates[1] - firstCoordinates[1];
        graphicsContext.fillRect(firstcoord, secondcoord, sizeH, sizeV);
    }

    /**
     * @return table of size 4, 2 first elements are coordinates of the first node of the rectangle, 2 last one for the other node.
     */
    @Override
    public Double[] getCoordinates() {
        return new Double[]{firstCoordinates[0], firstCoordinates[1], secondCoordinates[0], secondCoordinates[1]};
    }

    /**
     * @return Get the TikZ code, with its sides and filling color set.
     */
    @Override
    public String getTikZCode() {
        Double[] coordinates = getCoordinates();
        return String.format("\\draw[color=%s,fill=%s] (%d,%d) rectangle (%d,%d);", color, color, coordinates[0].intValue(), coordinates[1].intValue(), coordinates[2].intValue(), coordinates[3].intValue());
    }

    /**
     * Calculates the x and y coordinates of the opposing node of the rectangle on the diagonal
     * using its length (we only make squares right now)
     * @return  the second node.
     */
    public Double[] determineSecondNode() {
        return new Double[]{firstCoordinates[0] + size, firstCoordinates[1] + size};
    }
}
