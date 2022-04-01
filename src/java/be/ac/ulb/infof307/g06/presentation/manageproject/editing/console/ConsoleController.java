package be.ac.ulb.infof307.g06.presentation.manageproject.editing.console;

import be.ac.ulb.infof307.g06.models.tikz.Parser;
import be.ac.ulb.infof307.g06.models.shapes.*;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Manage the Tikz part
 */
public class ConsoleController {
    private final ConsoleView view;
    private final Parser parser;
    private ConsoleControllerListener listener;
    private Integer instructionPointer;
    private Stage primaryStage;

    /**
     * Used for tests.
     */
    protected ConsoleController() {
        instructionPointer = 0;
        view = null;
        parser = new Parser();
    }

    /**
     * Constructor that instantiate the console with the given content.
     * @param content the content to display in the console.
     */
    public ConsoleController(String content) {
        this.primaryStage = StageUtils.getStage();
        instructionPointer = 0;
        view = new ConsoleView();
        parser = new Parser();
        setText(content);
    }

    /**
     * Translates the text in the consoleView into drawings.
     */
    public void translate() {
        parser.goToBegin();
        parser.setText(view.getText());
        int len = ConsoleView.getCodeArea().getLength();
        int i = 0;
        String instruction = parser.getNextInstruction();
        while (!instruction.equals(ConstantsUtils.zeroString) && len > 0) {
            String[] options = new String[1];
            options[0] = parser.getLinkInInstruction(instruction);
            if (!options[0].equals(ConstantsUtils.lineFormString) && !options[0].equals(ConstantsUtils.rectangleString) && !options[0].equals(ConstantsUtils.circleString)) {
                instruction = ConstantsUtils.zeroString;
            } else {
                String elementary = parser.getElementaryInstruction(instruction);
                String[] instOp = parser.getInstructionOptions(instruction);
                Double[][] coord = parser.getCoordinates(instruction);
                translateCodeIntoDrawing(elementary, instOp, options, coord);
                instruction = parser.getNextInstruction();
                len -= ConsoleView.getCodeArea().getParagraphLength(i) + 1;
                i++;
            }
        }
    }

    /**
     * Method that replace all the text code area with the text given in parameter.
     * @param text String - The text to display in the code area.
     */
    public void changeText(String text) {
        view.changeText(text);
    }

    /**
     * Extract DiagramShape from an instruction line.
     * @param instruction the instruction line to analyze.
     * @param instructionOptions the instructions options.
     * @param coordinates position on the drawing.
     * @return the object DiagramShape Constructed by The instruction.
     */
    public DiagramShapeModel extractInformationFromCodeLine(String instruction, String[] instructionOptions, Double[][] coordinates) {
        int size = 30;
        String type = ConstantsUtils.edgeString;
        Color color = Color.web(instructionOptions[0]);
        Color colorFill = color;
        if (instruction.contains(ConstantsUtils.drawString)) {
            if (!instructionOptions[1].equals(ConstantsUtils.emptyString)) {
                size = Integer.parseInt(instructionOptions[1]);
            }
            if (instructionOptions[2].equals(ConstantsUtils.oneString)) {
                type = ConstantsUtils.dashString;
            } else if (instructionOptions[2].equals(ConstantsUtils.twoString)) {
                type = ConstantsUtils.arcString;
            }
        } else if (instruction.contains(ConstantsUtils.filldrawString)) {
            colorFill = Color.web(instructionOptions[1]);
            if (!instructionOptions[2].equals(ConstantsUtils.emptyString))
                size = Integer.parseInt(instructionOptions[2]);
        }
        return this.getShapeFromCode(instruction ,type, color, size, coordinates[0], coordinates[1]);
    }

