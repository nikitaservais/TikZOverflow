package be.ac.ulb.infof307.g06.models;

import be.ac.ulb.infof307.g06.models.shapes.*;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestCopyPasteModel extends CopyPasteModel {
    ArrayList<DiagramShapeModel> diagramShapes;
    private Double[] destination, testCoordinates;

    public TestCopyPasteModel() {
        super();
    }


    @BeforeAll
    void setUp() {
        DiagramShapeModel circle = new CircleModel("circle", Color.RED, 10, new Double[]{10.0, 10.0});
        DiagramShapeModel rectangle = new RectangleModel("rectangle", Color.RED, 10, new Double[]{10.0, 10.0});
        DiagramShapeModel line = new LineModel("line", Color.RED, 10, new Double[]{10.0, 10.0}, new Double[]{20.0, 20.0});
        diagramShapes = new ArrayList<DiagramShapeModel>(Arrays.asList(circle, rectangle, line));
        Double[] origin = new Double[]{5.0,5.0};
        this.setDiagramShapes(diagramShapes);
        this.setOrigin(origin);
        destination = new Double[]{105.0,105.0};
        testCoordinates = new Double[]{50.0,35.0};
    }

    @Test
    void shapesCoordinatesAreCorrectlyUpdated() {
        ArrayList<DiagramShapeModel> pastedDiagrams = this.getPastedShapesCoordinates(destination);
        Double[] newCoordinates = pastedDiagrams.get(0).getCoordinates();
        assertEquals(110.0,newCoordinates[0]);
        assertEquals(110.0,newCoordinates[1]);
        newCoordinates = pastedDiagrams.get(1).getCoordinates();
        assertEquals(110.0,newCoordinates[0]);
        assertEquals(110.0,newCoordinates[1]);
        assertEquals(120.0,newCoordinates[2]);
        assertEquals(120.0,newCoordinates[3]);
        newCoordinates = pastedDiagrams.get(2).getCoordinates();
        assertEquals(110.0,newCoordinates[0]);
        assertEquals(110.0,newCoordinates[1]);
        assertEquals(120.0,newCoordinates[2]);
        assertEquals(120.0,newCoordinates[3]);
    }

    @Test
    void newCoordinatesAreCorrectlyCalculated() {
        Double[] newCoordinates = this.calculateCoordinates(testCoordinates,destination);
        assertEquals(150,newCoordinates[0]);
        assertEquals(135,newCoordinates[1]);
    }
}