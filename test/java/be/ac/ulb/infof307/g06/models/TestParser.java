package be.ac.ulb.infof307.g06.models;

import be.ac.ulb.infof307.g06.models.tikz.Parser;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestParser {
    Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    void getElementaryInstructionReturnsCorrectionElementaryWhenNoBrackets() {
        parser.setText("\\draw (100, 100) rectangle (400, 400);");
        String actual = parser.getElementaryInstruction(parser.getNextInstruction());
        assertEquals("draw",actual);
    }

    @Test
    void getElementaryInstructionReturnsCorrectionElementaryWithBrackets() {
        parser.setText("\\draw[color=red] (100, 100) rectangle (400, 400);");
        String actual = parser.getElementaryInstruction(parser.getNextInstruction());
        assertEquals("draw",actual);
    }

    @Test
    void getInstructionOptionsReturnsDefaultWhenNoOptions() {
        parser.setText("\\draw (100, 100) rectangle (400, 400);");
        String[] actual = parser.getInstructionOptions(parser.getNextInstruction());
        String[] expected = {ConstantsUtils.defaultShapeColor, ConstantsUtils.defaultShapeSize, ConstantsUtils.emptyString};
        assertArrayEquals(expected,actual);
    }

    @Test
    void getInstructionOptionsReturnsCorrectColorWhenItsTheOnlyOption() {
        parser.setText("\\draw[color=red] (100, 100) rectangle (400, 400);");
        String[] actual = parser.getInstructionOptions(parser.getNextInstruction());
        String[] expected = {"red", "300", ConstantsUtils.emptyString};
        assertArrayEquals(expected,actual);
    }

    @Test
    void getInstructionOptionsReturnsCorrectOneStringWhenItsTheOnlyOption() {
        parser.setText("\\draw[dash] (100, 100) rectangle (400, 400);");
        String[] actual = parser.getInstructionOptions(parser.getNextInstruction());
        String[] expected = {ConstantsUtils.defaultShapeColor, "300", ConstantsUtils.oneString};
        assertArrayEquals(expected,actual);
    }

    @Test
    void getInstructionOptionsReturnsCorrectTwoStringWhenItsTheOnlyOption() {
        parser.setText("\\draw[->] (100, 100) rectangle (400, 400);");
        String[] actual = parser.getInstructionOptions(parser.getNextInstruction());
        String[] expected = {ConstantsUtils.defaultShapeColor,"300", ConstantsUtils.twoString};
        assertArrayEquals(expected,actual);
    }

    @Test
    void getInstructionOptionsReturnsCorrectOptions() {
        parser.setText("\\draw[color=loukas,->] (100, 100) rectangle (400, 400);");
        String[] actual = parser.getInstructionOptions(parser.getNextInstruction());
        String[] expected = {"loukas","300", ConstantsUtils.twoString};
        assertArrayEquals(expected,actual);
    }

    @Test
    void getLinkInInstructionDetectingRectangle() {
        parser.setText("\\draw (100, 100) rectangle (400, 400);");
        String actual = parser.getLinkInInstruction(parser.getNextInstruction());
        assertEquals(ConstantsUtils.rectangleString,actual);
    }

    @Test
    void getLinkInInstructionDetectingCircle() {
        parser.setText("\\draw (100, 100) circle (400, 400);");
        String actual = parser.getLinkInInstruction(parser.getNextInstruction());
        assertEquals(ConstantsUtils.circleString,actual);
    }

    @Test
    void getLinkInInstructionDetectingLine() {
        parser.setText("\\draw (100, 100) -- (400, 400);");
        String actual = parser.getLinkInInstruction(parser.getNextInstruction());
        assertEquals(ConstantsUtils.lineFormString,actual);
    }

    @Test
    void getCoordinatesReturnsCorrectFirstCoordinatesForCircle() {
        parser.setText("\\draw (200, 200) circle (100pt);");
        Double[][] actual = parser.getCoordinates(parser.getNextInstruction());
        Double[] expected = {200.0,200.0};
        assertArrayEquals(expected,actual[0]);
    }

    @Test
    void getCoordinatesReturnsCorrectSecondCoordinatesForCircle() {
        parser.setText("\\draw (200, 200) circle (100pt);");
        Double[][] actual = parser.getCoordinates(parser.getNextInstruction());
        Double[] expected = {100.0,0.0};
        assertArrayEquals(expected,actual[1]);
    }

    @Test
    void getCoordinatesReturnsCorrectFirstCoordinatesForNotCircle() {
        parser.setText("\\draw (100, 100) -- (400, 400);");
        Double[][] actual = parser.getCoordinates(parser.getNextInstruction());
        Double[] expected = {100.0,100.0};
        assertArrayEquals(expected,actual[0]);
    }

    @Test
    void getCoordinatesReturnsCorrectSecondCoordinatesForNotCircle() {
        parser.setText("\\draw (100, 100) -- (400, 400);");
        Double[][] actual = parser.getCoordinates(parser.getNextInstruction());
        Double[] expected = {400.0,400.0};
        assertArrayEquals(expected,actual[1]);
    }

    @Test
    void getNextInstructionReturnsFirstInstruction() {
        String instruction = "\\draw (100, 100) -- (400, 400)";
        String instruction2 = "\\draw[color=red,->] (100, 100) -- (400, 400)";
        String instruction3 = "\\draw[color=red] (100, 100) circle (40pt)";
        parser.setText(instruction+";\n"+instruction2+";\n"+instruction3+";");
        assertEquals(instruction,parser.getNextInstruction());
    }

    @Test
    void getNextInstructionReturnsSecondInstruction() {
        String instruction = "\\draw (100, 100) -- (400, 400)";
        String instruction2 = "\\draw[color=red,->] (100, 100) -- (400, 400)";
        String instruction3 = "\\draw[color=red] (100, 100) circle (40pt)";
        parser.setText(instruction+";\n"+instruction2+";\n"+instruction3+";");
        parser.getNextInstruction();
        assertEquals(instruction2,parser.getNextInstruction());
    }

    @Test
    void getNextInstructionReturnsThirdInstruction() {
        String instruction = "\\draw (100, 100) -- (400, 400)";
        String instruction2 = "\\draw[color=red,->] (100, 100) -- (400, 400)";
        String instruction3 = "\\draw[color=red] (100, 100) circle (40pt)";
        parser.setText(instruction+";\n"+instruction2+";\n"+instruction3+";");
        parser.getNextInstruction();
        parser.getNextInstruction();
        assertEquals(instruction3, parser.getNextInstruction());
    }
}
