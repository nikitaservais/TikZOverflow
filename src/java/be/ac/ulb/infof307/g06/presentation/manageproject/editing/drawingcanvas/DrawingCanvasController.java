package be.ac.ulb.infof307.g06.presentation.manageproject.editing.drawingcanvas;

import be.ac.ulb.infof307.g06.presentation.manageproject.editing.copypaste.CopyPasteController;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.copypaste.CopyPasteListener;
import be.ac.ulb.infof307.g06.models.shapes.*;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Manages the choice and drawing of diagrams
 */
public class DrawingCanvasController implements DrawingListener {
    private final DrawingCanvasView drawingCanvasView;
    private DrawingControllerListener listener;
    private Boolean selectionMode;
    private CopyPasteListener copyPasteListener;

    /**
     * Constructor.
     * Creates the EditingView class such that the user can start drawing.
     */
    public DrawingCanvasController() {
        this.selectionMode = false;
        drawingCanvasView = new DrawingCanvasView();
        copyPasteListener = new CopyPasteController(this);
        drawingCanvasView.setListener(this);
    }

    @Override
    public void addTikZCodeFrom(DiagramShapeModel shape) {
        listener.addTikZCodeFrom(shape);
    }

    /**
     * On click to draw initializes the shape to draw with selected options
     * activates draw and translation
     */

    @Override
    public DiagramShapeModel createShape(String lineType, Color shapeColor, Integer size, Double[] coordinates) {
        DiagramShapeModel shape;
        if (lineType.equals(ConstantsUtils.circleString)) {
            shape = new CircleModel(lineType, shapeColor, size, coordinates);
        } else {
            shape = new RectangleModel(lineType, shapeColor, size, coordinates);
        }
        GraphicsContext gc = getGraphicsContext();
        shape.draw(gc);
        return shape;
    }

    /**
     * Create a new Rectangle
     * @param lineType the outline type
     * @param shapeColor the color of the outline
     * @param size the size of the rectangle
     * @param coordinates of the two opposed edges
     * @return the created rectangle
     */
    @Override
    public RectangleModel createShape(String lineType, Color shapeColor, Integer size, Double[][] coordinates) {
        return new RectangleModel(lineType, shapeColor, size, coordinates);
    }

    /**
     * On click to draw initializes the shape to draw with selected options
     * Works for lines (edges and arcs) and therefore needs a 2nd point
     */

    @Override
    public CircleModel createShape(Color shapeColor, Integer size, Double[] coordinates) {
        return new CircleModel(ConstantsUtils.circleString, shapeColor, size, coordinates);
    }

    /**
     * Creates a new diagram shape
     * @param lineType the outline type
     * @param shapeColor the color of the outline
     * @param size the size of the rectangle
     * @param firstCoordinates needed
     * @param secondCoordinates needed
     * @return the created diagram shape
     */
    @Override
    public DiagramShapeModel createShape(String lineType, Color shapeColor, Integer size, Double[] firstCoordinates, Double[] secondCoordinates) {
        DiagramShapeModel shape;
        if (lineType.equals(ConstantsUtils.edgeString)) {
            shape = new LineModel(lineType, shapeColor, size, firstCoordinates, secondCoordinates);
        } else if (lineType.equals(ConstantsUtils.dashString)) {
            shape = new DashModel(lineType, shapeColor, size, firstCoordinates, secondCoordinates);
        } else {
            shape = new ArcModel(lineType, shapeColor, size, firstCoordinates, secondCoordinates);
        }
        GraphicsContext graphicsContext = getGraphicsContext();
        shape.draw(graphicsContext);
        return shape;
    }

    /**
     * @return the shape from DrawingCanvasView toolBar
     */
    @Override
    public String getShapeType() {
        return listener.getShapeType();
    }

    /**
     * @return the color from DrawingCanvasView toolBar
     */
    @Override
    public Color getShapeColor() {
        return listener.getShapeColor();
    }

    /**
     * @return the size of the shape from DrawingCanvasView toolbar
     */
    @Override
    public Integer getShapeSize() {
        return listener.getShapeSize();
    }

    /**
     * Draws a rectangle
     * @param type        of rectangle
     * @param color       of the rectangle
     * @param size        of the rectangle
     * @param coordinates of the rectangle
     */
    public void drawRectangle(String type, Color color, Integer size, Double[][] coordinates) {
        draw(createShape(type, color, size, coordinates));
    }

    /**
     * Fills the given rectangle
     * @param colorFill the color used to fill
     * @param size the size of the rectangle
     * @param coordinates of the rectangle
     */
    public void fillRectangle(Color colorFill, Integer size, Double[][] coordinates) {
        fill(createShape(ConstantsUtils.rectangleString, colorFill, size, coordinates));
    }

