package be.ac.ulb.infof307.g06.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestStringCompareUtils extends StringCompareUtils {
    String originalText;
    String emptyText;
    String invertedLine;
    String insertedLine;
    String deletedLine;

    @BeforeAll
    void setup(){
        originalText = "\\draw[color=0x000000ff,fill=0x000000ff] (160,222) circle (50pt);\n" +
                "\\draw[color=0xcc3333ff,fill=0xcc3333ff] (307,424) circle (50pt);\n" +
                "\\draw[color=0x000000ff,fill=0x000000ff] (411,247) circle (50pt);\n" +
                "\\draw[color=0x000000ff,->] (201,251) -- (421,272);";
        emptyText = "";
        invertedLine = "\\draw[color=0x000000ff,fill=0x000000ff] (160,222) circle (50pt);\n" +
                "\\draw[color=0x000000ff,fill=0x000000ff] (411,247) circle (50pt);\n" +
                "\\draw[color=0x000000ff,->] (201,251) -- (421,272);\n" +
                "\\draw[color=0xcc3333ff,fill=0xcc3333ff] (307,424) circle (50pt);";
        insertedLine = "\\draw[color=0x000000ff,fill=0x000000ff] (160,222) circle (50pt);\n" +
                "\\draw[color=0xcc3333ff,fill=0xcc3333ff] (307,424) circle (50pt);\n" +
                "\\draw[color=0x000000ff,fill=0x000000ff] (411,247) circle (50pt);\n" +
                "\\draw[color=0x000000ff,->] (201,251) -- (421,272);\n" +
                "\\draw[color=0x000000ff,->] (201,251) -- (421,272);";
        deletedLine = "\\draw[color=0x000000ff,fill=0x000000ff] (160,222) circle (50pt);\n" +
                "\\draw[color=0xcc3333ff,fill=0xcc3333ff] (307,424) circle (50pt);\n" +
                "\\draw[color=0x000000ff,fill=0x000000ff] (411,247) circle (50pt);\n";
    }

    /**
     * Checks if the content is the same and has no deletion
     */
    @Test
    public void sameStringGetDeletionEmptyReturnsTrue(){
        assertTrue(StringCompareUtils.getDeletions(originalText,originalText).isEmpty());
    }

    /**
     * Checks if the content is the same and has no insertion
     */
    @Test
    public void sameStringGetInsertionEmptyReturnsTrue(){
        assertTrue(StringCompareUtils.getInsertions(originalText,originalText).isEmpty());
    }

    /**
     * Checks if the content is the same
     */
    @Test
    public void sameStringTheSameCommonReturnsTrue(){
        assertTrue(compareStrings(originalText, StringCompareUtils.getCommon(originalText,originalText)));
    }

    /**
     * Checks if insertion is detected
     */
    @Test
    public void insertedLineGetInsertionEmptyReturnsFalse(){
        assertFalse(StringCompareUtils.getInsertions(originalText,insertedLine).isEmpty());
    }

    /**
     * Checks if there is no deletion
     */
    @Test
    public void insertedLineGetDeletionEmptyReturnsTrue(){
        assertTrue(StringCompareUtils.getDeletions(originalText,insertedLine).isEmpty());
    }

    /**
     * Checks if the inserted string has the same common lines
     */
    @Test
    public void insertedStringTheSameCommonReturnsTrue(){
        assertTrue(compareStrings(originalText, StringCompareUtils.getCommon(originalText,insertedLine)));
    }

    /**
     * Checks if no insertion is detected
     */
    @Test
    public void deletedLineGetInsertionEmptyReturnsTrue(){
        assertTrue(StringCompareUtils.getInsertions(originalText,deletedLine).isEmpty());
    }

    /**
     * Checks if deletion is detected
     */
    @Test
    public void deletedLineGetDeletionEmptyReturnsFalse() {
        assertFalse(StringCompareUtils.getDeletions(originalText, deletedLine).isEmpty());
    }

    /**
     * Checks if deleted line string has the same common
     */
    @Test
    public void deletedLineTheSameCommonReturnsFalse() {
        assertFalse(compareStrings(originalText, StringCompareUtils.getCommon(originalText,deletedLine)));
    }


    /**
     * Detects if no insertion when lines are only swapped
     */
    @Test
    public void invertedLineGetInsertionEmptyReturnsTrue(){
        assertTrue(StringCompareUtils.getInsertions(originalText,invertedLine).isEmpty());
    }

    /**
     * Checks if no deletion when lines are only swapped
     */
    @Test
    public void invertedLineGetDeletionEmptyReturnsTrue() {
        assertTrue(StringCompareUtils.getInsertions(originalText, invertedLine).isEmpty());
    }


    /**
     * Checks if the content is the same as original when lines are swapped
     */
    @Test
    public void invertedLineTheSameCommonReturnsTrue() {
        assertTrue(compareStrings(originalText, StringCompareUtils.getCommon(originalText, invertedLine)));
    }


    /**
     * Checks if the numbre of occurrence of "a" is equal to 3
     */
    @Test
    public void getNumberOfOccurrenceReturnsCorrectNumber(){
        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("a");
        testArray.add("a");
        testArray.add("a");
        assertEquals(3,getNumberOfOccurrence("a",testArray));
    }

    /**
     * Tests the split function
     */
    @Test
    public void testArrayIsCorrectlySplit(){
        ArrayList<String> testArray = splitIntoArrayList(originalText);
        assertTrue(compareStrings(originalText, testArray));
    }

    /**
     * Checks if the content is still the same
     * @param originalText : the string of the first text
     * @param arrayList : an array containing the second text split by the new lines
     * @return  res : a boolean that indicate if the 2 texts are the sames
     */
    private boolean compareStrings(String originalText,ArrayList<String> arrayList){
        ArrayList<String> originalArray = splitIntoArrayList(originalText);
        for(String line : arrayList){
            originalArray.remove(line);
        }
        return originalArray.isEmpty();
    }

    /**
     * Checks if insertion is detected with an empty original text
     */
    @Test
    void emptyOriginalTextGetInsertionEmptyReturnsFalse() {
        assertFalse(StringCompareUtils.getInsertions(emptyText,insertedLine).isEmpty());

    }

    /**
     * Checks there is no deletion with an empty original text
     */
    @Test
    void emptyOriginalTextGetDeletionEmptyReturnsTrue() {
        assertTrue(StringCompareUtils.getDeletions(emptyText,insertedLine).isEmpty());
    }

    /**
     * Checks there is no common part with an original text
     */
    @Test
    void emptyOriginalTextTheSameCommonReturnsFalse() {
        assertFalse(compareStrings(originalText, StringCompareUtils.getCommon(emptyText,insertedLine)));
    }


}
