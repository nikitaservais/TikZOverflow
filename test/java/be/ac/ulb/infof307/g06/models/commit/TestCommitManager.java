package be.ac.ulb.infof307.g06.models.commit;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.models.commit.CommitManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * These are the tests for the CommitManager class.
 *
 * The way this is being tested is that we are going to pass
 * a string to the class and in function of the return values
 * of the getters we can identify if the parseCommitContent
 * function works properly.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestCommitManager {
    //to test parseCommitContent
    private CommitManager commitManager;
    private String formatedContent = "commit 10003413\n" +
            "\t+\\draw (1,2) circle (3cm);\n" +
            "\t-\\draw (12,13) -- (54,12);\n" +
            "\t+\\draw (12,13) rectangle (13, 14);\n\n";

    @BeforeEach
    public void setUp() throws DatabaseConnectionException {
        commitManager = new CommitManager();
    }

    @Test
    public void getNewLinesReturnsTwoAdditions(){
        commitManager.parseCommitContent(formatedContent);
        ArrayList<String> expectedAdditions = new ArrayList<>(
                Arrays.asList("\\draw (1,2) circle (3cm);", "\\draw (12,13) rectangle (13, 14);"));
        assertEquals(expectedAdditions, commitManager.getNewLines());
    }

    @Test
    public void getRemovedLinesReturnsOneDeletion(){
        commitManager.parseCommitContent(formatedContent);
        ArrayList<String> expectedAdditions = new ArrayList<>(
                Arrays.asList("\\draw (12,13) -- (54,12);"));
        assertEquals(expectedAdditions, commitManager.getRemovedLines());
    }

    @Test
    public void isAddedLineCorrectlyAdd() {
        String line = "slip de bain";
        commitManager.addNewLine(line);
        assertTrue(commitManager.getNewLines().contains(line));
    }

    @Test
    public void isRemovedLineCorrectlyAdd() {
        String line = "slip de bain";
        commitManager.addRemovedLine(line);
        assertTrue(commitManager.getRemovedLines().contains(line));
    }

    @Test
    public void isCommitAsStringWellStructuredWithOnlyAddedLines() {
        commitManager.addNewLine("good morning");
        commitManager.addNewLine("ladies and gentlemen");
        commitManager.setCommitID(4);
        String expected = "commit 4\n" +
                "\t+good morning\n" +
                "\t+ladies and gentlemen\n";
        assertEquals(expected, commitManager.toString());
    }

    @Test
    public void isCommitAsStringWellStructuredWithOnlyRemovedLines() {
        commitManager.addRemovedLine("every greek can die");
        commitManager.addRemovedLine("Socrate is a greek");
        commitManager.addRemovedLine("So Socrate is mortal");
        commitManager.setCommitID(6);
        String expected = "commit 6\n" +
                "\t-every greek can die\n" +
                "\t-Socrate is a greek\n" +
                "\t-So Socrate is mortal\n";
        assertEquals(expected, commitManager.toString());
    }

    @Test
    public void isCommitAsStringWellStructuredWithAddedAndRemovedLines() {
        commitManager.addNewLine("good morning");
        commitManager.addRemovedLine("ladies and gentlemen");
        commitManager.addNewLine("ladies, gentlemen and everyone else");
        commitManager.setCommitID(15);
        String expected = "commit 15\n" +
                "\t+good morning\n" +
                "\t+ladies, gentlemen and everyone else\n" +
                "\t-ladies and gentlemen\n";
        assertEquals(expected, commitManager.toString());
    }

    @Test
    public void isCommitAsStringWellStructuredWhenEmpty() {
        commitManager.setCommitID(42);
        String expected = "commit 42\n";
        assertEquals(expected, commitManager.toString());
    }
}