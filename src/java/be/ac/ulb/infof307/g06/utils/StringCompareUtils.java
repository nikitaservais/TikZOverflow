package be.ac.ulb.infof307.g06.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contains static methods to get the differences between strings
 */
public class StringCompareUtils {

    /**
     * Get all the insertions that occurred in newText compared to originalText
     * @param originalText : the first string
     * @param newText : the new string that we will be comparing to the original one
     * @return an ArrayList with inserted line in newText
     */
    public static ArrayList<String> getInsertions(String originalText, String newText) {
        ArrayList<String> newTextArray = splitIntoArrayList(newText);
        if (!originalText.isEmpty()) {
            ArrayList<String> originalTextArray = splitIntoArrayList(originalText);
            for(String line : originalTextArray){
                newTextArray.remove(line);
            }
        }
        return newTextArray;
    }

    /**
     * Gets all the deletions that occurred in newText compared to originalText
     * @param originalText : the first string
     * @param newText : the new string that we will be comparing to the original one
     * @return an ArrayList with deleted line in newText
     */
    public static ArrayList<String> getDeletions(String originalText, String newText) {
        ArrayList<String> newTextArray = splitIntoArrayList(newText);
        ArrayList<String> originalTextArray = new ArrayList<>();
        if (!originalText.isEmpty()) {
            originalTextArray = splitIntoArrayList(originalText);
            for(String line : newTextArray){
                originalTextArray.remove(line);
            }
        }
        return originalTextArray;
    }

    /**
     * Splits a text into an ArrayList containing each line
     * @param text : the string to split into an arraylist
     * @return an arraylist of String containing each line of the text
     */
    public static ArrayList<String> splitIntoArrayList(String text){
        return new ArrayList<>(Arrays.asList(text.split("\n")));
    }

    /**
     * Get all the lines in common between newText and originalText
     * @param originalText : the first string
     * @param newText : the new string that we will be comparing to the original one
     * @return an ArrayList with common lines
     */
    public static ArrayList<String> getCommon(String originalText, String newText) {
        ArrayList<String> originalTextArray = splitIntoArrayList(originalText);
        ArrayList<String> newTextArray = splitIntoArrayList(newText);
        ArrayList<String> result = new ArrayList<>();
        for(String line : newTextArray){
            if(originalTextArray.contains(line) &&
                    getNumberOfOccurrence(line,originalTextArray) != getNumberOfOccurrence(line,result)&&
                    getNumberOfOccurrence(line,newTextArray) != getNumberOfOccurrence(line,result)
            ) result.add(line);
        }
        return  result;
    }

    /**
     * Counts the number occurrences of a string in an arrayList
     * @param stringToCount : the string to count
     * @param arrayToParse : the array to parse
     * @return res : an int containing the number of occurrences
     */
    protected static int getNumberOfOccurrence(String stringToCount, ArrayList<String> arrayToParse){
        int res = 0;
        for(String line : arrayToParse){
            if(line.equals(stringToCount)) res++;
        }
        return res;
    }

}
