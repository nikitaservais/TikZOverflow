package be.ac.ulb.infof307.g06.presentation.manageproject.editing.drawingcanvas;

import be.ac.ulb.infof307.g06.models.shapes.DiagramShapeModel;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Stack;
import java.util.stream.Stream;

/**
 * Canvas used to draw shapes and lines on
 */
public class DrawingCanvasView extends AnchorPane {
    private final GraphicsContext graphicsContext;
    private final Stack<Double[]> stack;
    @FXML
    private Pane pane;
    @FXML
    private Canvas canvas;
    private ContextMenu contextMenu;
    private DrawingListener listener;
    private Color shapeColor;
    private String shapeType;
    private Integer shapeSize;
    private double positionX;
    private double positionY;
    private IntegerProperty objectSize;
    private ObjectProperty<Color> objectColor;
    private StringProperty objectType;
    Double[] pastePos = {0.0, 0.0};

    public DrawingCanvasView() {
        FxmlUtils.loadFxml(this);
        setId("drawingCanvas");
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        graphicsContext = canvas.getGraphicsContext2D();
        stack = new Stack<>();
        setOnMouseClicked(this::drawOnClick);
        setOnMousePressed(this::dragBegan);
        setOnMouseReleased(this::dragEnded);
        setOnMouseMoved(this::updatePosition);
        initializeContextMenu();
    }

    public ObjectProperty<Color> objectColorProperty() {
        return objectColor;
    }

    public StringProperty objectTypeProperty() {
        return objectType;
    }

    /**
     * @param e click event
     * @return coordinates where the user clicked within the canvas
     */
    public Double[] getClickPosition(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        return new Double[]{x, y};
    }

    /**
     * @param e click event
     * @return coordinates where the user clicked within the canvas
     */
    public Double[] getClickPosition(DragEvent e) {
        double x = e.getX();
        double y = e.getY();
        return new Double[]{x, y};
    }

    /**
     * @param lineType          arc, dash or edge
     * @param shapeColor        color to draw
     * @param size              of the shape
     * @param firstCoordinates  of click
     * @param secondCoordinates of click
     * @return a shape formed by given parameters
     */
    public DiagramShapeModel createShape(String lineType, Color shapeColor, Integer size, Double[] firstCoordinates, Double[] secondCoordinates) {
        return listener.createShape(lineType, shapeColor, size, firstCoordinates, secondCoordinates);
    }

    /**
     * @param lineType    arc, dash or edge
     * @param shapeColor  color to draw
     * @param size        of the shape
     * @param coordinates of click
     * @return a shape formed by given parameters
     */
    public DiagramShapeModel createShape(String lineType, Color shapeColor, Integer size, Double[] coordinates) {
        return listener.createShape(lineType, shapeColor, size, coordinates);
    }

    /**
     * Initializes the behaviour of Canvas when a drag release is performed
     * @param e the mouse event.
     */
    public void drawOnClick(DragEvent e) {
        stack.push(getClickPosition(e));
        getDrawingInfo();
        DiagramShapeModel shape = drawShapeWithOneClick();
        addTikZCodeFrom(shape);
    }

    public void pasteOnKeyEvent() {
        if (listener.isSelectedModeActivated()) {
            listener.paste(new Double[]{positionX, positionY});
        }
    }

    public void copyOnKeyEvent() {
        if (listener.isSelectedModeActivated()) {
            listener.copy();
        }
    }

    public IntegerProperty objectSizeProperty() {
        return objectSize;
    }

    @FXML
    private void initialize() {
        objectSize = new SimpleIntegerProperty();
        objectColor = new SimpleObjectProperty<>();
        objectType = new SimpleStringProperty();
    }

    /**
     * Method that initialize the context menu for copy paste and set the event
     * handling for copy paste.
     */
    private void initializeContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Copy");
        item1.setOnAction(event -> listener.copy());
        MenuItem item2 = new MenuItem("Paste");
        item2.setOnAction(event -> listener.paste(pastePos));
        contextMenu.getItems().addAll(item1, item2);

