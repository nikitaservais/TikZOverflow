package be.ac.ulb.infof307.g06.models.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Manage the line model.
 */
public class LineModel extends DiagramShapeModel {

    /**
     * Create a line with the first coordinate of beginning and the second coordinate of end.
     * @param type of the shape, here is a line.
     * @param color of the line.
     * @param size of the line.
     * @param firstCoordinates beginning.
     * @param secondCoordinates end.
     */
    public LineModel(String type, Color color, Integer size, Double[] firstCoordinates, Double[] secondCoordinates) {
        super(type, color, size, firstCoordinates, secondCoordinates, 1);
    }

    /**
     * Draws the line.
     */
    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setStroke(color);
        graphicsContext.strokeLine(firstCoordinates[0], firstCoordinates[1], secondCoordinates[0], secondCoordinates[1]);
    }

    /**
     * Returns the coordinates of the line, namely the x and y.
     * coordinates of the center (first point of line) and the lineEnd.
     */
    @Override
    public Double[] getCoordinates() {
        return new Double[]{firstCoordinates[0], firstCoordinates[1], secondCoordinates[0], secondCoordinates[1]};
    }

    /**
     * Get the TikZ code.
     */
    @Override
    public String getTikZCode() {
        Double[] coordinates = getCoordinates();
        String tikzCommand = String.format("\\draw[color=%s] (%d,%d) -- (%d,%d);", color, coordinates[0].intValue(), coordinates[1].intValue(), coordinates[2].intValue(), coordinates[3].intValue());
        return tikzCommand;
    }
}