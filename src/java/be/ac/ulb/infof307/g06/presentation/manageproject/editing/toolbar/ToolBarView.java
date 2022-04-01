package be.ac.ulb.infof307.g06.presentation.manageproject.editing.toolbar;

import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


/**
 * this is the toolbar for the tool bar where the user can choose what he wants to do
 */
public class ToolBarView extends ToolBar {
    private final SceneUtils scene;
    @FXML
    private ImageView dashImageView;
    @FXML
    private ImageView lineImageView;
    @FXML
    private ImageView arrowImageView;
    @FXML
    private ImageView rectangleImageView;
    @FXML
    private ImageView circleImageView;
    @FXML
    private ToggleGroup shapeToggleGroup;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Spinner<Integer> sizeSpinner;
    @FXML
    private ToggleButton selectionModeToggleButton;
    @FXML
    private ToggleButton circleButton;

    @FXML
    private ToggleButton rectangleButton;
    private ToolBarListener listener;
    private IntegerProperty objectSize;
    private ObjectProperty<Color> objectColor;
    private StringProperty objectType;

    public ToolBarView() {
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
    }

    public void setDragHandler(EventHandler<MouseEvent> dragDetected, EventHandler<DragEvent> dragOverEvent) {
        rectangleButton.setOnDragDetected(dragDetected);
        rectangleButton.setOnDragOver(dragOverEvent);
        circleButton.setOnDragDetected(dragDetected);
        circleButton.setOnDragOver(dragOverEvent);
    }

    public IntegerProperty objectSizeProperty() {
        return objectSize;
    }

    public ObjectProperty<Color> objectColorProperty() {
        return objectColor;
    }

    public StringProperty objectTypeProperty() {
        return objectType;
    }

    @FXML
    private void initialize() {
        objectType = new SimpleStringProperty();
        objectSize = new SimpleIntegerProperty();
        objectColor = new SimpleObjectProperty<>();
        objectSize.bind(sizeSpinner.valueProperty());
        objectType.set(shapeToggleGroup.getSelectedToggle().getUserData().toString());
        shapeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                objectType.set(newValue.getUserData().toString());
            }
        });
        objectColor.bind(colorPicker.valueProperty());
        circleImageView.setImage(new Image(getClass().getResource("/images/circle.png").toExternalForm()));
        rectangleImageView.setImage(new Image(getClass().getResource("/images/rectangle.png").toExternalForm()));
        arrowImageView.setImage(new Image(getClass().getResource("/images/arc.png").toExternalForm()));
        dashImageView.setImage(new Image(getClass().getResource("/images/dash.png").toExternalForm()));
        lineImageView.setImage(new Image(getClass().getResource("/images/edge.png").toExternalForm()));
        sizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 100, 1));

        shapeToggleGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == null) {
                shapeToggleGroup.selectToggle(circleButton);
            }
        }));
    }

    /**
     * when clear button is clicked
     */
    @FXML
    private void clearButtonClicked(ActionEvent event) {
        listener.clearButtonClicked();
    }

    /**
     * when translate button is clicked
     */
    @FXML
    private void translateButtonClicked(ActionEvent event) {
        listener.translateButtonClicked();
    }

    /**
     * when previewing button is clicked
     */
    @FXML
    private void previewButtonClicked(ActionEvent event) {
        listener.previewButtonClicked();
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

    /**
     * Getter to get the shape size selected.
     * @return int the size of the shape required.
     */
    public int getShapeSize() {
        return sizeSpinner.getValue();
    }

    /**
     * Getter to get the shape type.
     * @return string the name of the button name selected.
     */
    public String getShapeType() {
        String shape = shapeToggleGroup.getSelectedToggle().getUserData().toString();
        return shape;
    }

    /**
     * Getter that gives the color wanted.
     * @return color the color selected.
     */
    public Color getShapeColor() {
        return colorPicker.getValue();
    }

    /**
     * Activates the selection mode
     * @param event the mouse click
     */
    @FXML
    public void setSelectionMode(ActionEvent event) {
        listener.setSelectionMode(selectionModeToggleButton.isSelected());
    }

    public void setListener(ToolBarListener listener) {
        this.listener = listener;
    }
}
