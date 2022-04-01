package be.ac.ulb.infof307.g06.models.branch;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.utils.FileUtils;
import be.ac.ulb.infof307.g06.models.VersionTextBuilder;
import be.ac.ulb.infof307.g06.models.branch.MergeModel;
import be.ac.ulb.infof307.g06.models.commit.Commit;
import be.ac.ulb.infof307.g06.models.metadata.MetaDataReader;
import be.ac.ulb.infof307.g06.models.metadata.MetaDataWriter;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestMergeModel extends MergeModel {
    private File masterFile;
    private File masterMetaData;
    private MetaDataReader masterMetaDataReader;
    private String masterPathMetaData = "testMaster.metaData";
    private String masterPathFile = "testMaster.file";

    @BeforeEach
    void setup() {
        Project master = new Project();
        master.setPathToMetaData(masterPathMetaData);
        master.setFilePath(masterPathFile);
        this.setMaster(master);
        this.masterFile = new File(masterPathFile);
        this.masterMetaData = new File(masterPathMetaData);
        masterMetaDataReader = new MetaDataReader(masterPathMetaData);
        this.setMasterMetaData(masterPathMetaData); // parent class attribute

        Commit commit = new Commit();
        commit.addNewLine("salut");
        commit.addNewLine("à tous");
        Commit commit2 = new Commit();
        commit2.addNewLine("Ash");
        commit2.addRemovedLine("à tous");

        try {
            MetaDataWriter metaDataWriter = new MetaDataWriter(masterPathMetaData);
            metaDataWriter.writeCommit(commit.toString());
            metaDataWriter.writeCommit(commit2.toString());
        }
        catch(IOException e) {}
    }

    @Test
    void verifyPresenceInTextValidWhenNotInText() {
        String text = "Un jour je serai le meilleur dresseur";
        ArrayList<String> lineToCheck = new ArrayList(Arrays.asList("dresseur","jour","sans répit"));
        ArrayList<String> expected = new ArrayList(Arrays.asList("sans répit"));
        assertEquals(expected, this.verifyPresenceInText(text,lineToCheck,false));
    }

    @Test
    void verifyPresenceInTextValidWhenInText() {
        String text = "Un jour je serai le meilleur dresseur";
        ArrayList<String> lineToCheck = new ArrayList(Arrays.asList("dresseur","jour","sans répit"));
        ArrayList<String> expected = new ArrayList(Arrays.asList("dresseur","jour"));
        assertEquals(expected, this.verifyPresenceInText(text,lineToCheck,true));
    }

    @Test
    void updateMasterMetaDataIsCorrectlyUpdatedWhenNoAddedLines() {
        ArrayList<String> toRemove = new ArrayList(Arrays.asList("mama","just killed a man"));
        Commit commit = new Commit();
        commit.addRemovedLines(toRemove);

        try {
            this.updateMasterMetaData(new ArrayList<>(), toRemove);
            String actual = masterMetaDataReader.getCommitFromID(masterMetaDataReader.getLastCommitID()).getCommitContent();
            assertEquals(commit.toString(masterMetaDataReader.getLastCommitID()),actual);
        }
        catch (IOException e) {}
    }

    @Test
    void updateMasterMetaDataIsCorrectlyUpdatedWhenOnlyAddedLines() {
        ArrayList<String> toAdd = new ArrayList(Arrays.asList("je me battrai","sans répit"));
        Commit commit = new Commit();
        commit.addNewLines(toAdd);

        try {
            this.updateMasterMetaData(toAdd, new ArrayList<>());
            String actual = masterMetaDataReader.getCommitFromID(masterMetaDataReader.getLastCommitID()).getCommitContent();
            assertEquals(commit.toString(masterMetaDataReader.getLastCommitID()),actual);
        }
        catch (IOException e) {}
    }

    @Test
    void updateMasterMetaDataIsCorrectlyUpdatedWithAddedAndRemovedLines() {
        ArrayList<String> toAdd = new ArrayList(Arrays.asList("radio","i play pokemon go everyday"));
        ArrayList<String> toRemove = new ArrayList(Arrays.asList("radio star","not today"));
        Commit commit = new Commit();
        commit.addNewLines(toAdd);
        commit.addRemovedLines(toRemove);

        try {
            this.updateMasterMetaData(toAdd, toRemove);
            String actual = masterMetaDataReader.getCommitFromID(masterMetaDataReader.getLastCommitID()).getCommitContent();
            assertEquals(commit.toString(masterMetaDataReader.getLastCommitID()),actual);
        }
        catch (IOException e) {}
    }

    @Test
    void updateMasterFileIsCorrectlyUpdatingMasterFile() throws IOException, DatabaseConnectionException {
        VersionTextBuilder versionTextBuilder = new VersionTextBuilder(masterPathMetaData);
        String expected = versionTextBuilder.getTextFrom(masterMetaDataReader.getLastCommitID());
        this.updateMasterFile();
        assertEquals(expected, FileUtils.fileToString(masterPathFile));
    }

    @AfterEach
    void CleanUp() {
        this.masterFile.delete();
        this.masterMetaData.delete();
    }
}