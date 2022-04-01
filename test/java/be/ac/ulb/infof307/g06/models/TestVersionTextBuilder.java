package be.ac.ulb.infof307.g06.models;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.models.commit.Commit;
import be.ac.ulb.infof307.g06.models.metadata.MetaDataWriter;
import be.ac.ulb.infof307.g06.models.VersionTextBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestVersionTextBuilder {
    private VersionTextBuilder versionTextBuilder;
    private String path = "test.MetaDataTest";
    private Commit commit;

    private String message = "commit test";
    private ArrayList<String> newLines;
    private ArrayList<String> removedLines;

    @BeforeAll
    void setup() throws DatabaseConnectionException {
        versionTextBuilder = new VersionTextBuilder(path);
        MetaDataWriter metaDataWriter = new MetaDataWriter(path);

        // creating commits for tests
        newLines = new ArrayList<>(Arrays.asList("salut à tous"));
        removedLines = new ArrayList<>(Arrays.asList(""));
        commit = new Commit(1,1, newLines, removedLines);
        try {
            metaDataWriter.writeCommit(commit.toString(1));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        newLines = new ArrayList<>(Arrays.asList("salut mes amis","comment allez-vous"));
        removedLines = new ArrayList<>(Arrays.asList("salut à tous"));
        commit = new Commit(1,1, newLines, removedLines);
        try {
            metaDataWriter.writeCommit(commit.toString(2));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        newLines = new ArrayList<>(Arrays.asList("moi ça va super, et vous?"));
        removedLines = new ArrayList<>(Arrays.asList("comment allez-vous"));
        commit = new Commit(1,1, newLines, removedLines);
        try {
            metaDataWriter.writeCommit(commit.toString(3));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void correctTextIsBuiltCommit1() {
        String text = "salut à tous";

        try {
            assertEquals(text, versionTextBuilder.getTextFrom(1));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void correctTextIsBuiltCommit2() {
        String text = "salut mes amis\ncomment allez-vous";
        try {
            assertEquals(text, versionTextBuilder.getTextFrom(2));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void correctTextIsBuiltCommit3() {
        String text = "salut mes amis\nmoi ça va super, et vous?";
        try {
            assertEquals(text, versionTextBuilder.getTextFrom(3));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    void CleanUp() {
        new File(path).delete();
    }
}