        setOnContextMenuRequested(event -> {
            if (listener.isSelectedModeActivated()) {
                pastePos = new Double[]{positionX, positionY};
                contextMenu.show(getScene().getRoot(), event.getScreenX(), event.getScreenY());
            }
        });
    }

    /**
     * Writes the code of given shape in editor
     * @param shape to be translated
     */
    private void addTikZCodeFrom(DiagramShapeModel shape) {
        listener.addTikZCodeFrom(shape);
    }

    /**
     * Draws the shape according to selected parameters.
     * @return the shape drawn
     */
    private DiagramShapeModel drawShapeWithTwoClicks() {
        Double[] secondCoordinates = stack.pop();
        Double[] firstCoordinates = stack.pop();
        return createShape(shapeType, shapeColor, shapeSize, firstCoordinates, secondCoordinates);
    }

    /**
     * Draws the shape according to selected parameters.
     * @return the shape drawn
     */
    private DiagramShapeModel drawShapeWithOneClick() {
        Double[] coordinates = stack.pop();
        DiagramShapeModel shape = createShape(shapeType, shapeColor, shapeSize, coordinates);
        shape.draw(getGraphicsContext());
        return shape;
    }

    /**
     * Initializes the behaviour of Canvas when a mouse click is performed
     * @param e the mouse event.
     */
    private void drawOnClick(MouseEvent e) {
        if (!listener.isSelectedModeActivated()) {
            stack.push(getClickPosition(e));
            getDrawingInfo();
            if (isDrawingTwoClick()) {
                if (stack.size() == 2) {
                    DiagramShapeModel shape = drawShapeWithTwoClicks();
                    addTikZCodeFrom(shape);
                }
            } else {
                DiagramShapeModel shape = drawShapeWithOneClick();
                addTikZCodeFrom(shape);
            }
        }
    }

    private void getDrawingInfo() {
        shapeColor = listener.getShapeColor();
        shapeType = listener.getShapeType();
        shapeSize = listener.getShapeSize();
    }

    /**
     * If selection mode is activated, passes the first coordinates to the controller
     * @param event a mouse event
     */
    private void dragBegan(MouseEvent event) {
        if (listener.isSelectedModeActivated() && !event.getButton().toString().equals(ConstantsUtils.secondaryMouseButton)) {
            listener.setFirstSelectionCoordinates(event.getX(), event.getY());
        }
    }

    /**
     * If selection mode is activated, passes the second coordinates to the controller
     * @param event a mouse event
     */
    private void dragEnded(MouseEvent event) {
        if (listener.isSelectedModeActivated() && !event.getButton().toString().equals(ConstantsUtils.secondaryMouseButton)) {
            listener.setSecondSelectionCoordinates(event.getX(), event.getY());
        }
    }

    private void updatePosition(MouseEvent mouseEvent) {
        positionX = mouseEvent.getX();
        positionY = mouseEvent.getY();
    }

    private boolean isDrawingTwoClick() {
        return Stream.of(ConstantsUtils.edgeString, ConstantsUtils.arcString, ConstantsUtils.dashString).anyMatch(s -> shapeType.equals(s));
    }

    /**
     * @return GraphicsContext object
     */
    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    /**
     * @return Canvas object
     */
    public Canvas getCanvas() {
        return canvas;
    }

    public int getObjectSize() {
        return objectSize.get();
    }

    public void setObjectSize(int objectSize) {
        this.objectSize.set(objectSize);
    }

    public Color getObjectColor() {
        return objectColor.get();
    }

    public void setObjectColor(Color objectColor) {
        this.objectColor.set(objectColor);
    }

    public String getObjectType() {
        return objectType.get();
    }

    public void setObjectType(String objectType) {
        this.objectType.set(objectType);
    }

    public void setShapeType(String shape) {
        shapeType = shape;
    }

    /**
     * Set the listener for the view
     * @param listener Listener for the view
     */
    public void setListener(DrawingListener listener) {
        this.listener = listener;
    }
}