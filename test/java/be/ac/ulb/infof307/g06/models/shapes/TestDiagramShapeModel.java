package be.ac.ulb.infof307.g06.models.shapes;

import be.ac.ulb.infof307.g06.models.shapes.*;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestDiagramShapeModel {
    private DiagramShapeModel circle;
    private DiagramShapeModel triangle;
    private DiagramShapeModel line;
    private DiagramShapeModel dash;
    private DiagramShapeModel arc;

    /**
     * Create all objects for tests
     */
    @BeforeAll
    void setUp(){
        circle = new CircleModel("circle", Color.RED, 10, new Double[]{10.0, 10.0});
        line = new LineModel("line", Color.RED, 10, new Double[]{10.0, 10.0}, new Double[]{20.0, 20.0});
        dash = new DashModel("dash", Color.RED, 10, new Double[]{10.0, 10.0}, new Double[]{20.0, 20.0});
        arc = new ArcModel("arc", Color.RED, 10, new Double[]{10.0, 10.0}, new Double[]{20.0, 20.0});
    }

    /**
     * Test the DiagramShapeModel type getter in the case of a circle
     */
    @Test
    void getTypeReturnsCorrectType(){
        assertEquals("circle", circle.getType());
    }

    /**
     * Test the DiagramShapeModel colorRGB getter in the case the color red
     */
    @Test
    void getColorRGBReturnsCorrectColors(){
        Double[] circleRedRGB = circle.getColorRGB();
        assertEquals(1.0, circleRedRGB[0]);
        assertEquals(0.0, circleRedRGB[1]);
        assertEquals(0.0, circleRedRGB[2]);
    }
    /**
     * Test the DiagramShapeModel color getter in the case the color red
     */
    @Test
    void getColorReturnsCorrectColor(){
        assertEquals(Color.RED, circle.getColor());
    }
    /**
     * Test the DiagramShapeModel size getter in the case of a circle of size 10
     */
    @Test
    void getSizeReturnsCorrectSize(){
        assertEquals(10.0, circle.getSize());
    }
    /**
     * Test the DiagramShapeModel edgeNumber getter in the case of a circle with 0 edges
     */
    @Test
    void getEdgeNumberReturnsCorrectEdgeNumber(){
        assertEquals(0, circle.getEdgeNumber());
    }
    /**
     * Test the DiagramShapeModel firstCoordinate getter in the case of a circle
     */
    @Test
    void getCircleFirstCoordinateReturnsCorrectsCoordinates(){
        Double[] circleCoordinates = circle.getFirstCoordinates();
        assertEquals(10.0, circleCoordinates[0]);
        assertEquals(10.0, circleCoordinates[1]);
    }
    /**
     * Test the DiagramShapeModel SecondCoordinate getter in the case of a line
     */
    @Test
    void getLineSecondCoordinatesReturnsCorrectsCoordinates(){
        Double[] lineCoordinates = line.getSecondCoordinates();
        assertEquals(20.0, lineCoordinates[0]);
        assertEquals(20.0, lineCoordinates[1]);
    }
    /**
     * Test the DiagramShapeModel color hexadecimal converter with the color red
     */
    @Test
    void getRedInHexadecimalFormReturnsCorrectColor(){
        Color color = Color.RED;
        assertEquals("#FF0000", DiagramShapeModel.getColorInHexadecimalForm(color)); //FF0000 = red in hexadeximal
    }
    /**
     * Test the DiagramShapeModel color RGB converter with the color red
     */
    @Test
    void getRedInRGBFormReturnsCorrectColor() {
        Color color = Color.RED;
        Double[] shapeRedRGB = DiagramShapeModel.getColorInRGBForm(color);
        assertEquals(1.0, shapeRedRGB[0]);
        assertEquals(0.0, shapeRedRGB[1]);
        assertEquals(0.0, shapeRedRGB[2]);
    }
    /**
     * Test the coordinate getter of the Circle with specific coordinates
     */
    @Test
    void getCoordinatesCircleReturnCorrectsCoordinates(){
        Double[] coordinates = circle.getCoordinates();
        assertEquals(10, coordinates[0]);
        assertEquals(10, coordinates[1]);
        assertEquals(10, coordinates[2]);
    }
    /**
     * Test the tikz code creator in the case of a circle
     */
    @Test
    void getTikZCodeCircleReturnsCorrectTikZCode(){
        Color color = circle.getColor();
        String tikzCommand = String.format("\\draw[color=%s,fill=%s] (10,10) circle (10pt);", color, color);
        assertEquals(tikzCommand, circle.getTikZCode());
    }
    /**
     * Test that the second node coordinates are correctly calculated when
     * using single node rectangle constructor
     */
    @Test
    void secondNodeCoordinatesAreCorrectlyCalculated(){
        DiagramShapeModel rectangle = new RectangleModel("rectangle", Color.RED, 10, new Double[]{10.0, 10.0});
        Double[] coordinates = rectangle.getCoordinates();
        assertEquals(10.0, coordinates[0]);
        assertEquals(10.0, coordinates[1]);
        assertEquals(20.0, coordinates[2]);
        assertEquals(20.0, coordinates[3]);
    }
    /**
     * Test the tikz code creator in the case of a rectangle
     */
    @Test
    void getTikZCodeRectangleReturnsCorrectTikZCode(){
        DiagramShapeModel rectangle = new RectangleModel("rectangle", Color.RED, 10, new Double[]{10.0, 10.0});
        Double[] coordinates = rectangle.getCoordinates();
        Color color = rectangle.getColor();
        String tikzCommand = String.format("\\draw[color=%s,fill=%s] (%d,%d) rectangle (%d,%d);", color, color,
                coordinates[0].intValue(), coordinates[1].intValue(), coordinates[2].intValue(), coordinates[3].intValue());
        assertEquals(tikzCommand, rectangle.getTikZCode());
    }

    /**
     * Test the coordinate getter of the line with specific coordinates
     */
    @Test
    void getLineCoordinatesReturnsCorrectCoordinates(){
        Double[] coordinates = line.getCoordinates();
        assertEquals(10.0, coordinates[0]);
        assertEquals(10.0, coordinates[1]);
        assertEquals(20.0, coordinates[2]);
        assertEquals(20.0, coordinates[3]);
    }
    /**
     * Test the tikz code creator in the case of a line
     */
    @Test
    void getLineTikZCodeReturnsCorrectTikZCode(){
        Double[] coordinates = line.getCoordinates();
        Color color = line.getColor();
        String tikzCommand = String.format("\\draw[color=%s] (%d,%d) -- (%d,%d);", color,
                coordinates[0].intValue(), coordinates[1].intValue(), coordinates[2].intValue(), coordinates[3].intValue());
        assertEquals(tikzCommand, line.getTikZCode());
    }
    /**
     * Test the coordinate getter of the dash with specific coordinates
     */
    @Test
    void getDashCoordinatesReturnsCorrectCoordinates(){
        Double[] coordinates = dash.getCoordinates();
        assertEquals(10.0, coordinates[0]);
        assertEquals(10.0, coordinates[1]);
        assertEquals(20.0, coordinates[2]);
        assertEquals(20.0, coordinates[3]);
    }
    /**
     * Test the tikz code creator in the case of a dash
     */
    @Test
    void getDashTikZCodeReturnsCorrectTikZCode(){
        Double[] coordinates = dash.getCoordinates();
        Color color = dash.getColor();
        String tikzCommand = String.format("\\draw[color=%s,dash] (%d,%d) -- (%d,%d);", color,
                coordinates[0].intValue(), coordinates[1].intValue(), coordinates[2].intValue(), coordinates[3].intValue());
        assertEquals(tikzCommand, dash.getTikZCode());
    }
    /**
     * Test the coordinate getter of the arc with specific coordinates
     */
    @Test
    void getArcCoordinatesReturnsCorrectCoordinates(){
        Double[] coordinates = arc.getCoordinates();
        assertEquals(10.0, coordinates[0]);
        assertEquals(10.0, coordinates[1]);
        assertEquals(20.0, coordinates[2]);
        assertEquals(20.0, coordinates[3]);
    }
    /**
     * Test the tikz code creator in the case of a arc
     */
    @Test
    void getArcTikZCodeReturnsCorrectTikZCode(){
        Double[] coordinates = arc.getCoordinates();
        Color color = arc.getColor();
        String tikzCommand = String.format("\\draw[color=%s,->] (%d,%d) -- (%d,%d);", color,
                coordinates[0].intValue(), coordinates[1].intValue(), coordinates[2].intValue(), coordinates[3].intValue());
        assertEquals(tikzCommand, arc.getTikZCode());
    }
}