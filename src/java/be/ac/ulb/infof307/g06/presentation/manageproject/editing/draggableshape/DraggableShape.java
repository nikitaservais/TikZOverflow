package be.ac.ulb.infof307.g06.presentation.manageproject.editing.draggableshape;

import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * Make a shape that can be dragged.
 */
public class DraggableShape extends AnchorPane {
    private Color color;
    private int size;
    private String type;

    public DraggableShape() {
        FxmlUtils.loadFxml(this);
    }

    @FXML
    private void initialize() {
        getStylesheets().add(getClass().getResource("/styling/DraggableShape.css").toExternalForm());
    }

    /**
     * Gets the color of the shape.
     * @return the color of the shape.
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @return the size of the shape.
     */
    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    /**
     * Set the color of the shape.
     * @param color the color of the shape.
     */
    public void setColor(Color color) {
        this.color = color;
        String s = color.toString();
        String substring = s.substring(2);
        setStyle(String.format("-fx-border-color: #%s", substring));
    }

    /**
     * Set the size of the shape.
     * @param size the size of the shape.
     */
    public void setSize(int size) {
        this.size = size;

        setPrefWidth(size);
        setPrefHeight(size);
    }

    /**
     * Set the type of the shape (circle, square, etc..)
     * @param type the type of the shape.
     */
    public void setType(String type) {
        this.type = type;
        if (type.equals("circle")) {
            getStyleClass().clear();
            getStyleClass().add("circle");
        } else {
            getStyleClass().clear();
            getStyleClass().add("shape");
        }
    }
}
