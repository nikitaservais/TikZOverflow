package be.ac.ulb.infof307.g06.models.metadata;

import be.ac.ulb.infof307.g06.models.metadata.MetaDataReader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestMetaDataReader {
    private String path;
    File metaData;
    FileWriter metaDataWriter;
    String commit_1;
    String commit_2;
    String commit_3;
    MetaDataReader metaDataReader;

    @BeforeAll
    void setUp() {
        path = ".MetaDataTest";
        metaData = new File(path);
        try {
            metaData.createNewFile();
            metaDataWriter = new FileWriter(path, true);
            commit_1 = "commit 1 \n" +
                    "\t+\\Draw (100, 100) circle (100pt);\n" +
                    "\t-\\Fill[color=blue] (100, 100) rectangle (400, 400);\n";
            metaDataWriter.write(commit_1);
            commit_2 = "commit 2 \n" +
                    "\t+\\Draw (134, 100) circle (80pt);\n" +
                    "\t-\\Fill[color=red] (100, 100) rectangle (400, 400);\n";
            metaDataWriter.write(commit_2);
            commit_3 = "commit 3 \n" +
                    "\t+\\Draw (134, 109) circle (80pt);\n" +
                    "\t-\\Fill[color=green] (100, 100) rectangle (300, 300);\n";
            metaDataWriter.write(commit_3);
            metaDataReader = new MetaDataReader(path);
            metaDataWriter.close();
        } catch (IOException e) {
            fail("Couldn't create the Testfile");
        }
    }

    @AfterAll
    void cleanUp() {
        if (metaData.exists()) {
            if (!metaData.delete()) {
                System.err.println("CleanUp failed");
            }
        }
    }

    @Test
    void getCommitFromIDReturningGoodCommit() {
        try {
            String commitContent = metaDataReader.getCommitFromID(2).getCommitContent();
            assertEquals(commit_2, commitContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getCommitFromIDReturningNothingWhenCommitDoesntExist() {
        try {
            String commitContent = metaDataReader.getCommitFromID(4).getCommitContent();
            assertEquals("", commitContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}