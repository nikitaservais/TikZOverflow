package be.ac.ulb.infof307.g06.presentation.manageproject.editing.copypaste;

import be.ac.ulb.infof307.g06.models.CopyPasteModel;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.drawingcanvas.DrawingCanvasController;
import be.ac.ulb.infof307.g06.models.tikz.Parser;
import be.ac.ulb.infof307.g06.models.shapes.DiagramShapeModel;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * This class handles the interaction with the user in selection mode
 */
public class CopyPasteController implements CopyPasteListener {
    private final DrawingCanvasController drawingCanvasController;
    private ArrayList<String> code;
    private double firstX;
    private double firstY;
    private double secondX;
    private double secondY;
    private final Parser parser;
    private boolean selectionMode;
    private CopyPasteModel clipboard;

    /**
     * Constructor of the CopyPasteController
     * @param drawingCanvasController the instance of the controller of this class.
     */
    public CopyPasteController(DrawingCanvasController drawingCanvasController) {
        clipboard = null;
        this.firstX = 0.0;
        this.firstY = 0.0;
        this.secondX = 0.0;
        this.secondY = 0.0;
        this.drawingCanvasController = drawingCanvasController;
        this.parser = new Parser();
        this.selectionMode = false;
    }

    /**
     * Stores the code to detect contained code in selection
     * @param code the written code
     */
    @Override
    public void setCode(String code) {
        this.code = new ArrayList<>();
        String[] codeSplit = code.split("\n");
        for (String line : codeSplit) {
            this.code.add(line);
        }
    }

    /**
     * Sets the selection mode is active or not.
     * @param value True if selection mode is on, false otherwise.
     */
    @Override
    public void setSelectionMode(Boolean value) {
        this.selectionMode = value;
    }

    /**
     * Set the first selection coordinates.
     * @param x the x value of the coordinate
     * @param y the Y value of the coordinate
     */
    @Override
    public void setFirstSelectionCoordinates(double x, double y) {
        this.firstX = x;
        this.firstY = y;
    }

    /**
     * Set the second selection coordinates.
     * @param x the x value of the coordinate
     * @param y the Y value of the coordinate
     */
    @Override
    public void setSecondSelectionCoordinates(double x, double y) {
        this.secondX = x;
        this.secondY = y;
    }

    /**
     * Determine if, for each shape, at least one point is contained in the shape, and if so, stores it as a shape in an arraylist
     * @return the arraylist containing contained shapes
     */
    @Override
    public ArrayList<DiagramShapeModel> determineShapesInSelection() {
        ArrayList<DiagramShapeModel> selection = new ArrayList<>();
        for (int i = 0; i < this.code.size(); i++) {
            if (this.isShapeAsCodeInsideSelection(this.code.get(i))) {
                selection.add(this.getShapeFromInstruction(this.code.get(i)));
            }
        }
        return selection;
    }

    /**
     * Gets the shape that a instruction is representing
     * @param instruction the code to translate
     * @return the corresponding shape
     */
    private DiagramShapeModel getShapeFromInstruction(String instruction) {
        String[] instOp = parser.getInstructionOptions(instruction);
        Double[][] coord = parser.getCoordinates(instruction);
        return this.drawingCanvasController.extractInformationFromCodeLine(instruction, instOp, coord);
    }

    /**
     * Determine is the shape represented by code line is inside the selection
     * @param instruction the shape as code
     * @return true if the shape is in selection, otherwise false
     */
    private Boolean isShapeAsCodeInsideSelection(String instruction) {
        String shape = this.parser.getLinkInInstruction(instruction);
        Double[][] coordinates = this.parser.getCoordinates(instruction);
        double leftX = min(this.firstX, this.secondX);
        double rightX = max(this.firstX, this.secondX);
        double upperY = min(this.firstY, this.secondY);
        double lowerY = max(this.firstY, this.secondY);
        Boolean inside = (coordinates[0][0] > leftX) && (coordinates[0][0] < rightX) && (coordinates[0][1] > upperY) && (coordinates[0][1] < lowerY);
        if (!shape.equals(ConstantsUtils.circleString)) {
            inside = inside || ((coordinates[1][0] > leftX) && (coordinates[1][0] < rightX) && (coordinates[1][1] > upperY) && (coordinates[1][1] < lowerY));
        }
        return inside;
    }

    public Double[] getFirstCoordinates() {
        return new Double[]{this.firstX, this.firstY};
    }

    public Double[] getSecondCoordinates() {
        return new Double[]{this.secondX, this.secondY};
    }

    public Double[] getUpperLeftCoordinates() {
        return new Double[]{min(this.firstX, this.secondX), min(this.firstY, this.secondY)};
    }

    public Double[] getDownRightCoordinates() {
        return new Double[]{max(this.firstX, this.secondX), max(this.firstY, this.secondY)};
    }

    /**
     * called when ctrl + c  or just by selecting to keep  the last copy value
     */
    @Override
    public void setCopy() {
        clipboard = new CopyPasteModel(determineShapesInSelection(), getUpperLeftCoordinates());
    }

    /** called when ctrl +v to paste the shapes or by pressing right click
     * @param destination where the upper left corner will be
     * @return the shapes with the correct coordinates
     */
    @Override
    public ArrayList<DiagramShapeModel> getNewPastedShapes(Double[] destination) {
        if(clipboard == null) return null;
        return clipboard.getPastedShapesCoordinates(destination);
    }
}
