package be.ac.ulb.infof307.g06.presentation.manageproject.editing.drawingcanvas;

import be.ac.ulb.infof307.g06.presentation.manageproject.editing.drawingcanvas.DrawingCanvasController;
import be.ac.ulb.infof307.g06.models.shapes.CircleModel;
import be.ac.ulb.infof307.g06.models.shapes.DashModel;
import be.ac.ulb.infof307.g06.models.shapes.DiagramShapeModel;
import javafx.embed.swing.JFXPanel;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the drawing controller. Purpose of these tests if to see if we
 * convert successfully from one type to another
 */
class TestDrawingCanvasController {
    DrawingCanvasController drawingCanvasController;
    CircleModel circleModel;
    DashModel dashModel;
    JFXPanel jfxPanel; // needed for elements that require javafx
    @BeforeEach
    void setUp() {
        jfxPanel = new JFXPanel();
        drawingCanvasController = new DrawingCanvasController();
        circleModel = new CircleModel("circle", Color.BLUE, 50, new Double[]{100.0, 200.0});
        dashModel = new DashModel("dash", Color.RED, 60, new Double[]{100.0, 200.0}, new Double[]{250.0, 318.0});
    }

    /**
     * Test if we have the same parameters when transitioning from shape model
     * to a specific shape model. In this case the CircleModel.
     */
    @Test
    void circleModelHasSameCoordinatesAsShapeModel() {
        DiagramShapeModel diagramShapeModel = drawingCanvasController.createShape("circle", Color.BLUE, 50, new Double[]{100.0, 200.0});
        assertEquals(circleModel.getCoordinates()[0], diagramShapeModel.getCoordinates()[0]);
        assertEquals(circleModel.getCoordinates()[1], diagramShapeModel.getCoordinates()[1]);
        assertEquals(circleModel.getCoordinates()[2], diagramShapeModel.getCoordinates()[2]);
    }

    /**
     * Test if we get the same type for our circleModel as the diagramShapeModel
     */
    @Test
    void circleModelIsSameTypeAsDiagramShapeModel(){
        DiagramShapeModel diagramShapeModel = drawingCanvasController.createShape("circle", Color.BLUE, 50, new Double[]{100.0, 200.0});
        assertEquals(circleModel.getType(), diagramShapeModel.getType());

    }

    /**
     * Test if we get the same color for our dashModel as the diagramShapeModel
     */
    @Test
    void circleModelIsSameColorAsDiagramShapeModel(){
        DiagramShapeModel diagramShapeModel = drawingCanvasController.createShape("circle", Color.BLUE, 50, new Double[]{100.0, 200.0});
        assertEquals(circleModel.getColor(), diagramShapeModel.getColor());
    }

    /**
     * Test if we get the same size for our dashModel as the diagramShapeModel
     */
    @Test
    void circleModelIsSameSizeAsDiagramShapeModel(){
        DiagramShapeModel diagramShapeModel = drawingCanvasController.createShape("circle", Color.BLUE, 50, new Double[]{100.0, 200.0});
        assertEquals(circleModel.getSize(), diagramShapeModel.getSize());
    }

    /**
     * Test if we have the same parameters when transitioning from shape model
     * to a specific shape model. In this case the DashModel.
     */
    @Test
    void dashModelHasSameCoordinatesAsShapeModel() {
        DiagramShapeModel diagramShapeModel = drawingCanvasController.createShape("dash", Color.RED, 60, new Double[]{100.0, 200.0}, new Double[]{250.0, 318.0});
        assertEquals(dashModel.getCoordinates()[0], diagramShapeModel.getCoordinates()[0]);
        assertEquals(dashModel.getCoordinates()[1], diagramShapeModel.getCoordinates()[1]);
        assertEquals(dashModel.getCoordinates()[2], diagramShapeModel.getCoordinates()[2]);
        assertEquals(dashModel.getCoordinates()[3], diagramShapeModel.getCoordinates()[3]);
    }

    /**
     * Test if we get the same type for our dashModel as the diagramShapeModel
     */
    @Test
    void dashModelIsSameTypeAsDiagramShapeModel(){
        DiagramShapeModel diagramShapeModel = drawingCanvasController.createShape("dash", Color.RED, 60, new Double[]{100.0, 200.0}, new Double[]{250.0, 318.0});
        assertEquals(dashModel.getType(), diagramShapeModel.getType());

    }

    /**
     * Test if we get the same color for our dashModel as the diagramShapeModel
     */
    @Test
    void dashModelIsSameColorAsDiagramShapeModel(){
        DiagramShapeModel diagramShapeModel = drawingCanvasController.createShape("dash", Color.RED, 60, new Double[]{100.0, 200.0}, new Double[]{250.0, 318.0});
        assertEquals(dashModel.getColor(), diagramShapeModel.getColor());
    }

    /**
     * Test if we get the same size for our dashModel as the diagramShapeModel
     */
    @Test
    void dashModelIsSameSizeAsDiagramShapeModel(){
        DiagramShapeModel diagramShapeModel = drawingCanvasController.createShape("dash", Color.RED, 60, new Double[]{100.0, 200.0}, new Double[]{250.0, 318.0});
        assertEquals(dashModel.getSize(), diagramShapeModel.getSize());
    }
}