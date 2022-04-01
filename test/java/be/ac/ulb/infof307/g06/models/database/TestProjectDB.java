package be.ac.ulb.infof307.g06.models.database;

import be.ac.ulb.infof307.g06.models.database.*;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.exceptions.ValueNotFoundException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestProjectDB {
    UserDB userDB;
    ProjectDB projectDB;
    BranchDB branchDB;
    CommitDB commitDB;

    String testFileName = "testFile";
    String testFileName2 = "testFile2";
    String newTestFileName = "newTestFile";
    String pathToFile = "/test/testFile";
    String pathToFile2 = "/test/testFile2";
    String newPathToFile = "/test/testFile";
    String testMessage = "message de commit";
    String testBranchName = "testBranch";

    int userID;
    String username = "test";
    String name = "testName";
    String givenName = "testGivenName";
    String email = "testEmail";
    String password = "testPassword";
    String checksum = "426a6214d30516f30c74e9a881cec101";

    User testUser;

    @BeforeAll
    void createUser() throws DatabaseException {
        userDB = new UserDB();
        projectDB = new ProjectDB();
        branchDB = new BranchDB();
        commitDB = new CommitDB();
        userID =  userDB.createUser(username, name, givenName, email, password);
        testUser = new User(username, givenName, name, email, password);
        testUser.setId(userID);
    }

    @AfterAll
    void deleteUser() throws DatabaseException  {
        userDB.deleteUserWithID(userID);
    }

    @Test
    void getProjectDataAfterInsertProjectReturnsRightData() throws DatabaseException, InvalidDriverException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        String[] data = projectDB.getProjectData(projectID);
        assertEquals(testFileName, data[0]);
        assertEquals(pathToFile, data[1]);
        projectDB.deleteProject(projectID);
    }

    @Test
    void getProjectsOfUserReturnsRightProjects() throws DatabaseException, ValueNotFoundException, InvalidDriverException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int projectID2 = projectDB.insertProject(userID, testFileName2, pathToFile2, checksum);
        List<Integer> listOfProjectID = projectDB.getProjectsOfUser(testUser.getId());
        assertEquals(listOfProjectID.get(0), projectID);
        assertEquals(listOfProjectID.get(1), projectID2);
        projectDB.deleteProject(projectID);
        projectDB.deleteProject(projectID2);
    }

    @Test
    void projectIsCorrectlyRenamed() throws DatabaseException, InvalidDriverException {
        int projectID = projectDB.insertProject(testUser.getId(), testFileName, pathToFile, checksum);
        projectDB.renameProject(projectID, newTestFileName);
        String[] data = projectDB.getProjectData(projectID);
        assertEquals(newTestFileName, data[0]);
        assertEquals(pathToFile, data[1]);
        projectDB.deleteProject(projectID);
    }

    @Test
    void pathIsCorrectlyUpdated() throws DatabaseException, InvalidDriverException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        projectDB.updatePath(projectID, newPathToFile);
        String[] data;
        data = projectDB.getProjectData(projectID);
        assertEquals(testFileName, data[0]);
        assertEquals(newPathToFile, data[1]);
        projectDB.deleteProject(projectID);
    }

    @Test
    void projectIsCorrectlyDeleted() throws DatabaseException, InvalidDriverException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branchID = branchDB.insertBranch(projectID, testBranchName);
        int commitID = commitDB.insertCommit(projectID,branchID, testMessage);
        projectDB.deleteProject(projectID);
        String[] data = projectDB.getProjectData(projectID);
        String[] commitData = commitDB.getCommitData(projectID,branchID,commitID);
        assertNull(data[0]);
        assertEquals(-1, branchDB.getBranchData(projectID, branchID).getProjectID());
        assertNull(commitData[0]);
    }

    @Test
    void checkProjectExistWithAnExistingProjectReturnsTrue() throws DatabaseException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        assertTrue(projectDB.checkProjectExist(pathToFile));
        assertFalse(projectDB.checkProjectExist("salut"));
        projectDB.deleteProject(projectID);
    }

    @Test
    void getMetadataPathGeneratesRightPath() throws DatabaseException, InvalidDriverException {
        int projectID = projectDB.insertProject(userID, testFileName, pathToFile, checksum);
        int branchID = branchDB.insertBranch(projectID,testBranchName);
        String[] projectData = projectDB.getProjectData(projectID);
        String branchPath = Paths.get(projectData[1]).getParent().toString();
        String actualMetadataPath = projectDB.getMetadataPath(projectID, branchID);
        String expectedMetadataPath = branchPath + File.separator + ".project" + projectID + "_branch" + branchID;
        assertEquals(expectedMetadataPath, actualMetadataPath);
        projectDB.deleteProject(projectID);
    }

    @Test
    void getProjectChecksumReturnsExpectedChecksum() throws DatabaseException, InvalidDriverException {
        int projectID = projectDB.insertProject(testUser.getId(), testFileName, pathToFile, checksum);
        String actualChecksum = projectDB.getProjectChecksum(projectID);
        assertEquals(checksum,actualChecksum);
        projectDB.deleteProject(projectID);
    }

    @Test
    void newChecksumIsTheCorrectOne() throws DatabaseException, InvalidDriverException {
        String newchecksum = "09df3bc50707273be56ac901e534230a";
        int projectID = projectDB.insertProject(testUser.getId(), testFileName, pathToFile, checksum);
        projectDB.updateChecksum(projectID, newchecksum);
        String updatedchecksum = projectDB.getProjectChecksum(projectID);
        assertEquals(newchecksum, updatedchecksum);
        projectDB.deleteProject(projectID);
    }
}