    /**
     * Draws a circle
     * @param color       of the circle
     * @param size        of the circle
     * @param coordinates of the circle
     */
    public void drawCircle(Color color, Integer size, Double[][] coordinates) {
        draw(createShape(color, size, coordinates[0]));
    }

    /**
     * Fills the given circle
     * @param colorFill the color used to fill
     * @param size the size of the circle
     * @param coordinates of the circle
     */
    public void fillCircle(Color colorFill, Integer size, Double[][] coordinates) {
        fill(createShape(colorFill, size, coordinates[0]));
    }

    /**
     * Draws a shape
     * @param type the type of the shape
     * @param color       of the shape
     * @param size        of the shape
     * @param firstCoordinates of the shape
     * @param secondCoordinates of the shape
     */
    public void drawShape(String type, Color color, Integer size, Double[] firstCoordinates, Double[] secondCoordinates) {
        draw(createShape(type, color, size, firstCoordinates, secondCoordinates));
    }

    /**
     * Draw a shape in the graphic context
     * @param shape to be drawn
     */
    public void draw(DiagramShapeModel shape) {
        shape.draw(getGraphicsContext());
    }

    /**
     * Fills the given rectangle
     * @param shape the rectangle to fill
     */
    public void fill(RectangleModel shape) {
        shape.fill(getGraphicsContext());
    }

    /**
     * Fills the given circle
     * @param shape the circle to fill
     */
    public void fill(CircleModel shape) {
        shape.fill(getGraphicsContext());
    }

    /**
     * Clears the drawing canvas
     */
    public void clear() {
        drawingCanvasView.getGraphicsContext().clearRect(0, 0, 2000, 2000);
    }

    /**
     * @return the view associated to the current controller
     */
    public DrawingCanvasView getView() {
        return drawingCanvasView;
    }

    /**
     * @return the graphicsContext associated to the view
     */
    public GraphicsContext getGraphicsContext() {
        return drawingCanvasView.getGraphicsContext();
    }

    /**
     * @return the canvas associated to the view
     */
    public Canvas getCanvas() { return drawingCanvasView.getCanvas(); }

    /**
     * @param listener to link controller and view
     */
    public void setListener(DrawingControllerListener listener) {
        this.listener = listener;
    }

    /**
     * Changes the selection mode to the value and, if false, removes the dashed frame
     * @param value the status of selection mode
     */
    public void setSelectionMode(Boolean value) {
        this.selectionMode = value;
        if (value) {
            this.copyPasteListener.setCode(this.listener.getCode());
        } else {
            this.listener.translate();
        }
        this.copyPasteListener.setSelectionMode(value);
    }
    public Boolean isSelectedModeActivated() {
        return selectionMode;
    }

    /**
     * Passes the first coordinates to selection to copypastelistener
     * @param x of the click
     * @param y of the click
     */
    @Override
    public void setFirstSelectionCoordinates(double x, double y) {
        this.listener.translate();
        this.copyPasteListener.setFirstSelectionCoordinates(x, y);
    }

    /**
     * Passes the second coordinates to selection to copypastelistener
     * @param x of the click
     * @param y of the click
     */
    @Override
    public void setSecondSelectionCoordinates(double x, double y) {
        this.copyPasteListener.setSecondSelectionCoordinates(x, y);
        this.drawRectangle(ConstantsUtils.dashedString, Color.color(0.,0.,0.,1.), 0, new Double[][]{this.copyPasteListener.getUpperLeftCoordinates(), this.copyPasteListener.getDownRightCoordinates()});
    }

    /**
     * Creates a DiagramShapeModel from a code line
     * @param instruction the line not parsed
     * @param instOp the option parsed from the line
     * @param coord the coordinates in the line
     * @return a DiagramShapeModel created
     */
    public DiagramShapeModel extractInformationFromCodeLine(String instruction, String[] instOp, Double[][] coord) {
        return this.listener.extractInformationFromCodeLine(instruction, instOp, coord);
    }

    /**
     * set the last items selected to the clipboard
     */
    @Override
    public void copy(){
        copyPasteListener.setCopy();
    }

    /**
     * paste the content of the clipboard
     * @param destination where the upper left corner will be pasted
     */
    @Override
    public void paste(Double[] destination) {
        ArrayList<DiagramShapeModel> diagrams = copyPasteListener.getNewPastedShapes(destination);
        if(diagrams != null){
            for(DiagramShapeModel diagram : diagrams){
                listener.addTikZCodeFrom(diagram);
            }
            listener.translate();
        } else {
            new ErrorMessage("Your clipboard is empty, please select an area first.");
        }

    }
}



