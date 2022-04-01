package be.ac.ulb.infof307.g06.utils;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to see if we are able to properly read out the content of
 * our test file
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestFileUtils {

    private String content;
    File testFile;

    @BeforeAll
    void setUp() {
        // Prepare the test file.
        content = "This is a test.";
        testFile = new File("test/testFile");
        try {
            testFile.createNewFile();
            FileWriter fileWriter = new FileWriter(testFile, false);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e){
            fail("Couldn't create the test file");
        }
    }

    @Test
    void isContentEqualToTheString() {
        try {
            assertEquals(content, FileUtils.fileToString(testFile.getPath()));
        } catch (IOException e){
            fail("Couldn't open the test file");
        }
    }

    @AfterAll
    void deleteFile() {
        testFile.delete();
    }
}