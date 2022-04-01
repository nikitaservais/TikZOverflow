package be.ac.ulb.infof307.g06.models.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Manage the circle model.
 */
public class CircleModel extends DiagramShapeModel {

    /**
     * Creates the circle.
     * @param type of the shape, here is a circle.
     * @param color of the circle.
     * @param size of the circle.
     * @param centerCoordinates second coordinates of the rectangle.
     */
    public CircleModel(String type, Color color, Integer size, Double[] centerCoordinates) {
        super(type, color, size, centerCoordinates, 0);
    }

    /**
     * Returns the x and y coordinates of the center of the circle and its radius for tikz later.
     */
    @Override
    public Double[] getCoordinates() {
        return new Double[]{firstCoordinates[0], firstCoordinates[1], size.doubleValue()};
    }

    /**
     * Draws the circle.
     * @param graphicsContext the instance of graphicsContext related.
     */
    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setStroke(color);
        graphicsContext.strokeOval(firstCoordinates[0], firstCoordinates[1], size, size);
    }

    /**
     * Fill the circle.
     * @param graphicsContext the instance of graphicsContext related.
     */
    public void fill(GraphicsContext graphicsContext) {
        graphicsContext.setStroke(color);
        graphicsContext.setFill(color);
        graphicsContext.fillOval(firstCoordinates[0], firstCoordinates[1], size, size);
    }

    /**
     * Get the TikZ code, with its sides and filling color set
     */
    @Override
    public String getTikZCode() {
        Double[] coordinates = getCoordinates();
        return String.format("\\draw[color=%s,fill=%s] (%d,%d) circle (%dpt);", color, color, coordinates[0].intValue(), coordinates[1].intValue(), coordinates[2].intValue());
    }
}
