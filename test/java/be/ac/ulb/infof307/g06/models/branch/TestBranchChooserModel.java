package be.ac.ulb.infof307.g06.models.branch;

import be.ac.ulb.infof307.g06.models.database.BranchDB;
import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.models.database.UserDB;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.models.branch.BranchChooserModel;
import be.ac.ulb.infof307.g06.models.branch.BranchManager;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestBranchChooserModel {
    private BranchChooserModel branchChooserModel;
    private ProjectDB projectDB;
    private UserDB userDB;
    private BranchDB branchDB;
    private int projectID;
    private int branchID;
    private String branchName;
    private int userID;
    File metadataFile;

    @BeforeAll
    void setUp() throws DatabaseException, VersioningException, InvalidDriverException {
        projectDB = new ProjectDB();
        userDB = new UserDB();
        branchDB = new BranchDB();
        branchName ="testBranch";
        userID = userDB.createUser("frr","desours","frr","jk@ulb.be","123");
        projectID = projectDB.insertProject(userID,"ProjectTest",new File("").getAbsolutePath(),"13545634da1");
        branchID = new BranchManager().newBranch(projectID, branchName);
        metadataFile = new File(projectDB.getMetadataPath(projectID,branchID));
        branchChooserModel = new BranchChooserModel(projectID);
    }

    @AfterAll
    void tearDown() throws DatabaseException {
        branchDB.deleteBranch(projectID, branchID);
        projectDB.deleteProject(projectID);
        userDB.deleteUserWithID(userID);
        metadataFile.delete();
    }

    @Test
    void isProjectNameProjectTest() throws DataAccessException, DatabaseConnectionException {
        Project test  = branchChooserModel.getBranch(branchName);
        assertEquals("ProjectTest",test.getName());
    }

    @Test
    void hasProjectCurrentBranchHasTheRightId() throws DataAccessException, DatabaseConnectionException {
        Project test  = branchChooserModel.getBranch(branchName);
        assertEquals(branchID,test.getCurrentBranch());
    }

    @Test
    void hasProjectTheCorrectPathToMetadata() throws DataAccessException, DatabaseConnectionException {
        Project test  = branchChooserModel.getBranch(branchName);
        assertEquals(metadataFile.getAbsolutePath(),test.getPathToMetaData());
    }

}