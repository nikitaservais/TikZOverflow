package be.ac.ulb.infof307.g06.models.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Manage the dashed line model.
 */
public class DashModel extends DiagramShapeModel {

    /**
     * DashModel constructor using the second shape constructor
     * @param type of the shape, here is a dashed line.
     * @param color of the dash line.
     * @param size of the dash line.
     * @param firstCoordinates beginning.
     * @param secondCoordinates end.
     */
    public DashModel(String type, Color color, Integer size, Double[] firstCoordinates, Double[] secondCoordinates) {
        super(type, color, size, firstCoordinates, secondCoordinates, 1);
    }

    /**
     * Returns the coordinates of the arc, namely the x and y
     * coordinates of the center (first point of dash line) and the dash lineEnd
     */
    @Override
    public Double[] getCoordinates() {
        return new Double[]{firstCoordinates[0], firstCoordinates[1], secondCoordinates[0], secondCoordinates[1]};
    }

    /**
     * Draws the dash line
     */
    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setStroke(color);
        graphicsContext.setLineDashes(5);
        graphicsContext.strokeLine(firstCoordinates[0], firstCoordinates[1], secondCoordinates[0], secondCoordinates[1]);
        graphicsContext.setLineDashes(0);
    }

    /**
     * Get the TikZ code
     */
    @Override
    public String getTikZCode() {
        Double[] coordinates = getCoordinates();
        String tikzCommand = String.format("\\draw[color=%s,dash] (%d,%d) -- (%d,%d);", color, coordinates[0].intValue(), coordinates[1].intValue(), coordinates[2].intValue(), coordinates[3].intValue());
        return tikzCommand;
    }
}