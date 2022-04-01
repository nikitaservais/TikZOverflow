package be.ac.ulb.infof307.g06.models.branch;

import be.ac.ulb.infof307.g06.exceptions.VersioningException;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.utils.FileUtils;
import be.ac.ulb.infof307.g06.models.commit.Commit;
import be.ac.ulb.infof307.g06.models.metadata.MetaDataWriter;
import be.ac.ulb.infof307.g06.models.branch.RevertModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestRevertModel extends RevertModel {
    private Commit commit;
    private Commit commit2;
    private String metadataFilePath = "metadata.test";
    private String projectFilePath = "Project.test";

    @BeforeEach
    void setUp() throws IOException {
        Project master = new Project();
        master.setPathToMetaData(metadataFilePath);
        master.setFilePath(projectFilePath);
        this.setFileInfo(master);
        commit = new Commit();
        commit.addNewLine("un jour je serai");
        commit.addNewLine("le meilleur dresseur.");
        commit.addNewLine("je me battrai");
        commit.addNewLine("sans répit");
        commit2 = new Commit();
        commit2.addRemovedLine("le meilleur dresseur.");
        commit2.addRemovedLine("je me battrai");
        commit2.addNewLine("le meilleur programmeur.");
        commit2.addNewLine("je coderai");
        Commit commit3 = new Commit();
        commit3.addRemovedLine("je coderai");
        commit3.addNewLine("je programmerai");
        MetaDataWriter metaDataWriter = new MetaDataWriter(this.metadataFilePath);

        metaDataWriter.writeCommit(commit.toString(1));
        metaDataWriter.writeCommit(commit2.toString(2));
        metaDataWriter.writeCommit(commit3.toString(3));

    }

    @Test
    void isRevertTIKZCodeCorrectlyRevertedToCommit1() throws IOException, VersioningException {
        String actual = "";
        this.revertTIKZCode(1);
        actual = FileUtils.fileToString(this.projectFilePath);
        String expected = "un jour je serai\nle meilleur dresseur.\nje me battrai\nsans répit";
        assertEquals(expected, actual);

    }

    @Test
    void isMetadataCorrectlyRevertedToCommit1() throws IOException {
        String actual = "";
        this.updateMetadataReverted(1);
        actual = FileUtils.fileToString(this.metadataFilePath);
    String expected = commit.toString(1);
        assertEquals(expected, actual);
    }

    @Test
    void isRevertTIKZCodeCorrectlyRevertedToCommit2() throws IOException, VersioningException {
        String actual = "";
        this.revertTIKZCode(2);
        actual = FileUtils.fileToString(this.projectFilePath);
        String expected = "un jour je serai\nsans répit\nle meilleur programmeur.\nje coderai";
        assertEquals(expected, actual);

    }

    @Test
    void isMetadataCorrectlyRevertedToCommit2() throws IOException {
        String actual = "";
        this.updateMetadataReverted(2);
        actual = FileUtils.fileToString(this.metadataFilePath);
        String expected = commit.toString(1) + "\n" + commit2.toString(2);
        assertEquals(expected, actual);
    }

    @AfterEach
    void CleanUp() {
        new File(metadataFilePath).delete();
        new File(projectFilePath).delete();
    }
}