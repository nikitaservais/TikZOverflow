package be.ac.ulb.infof307.g06.models.shapes;

import be.ac.ulb.infof307.g06.models.shapes.ArcModel;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestArcModel {
    private ArcModel arc;

    /**
     * Create a new arc object for tests
     */
    @BeforeAll
    void setUp() {
        arc = new ArcModel("arc", Color.RED, 10, new Double[]{10.0, 10.0}, new Double[]{20.0, 20.0});
    }

    /**
     * Test the arrow head coordinate calculator, test if all the coordinates are
     * also the one we expected.
     */
    @Test
    void getsCorrectsArrowheadCoordinates() {
        Double[] arrowCoordinates = arc.getArrowheadCoordinates();
        Double[] arrowCoordinatesCalculated = calculateArrowHeadCoordinates();
        assertEquals(arrowCoordinatesCalculated[0], arrowCoordinates[0]);
        assertEquals(arrowCoordinatesCalculated[1], arrowCoordinates[1]);
        assertEquals(arrowCoordinatesCalculated[2], arrowCoordinates[2]);
        assertEquals(arrowCoordinatesCalculated[3], arrowCoordinates[3]);
    }

    /**
     * Method used to calculate arrow head coordinates for future testing
     */
    public Double[] calculateArrowHeadCoordinates() {
        Double arrowHeadSize = ArcModel.getArrowHeadSize();
        Double[] coordinates = arc.getCoordinates();
        Double angle = Math.atan2((coordinates[3] - coordinates[1]),
                (coordinates[2] - coordinates[0])) - Math.PI / 2.0;
        Double sin = Math.sin(angle);
        Double cos = Math.cos(angle);
        Double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + coordinates[2];
        Double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + coordinates[3];
        Double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + coordinates[2];
        Double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + coordinates[3];
        return new Double[]{x1, y1, x2, y2};
    }

    /**
     * test the arrowHeadSize getter, the variable is a constant = 5
     */
    @Test
    void arrowHeadSizeIsFive(){
        assertEquals(5.0, ArcModel.getArrowHeadSize());
    }
}