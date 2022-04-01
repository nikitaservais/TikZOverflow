package be.ac.ulb.infof307.g06.models.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Manage the arc model.
 */
public class ArcModel extends DiagramShapeModel {
    private static final Double arrowHeadSize = 5.;

    /**
     * ArcModel constructor using the second shape constructor
     * @param type of the shape, here is a arc with pointed direction to the second coordinates.
     * @param color of the arc.
     * @param size of the arc.
     * @param firstCoordinates beginning.
     * @param secondCoordinates end.
     */
    public ArcModel(String type, Color color, Integer size, Double[] firstCoordinates, Double[] secondCoordinates) {
        super(type, color, size, firstCoordinates, secondCoordinates, 1);
    }

    /**
     * Get the coordinates of the arc.
     * @return table of size 4, 2 first elements are coordinates of the beginning of the arc, 2 last one for the end of the arc.
     */
    @Override
    public Double[] getCoordinates() {
        return new Double[]{firstCoordinates[0], firstCoordinates[1], secondCoordinates[0], secondCoordinates[1]};
    }

    /**
     * Draws the arc.
     */
    @Override
    public void draw(GraphicsContext graphicsContext) {
        Double[] arrowCoordinates = getArrowheadCoordinates();
        graphicsContext.setStroke(color);
        graphicsContext.strokeLine(firstCoordinates[0], firstCoordinates[1], secondCoordinates[0], secondCoordinates[1]);
        graphicsContext.strokeLine(arrowCoordinates[0], arrowCoordinates[1], secondCoordinates[0], secondCoordinates[1]);
        graphicsContext.strokeLine(arrowCoordinates[2], arrowCoordinates[3], secondCoordinates[0], secondCoordinates[1]);
    }

    /**
     * Get the Tikz shape.
     */
    @Override
    public String getTikZCode() {
        Double[] coordinates = getCoordinates();
        String tikzCommand = String.format("\\draw[color=%s,->] (%d,%d) -- (%d,%d);", color, coordinates[0].intValue(), coordinates[1].intValue(), coordinates[2].intValue(), coordinates[3].intValue());
        return tikzCommand;
    }

    /**
     * @return arrowHeadSize.
     */
    public static Double getArrowHeadSize() { return arrowHeadSize; }

    /**
     * Returns the coordinates of the arc, namely the x and y.
     * coordinates of the center (first point of arc) and the arc end.
     * @return the coordinates of the arc.
     */
    public Double[] getArrowheadCoordinates() {
        Double angle = Math.atan2((secondCoordinates[1] - firstCoordinates[1]), (secondCoordinates[0] - firstCoordinates[0])) - Math.PI / 2.0;
        Double sin = Math.sin(angle);
        Double cos = Math.cos(angle);
        Double x1 = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + secondCoordinates[0];
        Double y1 = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + secondCoordinates[1];
        Double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + secondCoordinates[0];
        Double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + secondCoordinates[1];
        return new Double[]{x1, y1, x2, y2};
    }
}