package be.ac.ulb.infof307.g06.models.database;

import be.ac.ulb.infof307.g06.models.database.BranchDB;
import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.models.database.UserDB;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.exceptions.ValueNotFoundException;
import be.ac.ulb.infof307.g06.models.branch.Branch;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestBranchDB {
    UserDB userDB;
    ProjectDB projectDB;
    BranchDB branchDB;

    String testFileName = "testFile";
    String pathToFile = "/test/testFile";
    String testBranchName = "testBranch";
    String testBranchName2 = "testBranch2";

    int userID;
    String username = "test";
    String name = "testName";
    String givenName = "testGivenName";
    String email = "testEmail";
    String password = "testPassword";
    String checksum = "426a6214d30516f30c74e9a881cec101";

    @BeforeAll
    void createUser() throws DatabaseException  {
        userDB = new UserDB();
        projectDB = new ProjectDB();
        branchDB = new BranchDB();
        userID =  userDB.createUser(username, name, givenName, email, password);

    }

    @AfterAll
    void deleteUser() throws DatabaseException  {
        userDB.deleteUserWithID(userID);
    }

    @Test
    void getBranchDataAfterInsertBranchReturnsRightData() throws DatabaseException, InvalidDriverException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branchID = branchDB.insertBranch(projectID,testBranchName);
        Branch data = branchDB.getBranchData(projectID,branchID);
        assertEquals(projectID, data.getProjectID());
        assertEquals(testBranchName, data.getBranchName());
        assertTrue(data.getStatus());
        projectDB.deleteProject(projectID);
    }

    @Test
    void getLastBranchIDAfterInsertReturnsRightID() throws DatabaseException, ValueNotFoundException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branchID = branchDB.insertBranch(projectID,testBranchName);
        int lastBranchID = branchDB.getLastBranchID(projectID);
        assertEquals(branchID,lastBranchID);
        projectDB.deleteProject(projectID);
    }

    @Test
    void getLastBranchIDWithNoInsertThrowsValueNotFoundException() throws DatabaseException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        try {
            int lastBranchID = branchDB.getLastBranchID(projectID);
            fail("Method didn't throw ValueNotFoundException.");
        } catch (ValueNotFoundException e) {
            projectDB.deleteProject(projectID);
        }
    }

    @Test
    void branchStatusIsCorrectlyUpdated() throws DatabaseException, InvalidDriverException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branchID = branchDB.insertBranch(projectID,testBranchName);
        branchDB.updateBranchStatus(projectID,branchID,0);
        Branch data = branchDB.getBranchData(projectID,branchID);
        assertFalse(data.getStatus());
        projectDB.deleteProject(projectID);
    }

    @Test
    void getBranchesReturnsRightBranches() throws DatabaseException, ValueNotFoundException, InvalidDriverException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branch1ID = branchDB.insertBranch(projectID,testBranchName);
        int branch2ID = branchDB.insertBranch(projectID,testBranchName2);
        List<Integer> listOfBranchID = branchDB.getBranches(projectID);
        assertEquals(listOfBranchID.get(0),branch1ID);
        assertEquals(listOfBranchID.get(1),branch2ID);
        projectDB.deleteProject(projectID);
    }

    @Test
    void getActiveBranchesReturnsRightBranches() throws DatabaseException, ValueNotFoundException, InvalidDriverException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branch1ID = branchDB.insertBranch(projectID,testBranchName);
        int branch2ID = branchDB.insertBranch(projectID,testBranchName2);
        branchDB.updateBranchStatus(projectID,branch1ID,0);
        List<Integer> listOfActiveBranchID = branchDB.getActiveBranches(projectID);
        assertEquals(listOfActiveBranchID.size(), 1);
        assertEquals(listOfActiveBranchID.get(0),branch2ID);
        projectDB.deleteProject(projectID);
    }

    @Test
    void getBranchFromNameReturnsTheRightID() throws DatabaseException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branchID = branchDB.insertBranch(projectID,testBranchName);
        int resultBranchId = branchDB.getBranchID(testBranchName, projectID);
        assertEquals(branchID, resultBranchId);
        projectDB.deleteProject(projectID);
    }

}