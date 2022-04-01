package be.ac.ulb.infof307.g06.presentation.manageproject.editing;

import be.ac.ulb.infof307.g06.presentation.manageproject.editing.console.ConsoleView;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.draggableshape.DraggableShape;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.drawingcanvas.DrawingCanvasView;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.toolbar.ToolBarView;
import be.ac.ulb.infof307.g06.presentation.menubar.MenuBarView;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


/**
 * View of the editing scene.
 */
public class EditingView extends AnchorPane {
    private final SceneUtils scene;
    private final ToolBarView toolBarView;
    private final MenuBarView menuBarView;
    private final ConsoleView consoleView;
    private final DrawingCanvasView drawingCanvasView;
    @FXML
    private VBox vBox;
    @FXML
    private SplitPane splitPane;
    @FXML
    private BorderPane borderPane;
    private DraggableShape dragShape;
    private EventHandler<DragEvent> dragOverRoot;
    private EventHandler<DragEvent> dragDoneRoot;
    private EventHandler<DragEvent> dragOverCanvas;
    private EventHandler<DragEvent> dragDroppedCanvas;
    private EventHandler<MouseEvent> dragDetected;

    /**
     * Constructor of the EditingView class.
     * @param consoleView       the related instance of the consoleView.
     * @param drawingCanvasView the related instance of the drawingCanvasView.
     * @param toolBarView       the related instance of the toolBarView.
     * @param menuBarView       the related instance of the menuBarView.
     */
    public EditingView(ConsoleView consoleView, DrawingCanvasView drawingCanvasView, ToolBarView toolBarView, MenuBarView menuBarView) { // temp for now view are passed as setter, will change when fxml is refacto for all view
        this.consoleView = consoleView;
        this.drawingCanvasView = drawingCanvasView;
        this.toolBarView = toolBarView;
        this.menuBarView = menuBarView;
        FxmlUtils.loadFxml(this);
        initDrag();
        scene = new SceneUtils(this);
        scene.setRoot(this);
        scene.setOnKeyPressed(this::initShortcuts);
    }

    @FXML
    private void initialize() {
        drawingCanvasView.objectSizeProperty().bind(toolBarView.objectSizeProperty());
        drawingCanvasView.objectColorProperty().bind(toolBarView.objectColorProperty());
        drawingCanvasView.objectTypeProperty().bind(toolBarView.objectTypeProperty());
        borderPane.setTop(menuBarView);
        vBox.getChildren().add(0, toolBarView);
        splitPane.getItems().addAll(drawingCanvasView, consoleView);
    }

    /**
     * Initiates the drag of a shape.
     */
    private void initDrag() {
        dragShape = new DraggableShape();
        getChildren().add(dragShape);
        dragShape.setVisible(false);
        setHandler();
    }

    /**
     * Manage the dragging of a shape.
     */
    private void setHandler() {
        dragOverRoot = event -> {
            dragShape.relocate(event.getSceneX(), event.getSceneY());
            event.consume();
        };

        dragDoneRoot = event -> {
            toolBarView.removeEventHandler(DragEvent.DRAG_OVER, dragOverRoot);
            drawingCanvasView.removeEventHandler(DragEvent.DRAG_DROPPED, dragDroppedCanvas);
            drawingCanvasView.removeEventHandler(DragEvent.DRAG_OVER, dragOverCanvas);
            dragShape.setVisible(false);
            event.consume();
        };

        dragOverCanvas = event -> {
            event.acceptTransferModes(TransferMode.ANY);
            dragShape.relocate(event.getSceneX(), event.getSceneY());
            event.consume();
        };

        dragDroppedCanvas = event -> {
            drawingCanvasView.drawOnClick(event);
            event.consume();
        };

        dragDetected = event -> {
            ToggleButton button = (ToggleButton) event.getSource();
            ClipboardContent content = new ClipboardContent();
            content.putString(button.getId());
            toolBarView.setOnDragOver(dragOverRoot);
            drawingCanvasView.setOnDragOver(dragOverCanvas);
            drawingCanvasView.setOnDragDropped(dragDroppedCanvas);
            dragShape.setColor(toolBarView.getShapeColor());
            dragShape.setSize(toolBarView.getShapeSize());
            drawingCanvasView.setShapeType(button.getId());
            dragShape.setType(button.getId());
            dragShape.relocate(event.getSceneX(), event.getSceneY());
            dragShape.startDragAndDrop(TransferMode.ANY).setContent(content);
            dragShape.setVisible(true);
            dragShape.setMouseTransparent(true);
            event.consume();
        };
        toolBarView.setDragHandler(dragDetected, dragOverRoot);
        setOnDragDone(dragDoneRoot);
    }

    /**
     * Initiate the keyboard shortcuts.
     * @param keyEvent key event.
     */
    private void initShortcuts(KeyEvent keyEvent) {
        final KeyCombination ctrlC = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);

        final KeyCombination ctrlV = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);

        if (ctrlC.match(keyEvent)) {
            drawingCanvasView.copyOnKeyEvent();
        } else if (ctrlV.match(keyEvent)) {
            drawingCanvasView.pasteOnKeyEvent();
        }
    }
}

