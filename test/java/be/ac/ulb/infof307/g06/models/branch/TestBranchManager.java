package be.ac.ulb.infof307.g06.models.branch;

import be.ac.ulb.infof307.g06.models.database.BranchDB;
import be.ac.ulb.infof307.g06.models.database.CommitDB;
import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.models.database.UserDB;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.branch.BranchManager;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBranchManager {
    private ProjectDB projectDB;
    private UserDB userDB;
    private BranchDB branchDB;
    private CommitDB commitDB;
    private String branchName;
    private int projectID;
    private int[] branches;
    private BranchManager branchManager;
    File metadataFile;
    private int commitId;
    private String content;
    private int userID;

    @BeforeAll
    void setup() throws DatabaseException, IOException, VersioningException, InvalidDriverException {
        projectDB = new ProjectDB();
        userDB = new UserDB();
        branchDB = new BranchDB();
        commitDB = new CommitDB();
        branchManager = new BranchManager();
        branchName ="testBranch";
        userID = userDB.createUser("frr","desours","frr","jk@ulb.be","123");
        projectID = projectDB.insertProject(userID,"ProjectTest",new File("").getAbsolutePath(),"13545634da1");
        branches = new int[]{branchManager.newBranch(projectID, "test1"),
                branchManager.newBranch(projectID, "test2")};
        metadataFile = new File(projectDB.getMetadataPath(projectID,branches[0]));
        content = "\\draw[color=0x000000ff,fill=0x000000ff] (160,222) circle (50pt);\n" +
                "\\draw[color=0xcc3333ff,fill=0xcc3333ff] (307,424) circle (50pt);\n" +
                "\\draw[color=0x000000ff,fill=0x000000ff] (411,247) circle (50pt);\n" +
                "\\draw[color=0x000000ff,->] (201,251) -- (421,272);";
        commitId = commitDB.insertCommit(projectID, branches[0], "testCommit");;
        String commitContent = "commit " + Integer.toString(commitId) +"\n" +
                "\t+\\draw[color=0x000000ff,fill=0x000000ff] (160,222) circle (50pt);\n" +
                "\t+\\draw[color=0xcc3333ff,fill=0xcc3333ff] (307,424) circle (50pt);\n" +
                "\t+\\draw[color=0x000000ff,fill=0x000000ff] (411,247) circle (50pt);\n" +
                "\t+\\draw[color=0x000000ff,->] (201,251) -- (421,272);\n";
        FileWriter fileWriter = new FileWriter(metadataFile);
        fileWriter.write(commitContent);
        fileWriter.close();
    }

    @AfterAll
    void tearDown() throws DatabaseException {
        commitDB.deleteCommit(projectID,branches[0],commitId);
        branchDB.deleteBranch(projectID, branches[0]);
        branchDB.deleteBranch(projectID, branches[1]);
        projectDB.deleteProject(projectID);
        userDB.deleteUserWithID(userID);
        metadataFile.delete();
    }

    @Test
    void isThere2Branches() throws DataAccessException {
        assertEquals(2,branchManager.getBranchesFromProject(projectID).size());
    }
    @Test
    void isFirstBranchNameByGetBranchesFromProjectTest1() throws DataAccessException {
        assertEquals("test1",branchManager.getBranchesFromProject(projectID).get(0).getBranchName());
    }
    @Test
    void isSecondBranchNameByGetBranchesFromProjectTest2() throws DataAccessException {
        assertEquals("test2",branchManager.getBranchesFromProject(projectID).get(1).getBranchName());
    }

    @Test
    void isTextBuildFromCommitIdEqualsToContent() throws ValueNotFoundException, VersioningException {
        assertEquals(content,branchManager.getTextFromBranch(projectID,"test1"));
    }

    @Test
    void isListOfMetadataPathsEqualsTo2() throws VersioningException {
        assertEquals(2,branchManager.getProjectBranchesMetaData(projectID).size());
    }
    @Test
    void isFirsMetadataNameEqualsToDotproject1_branch0() throws VersioningException {
        String expected = ".project" + Integer.toString(projectID) + "_branch0";
        assertEquals(expected,branchManager.getProjectBranchesMetaData(projectID).get(0));
    }

    @Test
    void isSecondMetadataNameEqualsToDotproject1_branch1() throws VersioningException {
        String expected = ".project" + Integer.toString(projectID) + "_branch1";
        assertEquals(expected,branchManager.getProjectBranchesMetaData(projectID).get(1));
    }
    @Test
    void doesFileNameEqualsToDotproject1_branch0() throws VersioningException {
        String expected = ".project" + Integer.toString(projectID) + "_branch0";
        assertEquals(expected,branchManager.metadataFileName(projectID,branches[0]));
    }

    @Test
    void isMetadataFileCreated() throws IOException, VersioningException, DatabaseException, InvalidDriverException {
        String metadataPath = new File (projectDB.getProjectPath(projectID)).getParent() + File.separator + branchManager.metadataFileName(projectID, branches[1]);
        int branchID = branchDB.insertBranch(projectID, branchName);
        branchManager.createMetadataFile(projectID,branchName);
        File metadata2 = new File(metadataPath);
        assertTrue(metadata2.exists());
        metadata2.delete();
        branchDB.deleteBranch(projectID, branchID);
    }

    @Test
    void  doesGetBranchReturnTheRightIdForTheFirstBranch() throws DataAccessException, DatabaseException, IOException, InvalidDriverException {
        assertEquals(branches[0],branchManager.getBranch(projectID,branches[0]).getBranchID());
    }

    @Test
    void doesGetBranchReturnTheRightIdForTheSecondBranch() throws DataAccessException, DatabaseException, IOException, InvalidDriverException {
        assertEquals(branches[1],branchManager.getBranch(projectID,branches[1]).getBranchID());
    }
    @Test
    void  doesGetBranchReturnTheRightNameForTheFirstBranch() throws DataAccessException, DatabaseException, IOException, InvalidDriverException {
        assertEquals("test1",branchManager.getBranch(projectID,branches[0]).getBranchName());
    }

    @Test
    void doesGetBranchReturnTheRightNameForTheSecondBranch() throws DataAccessException, DatabaseException, IOException, InvalidDriverException {
        assertEquals("test2",branchManager.getBranch(projectID,branches[1]).getBranchName());
    }

}
