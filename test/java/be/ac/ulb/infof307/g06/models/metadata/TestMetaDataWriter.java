package be.ac.ulb.infof307.g06.models.metadata;

import be.ac.ulb.infof307.g06.utils.FileUtils;
import be.ac.ulb.infof307.g06.models.metadata.MetaDataWriter;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestMetaDataWriter extends MetaDataWriter {
    private MetaDataWriter metaDataWriter;
    private String commit;
    private File metaData;
    private String path;

    public TestMetaDataWriter() {
        super();
    }

    @BeforeEach
    void setUp() {
        path = ".MetaDataTest";
        metaData = new File(path);
        try {metaData.createNewFile();}
        catch (IOException e) {
            fail("Couldn't create the Testfile");
        }
        metaDataWriter = new MetaDataWriter(path);
        commit = "commit 324 \n" +
                "\t+\\Draw (100, 100) circle (100pt);\n" +
                "\t-\\Fill[color=blue] (100, 100) rectangle (400, 400);";
    }

    @AfterEach
    void CleanUp() {
        if (metaData.exists()) {
            if (!metaData.delete()) {
                System.err.println("CleanUp failed");
            }
        }
    }

    @Test
    void doesCommitEqualsTheFileContent() {
        String readCommit = "";
        try {
            metaDataWriter.writeCommit(commit);
            readCommit = FileUtils.fileToString(path);
        }
        catch (IOException e) {
            fail("WriteCommit test failed with an IOexception");
        }
        assertEquals(commit, readCommit);
    }

    @Test
    void verifyReturnTrueIfCommitInCorrectFormat() {
        assertTrue(verifyCommit(commit));
    }

    @Test
    void verifyReturnFalseIfTabMissingInCommit() {
        String falseCommit = "commit 324 \n" +
                "\t+\\Draw (100, 100) circle (100pt);\n" +
                "-\\Fill[color=blue] (100, 100) rectangle (400, 400);";
        assertFalse(verifyCommit(falseCommit));
    }

    @Test
    void verifyReturnFalseIfCommitNotTheFirstWord() {
        String falseCommit = "comit 324 \n" +
                "\t+\\Draw (100, 100) circle (100pt);\n" +
                "\t-\\Fill[color=blue] (100, 100) rectangle (400, 400);";
        assertFalse(verifyCommit(falseCommit));
    }

    @Test
    void verifyReturnFalseIfIdCommitWrong() {
        String falseCommit = "commit 32a4 \n" +
                "\t+\\Draw (100, 100) circle (100pt);\n" +
                "\t-\\Fill[color=blue] (100, 100) rectangle (400, 400);";
        assertFalse(verifyCommit(falseCommit));
    }

    @Test
    void verifyReturnFalseIfNoSpaceBetweenCommitAndId() {
        String falseCommit = "commit324 \n" +
                "\t+\\Draw (100, 100) circle (100pt);\n" +
                "\t-\\Fill[color=blue] (100, 100) rectangle (400, 400);";
        assertFalse(verifyCommit(falseCommit));
    }

    @Test
    void verifyReturnFalseIfAddThenRemoveThenAdd() {
        String falseCommit = "commit 324 \n" +
                "\t+\\Draw (100, 100) circle (100pt);\n" +
                "\t-\\Fill[color=blue] (100, 100) rectangle (400, 400);\n" +
                "\t+\\Draw (100, 100) circle (100pt);";
        assertFalse(verifyCommit(falseCommit));
    }

    @Test
    void verifyReturnTrueIfBranchInCorrectFormat() {
        String branch = "branched machin from bidule;";
        assertTrue(verifyStatement(branch));
    }

    @Test
    void verifyReturnFalseIfFirstWordNotBranchedForBranch() {
        String branch = "branche machin from bidule;";
        assertFalse(verifyStatement(branch));
    }

    @Test
    void verifyReturnFalseIfLastCharNotSemicolon() {
        String branch = "branched machin from bidule";
        assertFalse(verifyStatement(branch));
    }

    @Test
    void verifyReturnFalseIfNotFromForBranch() {
        String branch = "branched machin fro bidule;";
        assertFalse(verifyStatement(branch));
    }

    @Test
    void verifyReturnFalseIfWrongLengthOfStringArray() {
        String branch = "branchedmachinfrombidule;";
        assertFalse(verifyStatement(branch));
    }

    @Test
    void verifyReturnTrueIfMergeInCorrectFormat() {
        String branch = "merged machin to bidule;";
        assertTrue(verifyStatement(branch));
    }

    @Test
    void verifyReturnTrueIfRevertInCorrectFormat() {
        String branch = "reverted machin to 453;";
        assertTrue(verifyStatement(branch));
    }

    @Test
    void verifyReturnFalseIfNotNumericInRevert() {
        String branch = "reverted machin to 45a3;";
        assertFalse(verifyStatement(branch));
    }

    @Test
    void verifyThatFileIsCorrectlyDeleted() {
        metaDataWriter.eraseMetaDataFile();
        File file = metaDataWriter.getMetaData();
        assertFalse(file.exists());
    }
}
