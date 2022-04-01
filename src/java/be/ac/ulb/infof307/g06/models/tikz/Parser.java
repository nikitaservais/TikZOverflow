package be.ac.ulb.infof307.g06.models.tikz;

import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import java.util.Arrays;

/**
 * parses a text to add syntax coloration and provides information on instructions
 */
public class Parser {
    private String text;
    private int cursorChar;

    /**
     * Constructor
     */
    public Parser() {
        text = ConstantsUtils.emptyString;
        cursorChar = 0;
    }

    /**
     * Reset the position of cursor
     */
    public void goToBegin() {
        cursorChar = 0;
        cursorChar = text.indexOf("\\");
    }

    /**
     * Extract the TikZ main instruction (for instance draw)
     * @param instruction is the full line
     * @return the elementary instruction
     */
    public String getElementaryInstruction(String instruction) {
        if (instruction.indexOf(ConstantsUtils.leftBracketChar) == -1) {
            return instruction.substring(1, instruction.indexOf(" "));
        } else {
            return instruction.substring(1, instruction.indexOf(ConstantsUtils.leftBracketChar));
        }
    }

    /**
     * Extract the TikZ options (in [])
     * @param instruction is the full line
     * @return a readable array with parsed options [color, size, type]
     */
    public String[] getInstructionOptions(String instruction) {
        String[] selectedOptions = new String[3];
        selectedOptions[0] = ConstantsUtils.defaultShapeColor;
        selectedOptions[1] = ConstantsUtils.defaultShapeSize; // used all the time
        selectedOptions[2] = ConstantsUtils.emptyString;
        if (instruction.indexOf(ConstantsUtils.leftBracketChar) != -1) { // check if specified options in instruction
            String options = instruction.substring(instruction.indexOf(ConstantsUtils.leftBracketChar) + 1, instruction.indexOf(ConstantsUtils.rightBracketChar));
            String[] listOfOptions = options.split(ConstantsUtils.commaString);
            // setup color
            for (int i = 0; i < listOfOptions.length; i++) {
                if (listOfOptions[i].length() >= 6 && listOfOptions[i].substring(0, 5).equals(ConstantsUtils.colorStringOption)) {
                    selectedOptions[0] = listOfOptions[i].split("=")[1];
                    break;
                }
            }

            if (Arrays.asList(listOfOptions).contains(ConstantsUtils.dashString)) {
                selectedOptions[2] = ConstantsUtils.oneString;
            }
            else if (Arrays.asList(listOfOptions).contains(ConstantsUtils.arrowFormString)) {
                selectedOptions[2] = ConstantsUtils.twoString;
            }

            if(instruction.contains(ConstantsUtils.rectangleString)){
                selectedOptions[1] = Integer.toString(getRectangleSizeFromInstruction(instruction));
            }
            else if(instruction.contains(ConstantsUtils.circleString)){
                String circleSize1 = instruction.split(" circle ")[1];
                selectedOptions[1] = circleSize1.split("pt")[0].substring(1).replace(" ","");
            }
        }
        return selectedOptions;
    }

    /**
     * Used to parse the instruction and get the size of the rectangle
     * @param instruction the full instruction line from the code area
     * @return the size of the rectangle
     */
    protected Integer getRectangleSizeFromInstruction(String instruction) {
        String[] temp1 = instruction.split( " "+ ConstantsUtils.rectangleString+" ");
        String[] temp2 = {temp1[0].split("] ")[1],temp1[1]};
        Integer[] positionOfXs = {new Integer(temp2[0].split(ConstantsUtils.commaString)[0].substring(1)),
                new Integer(temp2[1].split(ConstantsUtils.commaString)[0].substring(1))};
        return positionOfXs[1]-positionOfXs[0];
    }

    /**
     * Extracts the way to link the dots
     * @param instruction is the full line
     * @return the part of the line specifying the link (--, circle or rectangle)
     */
    public String getLinkInInstruction(String instruction) {
        return (instruction.substring(instruction.indexOf(ConstantsUtils.rightParenthesisChar) + 1, instruction.indexOf(ConstantsUtils.rightParenthesisChar)
                + instruction.substring(instruction.indexOf(ConstantsUtils.rightParenthesisChar)).indexOf(ConstantsUtils.leftParenthesisChar))).trim();
    }

    /**
     * Extracts the coordinates. In case of circle, the first tuple is the center, and the second is the radius and zero
     * @param instruction is the full line
     * @return a 2x2 matrix with coordinates
     */
    public Double[][] getCoordinates(String instruction) {
        String[] parsedCoordinates = instruction.split(getLinkInInstruction(instruction));
        Double[][] result = new Double[2][2];
        String[] firstCoordinate = parsedCoordinates[0].substring(parsedCoordinates[0].indexOf(ConstantsUtils.leftParenthesisChar) + 1,
                parsedCoordinates[0].indexOf(ConstantsUtils.rightParenthesisChar)).split(ConstantsUtils.commaString);
        result[0][0] = Double.parseDouble(firstCoordinate[0]);
        result[0][1] = Double.parseDouble(firstCoordinate[1]);

        if (!getLinkInInstruction(instruction).equals(ConstantsUtils.circleString)) { // si ce n'est pas un cercle
            String[] secondCoordinate = parsedCoordinates[1].substring(parsedCoordinates[1].indexOf(ConstantsUtils.leftParenthesisChar) + 1,
                    parsedCoordinates[1].indexOf(ConstantsUtils.rightParenthesisChar)).split(ConstantsUtils.commaString);
            result[1][0] = Double.parseDouble(secondCoordinate[0]);
            result[1][1] = Double.parseDouble(secondCoordinate[1]);
        } else {
            String secondCoordinate = parsedCoordinates[1].substring(parsedCoordinates[1].indexOf(ConstantsUtils.leftParenthesisChar) + 1,
                    parsedCoordinates[1].indexOf('p'));
            result[1][0] = Double.parseDouble(secondCoordinate);
            result[1][1] = 0.0;
        }
        return result;
    }

    /**
     * Sets the cursor at the begin of the next instruction
     * @return the instruction just read or Constants.zeroString if end of text or unreadable text
     */
    public String getNextInstruction() {
        if (cursorChar != -1 && cursorChar < text.length()) { // -1: no \ found, text.length(): end of text
            Integer endOfInstructionIndex = cursorChar + text.substring(cursorChar).indexOf(";\n") - 1;
            if (endOfInstructionIndex > 0 || (cursorChar + text.substring(cursorChar).indexOf(";")) == text.length() - 1) {
                if ((cursorChar + text.substring(cursorChar).indexOf(";")) == text.length() - 1) {
                    endOfInstructionIndex = text.length() - 2;
                }
                String instruction = text.substring(cursorChar, endOfInstructionIndex + 1);
                if ((text.substring(endOfInstructionIndex)).indexOf('\\') != -1) { // if a \ is found
                    cursorChar = (text.substring(endOfInstructionIndex)).indexOf('\\') + endOfInstructionIndex;
                } else {
                    cursorChar = -1;
                }
                return instruction;
            }
        }
        return ConstantsUtils.zeroString;
    }

    public String getText() { return text; }

    public void setText(String newText) {
        text = newText;
        cursorChar = text.indexOf("\\");
    }
}