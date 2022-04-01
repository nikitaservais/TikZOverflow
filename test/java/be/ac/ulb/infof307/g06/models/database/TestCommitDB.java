package be.ac.ulb.infof307.g06.models.database;

import be.ac.ulb.infof307.g06.models.database.*;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.exceptions.ValueNotFoundException;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestCommitDB {
    UserDB userDB;
    ProjectDB projectDB;
    BranchDB branchDB;
    CommitDB commitDB;

    String testFileName = "testFile";
    String pathToFile = "/test/testFile";
    String testMessage = "message de commit";
    String testBranchName = "testBranch";

    int userID;
    String username = "test";
    String name = "testName";
    String givenName = "testGivenName";
    String email = "testEmail";
    String password = "testPassword";

    String checksum = "426a6214d30516f30c74e9a881cec101";


    @BeforeAll
    void createUser() throws DatabaseException {
        userDB = new UserDB();
        projectDB = new ProjectDB();
        branchDB = new BranchDB();
        commitDB = new CommitDB();
        userID =  userDB.createUser(username, name, givenName, email, password);
    }

    @AfterAll
    void deleteUser() throws DatabaseException  {
        userDB.deleteUserWithID(userID);
    }

    @Test
    void getCommitDataAfterInsertCommitReturnsRightData() throws DatabaseException, InvalidDriverException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branchID = branchDB.insertBranch(projectID,testBranchName);
        int commitID = commitDB.insertCommit(projectID,branchID,testMessage);
        String[] data = commitDB.getCommitData(projectID,branchID,commitID);
        assertEquals(Integer.toString(projectID), data[0]);
        assertEquals(Integer.toString(branchID), data[1]);
        assertEquals(testMessage, data[3]);
        projectDB.deleteProject(projectID);
    }

    @Test
    void getLastCommitIDAfterInsertReturnsRightID() throws DatabaseException, ValueNotFoundException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branchID = branchDB.insertBranch(projectID,testBranchName);
        int commitID = commitDB.insertCommit(projectID,branchID,testMessage);
        int lastCommitID = commitDB.getLastCommitID(projectID,branchID);
        assertEquals(commitID,lastCommitID);
        projectDB.deleteProject(projectID);
    }

    @Test
    void getLastCommitIDWithNoInsertThrowsValueNotFoundException() throws DatabaseException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branchID = branchDB.insertBranch(projectID,testBranchName);
        try {
            commitDB.getLastCommitID(projectID,branchID);
            fail("Method didn't throw ValueNotFoundException.");
        } catch (ValueNotFoundException e) {
            projectDB.deleteProject(projectID);
        }
    }

    @Test
    void getCommitsReturnsRightCommits() throws DatabaseException, ValueNotFoundException, InvalidDriverException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branchID = branchDB.insertBranch(projectID,testBranchName);
        int commit1ID = commitDB.insertCommit(projectID,branchID,testMessage);
        int commit2ID = commitDB.insertCommit(projectID,branchID,testMessage);
        List<Integer> listOfCommitID = commitDB.getCommits(projectID,branchID);
        assertEquals(listOfCommitID.get(0),commit1ID);
        assertEquals(listOfCommitID.get(1),commit2ID);
        projectDB.deleteProject(projectID);
    }

    @Test
    void deleteCommitMethodDeletesCommitInDatabase() throws DatabaseException, InvalidDriverException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branchID = branchDB.insertBranch(projectID, testBranchName);
        int commitID = commitDB.insertCommit(projectID,branchID, testMessage);
        commitDB.deleteCommit(projectID, branchID, commitID);
        String[] commitData = commitDB.getCommitData(projectID,branchID,commitID);
        assertNull(commitData[0]);
        projectDB.deleteProject(projectID);
    }
}