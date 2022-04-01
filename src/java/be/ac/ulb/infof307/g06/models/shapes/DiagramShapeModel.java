package be.ac.ulb.infof307.g06.models.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Shape abstract classes with the common variables
 */
public abstract class DiagramShapeModel implements Cloneable {
    protected String type;
    protected Double[] colorRGB;
    protected Color color;
    protected Integer size;
    protected Double[] firstCoordinates;
    protected Double[] secondCoordinates;
    protected Integer edgeNumber;

    /**
     * Constructor of the shape (no lines)
     * @param type of the shape.
     * @param color of the shape.
     * @param size of the shape.
     * @param firstCoordinates of the shape.
     * @param edgeNumber number of edges, depending of the shape.
     */
    public DiagramShapeModel(String type, Color color, Integer size, Double[] firstCoordinates, Integer edgeNumber) {
        this.type = type;
        colorRGB = getColorInRGBForm(color);
        this.color = color;
        this.size = size;
        this.firstCoordinates = firstCoordinates;
        this.edgeNumber = edgeNumber;
    }

    /**
     * Constructor of the shape for lines (two clicks needed = lineEnd)
     * @param type of the line.
     * @param color of the line.
     * @param size of the line.
     * @param firstCoordinates of the shape.
     * @param secondCoordinates of the shape.
     * @param edgeNumber always 1 in this case.
     */
    DiagramShapeModel(String type, Color color, Integer size, Double[] firstCoordinates, Double[] secondCoordinates, Integer edgeNumber) {
        this.type = type;
        colorRGB = getColorInRGBForm(color);
        this.color = color;
        this.size = size;
        this.firstCoordinates = firstCoordinates;
        this.secondCoordinates = secondCoordinates;
        this.edgeNumber = edgeNumber;
    }

    /**
     * @param color the color to translate in hexadecimal.
     * @return Color in Web way to write
     */
    public static String getColorInHexadecimalForm(Color color) {
        return String.format("#%02X%02X%02X", (Integer.valueOf((int) color.getRed()) * 255), (Integer.valueOf((int) color.getGreen()) * 255), (Integer.valueOf((int) color.getBlue()) * 255));
    }

    //Getters and Setters.
    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    public Double[] getColorRGB() {
        return colorRGB;
    }
    public void setColorRGB(Double[] colorRGB) {
        this.colorRGB = colorRGB;
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }
    public void setSize(Integer size) {
        this.size = size;
    }

    public Double[] getFirstCoordinates() {
        return firstCoordinates;
    }
    public void setFirstCoordinates(Double[] firstCoordinates) {
        this.firstCoordinates = firstCoordinates;
    }

    public Double[] getSecondCoordinates() {
        return secondCoordinates;
    }
    public void setSecondCoordinates(Double[] secondCoordinates) {
        this.secondCoordinates = secondCoordinates;
    }

    public Integer getEdgeNumber() {
        return edgeNumber;
    }
    public void setEdgeNumber(Integer edgeNumber) {
        this.edgeNumber = edgeNumber;
    }

    /**
     * Abstract method to draw the shape.
     * @param graphicsContext the instance of graphicsContext related.
     */
    public abstract void draw(GraphicsContext graphicsContext);

    /**
     * Abstract method to get the coordinates of the shape.
     * @return the coordinates.
     */
    public abstract Double[] getCoordinates();

    /**
     * Abstract method to translate the shape in tikz.
     * @return the tikz code of the console.
     */
    public abstract String getTikZCode();

    /**
     * Transform a color to RGB.
     * @param color the color to translate.
     * @return the RGB equivalent to the color.
     */
    public static Double[] getColorInRGBForm(Color color) {
        return new Double[]{color.getRed(), color.getGreen(), color.getBlue()};
    }

    @Override
    public DiagramShapeModel clone() {
        DiagramShapeModel clone = null;
        try
        {
            clone =(DiagramShapeModel) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException(e);
        }

        clone.setType(this.getType());
        clone.setFirstCoordinates(this.getFirstCoordinates().clone());
        if(this.getSecondCoordinates() != null) clone.setSecondCoordinates(this.getSecondCoordinates().clone());
        clone.setColor(this.getColor());
        clone.setSize(this.getSize());
        clone.setColorRGB(this.getColorRGB().clone());
        clone.setEdgeNumber(this.getEdgeNumber());
        return clone;
    }
}