    /**
     * Translate the code to a diagram shape model.
     * @param instruction the instruction to translate.
     * @param type the type of the shape.
     * @param color the color of the shape.
     * @param size the size of the shape.
     * @param coordinates1 the x coordinate.
     * @param coordinates2 the y coordinate.
     * @return return an instance of diagramShapeModel.
     */
    private DiagramShapeModel getShapeFromCode(String instruction, String type, Color color, int size, Double[] coordinates1, Double[] coordinates2) {
        String shape = this.parser.getLinkInInstruction(instruction);
        DiagramShapeModel diagramShapeModel = null;
        if(!shape.equals(ConstantsUtils.lineFormString)) {
            switch (shape) {
                case ConstantsUtils.circleString:
                    diagramShapeModel = new CircleModel(type, color, size, new Double[]{coordinates1[0], coordinates1[1], coordinates2[0], coordinates2[1]});
                    break;
                case ConstantsUtils.rectangleString:
                    diagramShapeModel = new RectangleModel(type, color, size, new Double[]{coordinates1[0], coordinates1[1], coordinates2[0], coordinates2[1]});
                    break;
            }
        } else {
            switch (type) {
                case ConstantsUtils.edgeString:
                    diagramShapeModel = new LineModel(type, color, size, coordinates1, coordinates2);
                    break;
                case ConstantsUtils.dashString:
                    diagramShapeModel = new DashModel(type, color, size, coordinates1, coordinates2);
                    break;
                case ConstantsUtils.arcString:
                    diagramShapeModel = new ArcModel(type, color, size, coordinates1, coordinates2);
                    break;
            }
        }
        return diagramShapeModel;
    }
    /**
     * Translate simple instructions into drawing
     * @param instruction        : The instruction to translate.
     * @param instructionOptions : Options about the instruction
     * @param link               : Other options
     * @param coordinates        : Coordinates
     */
    private void translateCodeIntoDrawing(String instruction, String[] instructionOptions, String[] link, Double[][] coordinates) {
        boolean filled = false;
        int size = 30;
        String type = ConstantsUtils.edgeString;
        Color color = Color.web(instructionOptions[0]);
        Color colorFill = color;
        if (instruction.equals(ConstantsUtils.drawString) || instruction.equals(ConstantsUtils.fillString)) {
            if (instruction.equals(ConstantsUtils.fillString)) {
                filled = true;
            } else {
                if (!instructionOptions[1].equals(ConstantsUtils.emptyString)) {
                    size = Integer.parseInt(instructionOptions[1]);
                }
                if (instructionOptions[2].equals(ConstantsUtils.oneString)) {
                    type = ConstantsUtils.dashString;
                }
                if (instructionOptions[2].equals(ConstantsUtils.twoString)) {
                    type = ConstantsUtils.arcString;
                }
            }
        } else if (instruction.equals(ConstantsUtils.filldrawString)) {
            colorFill = Color.web(instructionOptions[1]);
            filled = true;
            if (!instructionOptions[2].equals(ConstantsUtils.emptyString))
                size = Integer.parseInt(instructionOptions[2]);
        }
        draw(type, color, colorFill, size, coordinates, link[0], filled);
        if (instructionPointer > 0) {
            instructionPointer--;
        }
    }

    /**
     * Draws the shape in the drawing controller
     * @param type        of the shape to draw
     * @param color       of the shape to draw
     * @param colorFill   of the shape to draw
     * @param size        to draw
     * @param coordinates of the shape to draw
     * @param link        to draw
     * @param filled      if the shape is filled
     */
    private void draw(String type, Color color, Color colorFill, Integer size, Double[][] coordinates, String link, Boolean filled) {
        if (link.equals(ConstantsUtils.rectangleString)) {
            if (filled)
                listener.fillRectangle(colorFill, size, coordinates);
            else
                listener.drawRectangle(type, color, size, coordinates);
        } else if (link.equals(ConstantsUtils.circleString)) {
            if (filled)
                listener.fillCircle(colorFill, size, coordinates);
            else
                listener.drawCircle(color, size, coordinates);
        } else {
            for (int i = 1; i < coordinates.length; i++) {
                listener.drawShape(type, color, size, coordinates[i - 1], coordinates[i]);
            }
        }
    }

    /**
     * @return the text of the current diagram
     */
    public String getText() {
        return view.getText();
    }

    /**
     * @param text code of the diagram
     */
    public void setText(String text) {
        view.addCode(text);
    }

    /**
     * @return the view associated to the controller
     */
    public ConsoleView getView() {
        return view;
    }

    /**
     * @param listener to link controller to the view
     */
    public void setListener(ConsoleControllerListener listener) {
        this.listener = listener;
    }
}