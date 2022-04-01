package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.models.database.UserDB;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.models.project.ManageProjectModel;
import be.ac.ulb.infof307.g06.models.project.ProjectManager;
import be.ac.ulb.infof307.g06.utils.FileUtils;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class that make the unitary tests.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestManageProjectModel {
    private ManageProjectModel model;
    private UserDB userDB;
    private ProjectDB projectDB;
    private String testFileContent;
    private User user;
    private User testUser;
    private int userID;

    @BeforeAll
    void setup() throws DatabaseException {
        userDB = new UserDB();
        projectDB = new ProjectDB();
        userID =  userDB.createUser("Test", "test", "test", "test", "test");
        testFileContent = "The file is not empty.";
        user = new User("username","firstname", "lastname", "email@email.com","12345678");
        user.setId(1);
        model = new ManageProjectModel(user);
    }

    @AfterAll
    void tearDown() throws DatabaseException {userDB.deleteUserWithID(userID);}

    /**
     * Method that test the updateFileInfo method by adding manually a fileInfo in a list of the current fileInfo
     * of the user Test. Then compare the previous list to à list that is made out of the dataaccess.
     */
    @Test
    void userFileInfoAreCorrectlyUpdated() throws ValueNotFoundException, DataAccessException, DatabaseException, IOException, InvalidDriverException {
        model.updateUserFileInfo();
        List<Project> expectedUserFiles = model.getUserProjects();
        File testFile = new File("test" + File.separator + "testFile");
        testFile.createNewFile();
        int testFileID = projectDB.insertProject(user.getId(), testFile.getName(), testFile.getAbsolutePath(), "09df3bc50707273be56ac901e534230a");
        Project testProject = new Project( "testFile", testUser, "", testFile.getAbsolutePath(), testFileID);
        expectedUserFiles.add(testProject);

        model.updateUserFileInfo();
        List<Project> userFiles = model.getUserProjects();
        Assertions.assertIterableEquals(expectedUserFiles, userFiles);

        //Delete files of Test user.
        List<Integer> idFilesOfUser = projectDB.getProjectsOfUser(this.user.getId());
        for (int i = 0; i < idFilesOfUser.size(); i++) {
            projectDB.deleteProject(idFilesOfUser.get(i));
        }
        testFile.delete();
        projectDB.deleteProject(testFileID);
    }

    @Test
    void copiedFileHaveRightContentAfterCopy() throws DataAccessException, VersioningException, IOException, DatabaseException, ValueNotFoundException, InvalidDriverException {
        File testFile = new File("test" + File.separator + "testFile");
        testFile.createNewFile();
        FileWriter fileWriter = new FileWriter(testFile, false);
        fileWriter.write(testFileContent);
        fileWriter.close();
        Project testProject = new Project( "testFile", testUser, "", testFile.getAbsolutePath(), 0);

        model.copyProject(testProject);
        File testFileCopy = new File("test" + File.separator + "testFile_copy");
        assertTrue(testFileCopy.exists());
        String content = FileUtils.fileToString(testFileCopy.getAbsolutePath());
        assertEquals(testFileContent, content, "copyProject method didn't executed properly");

        //Delete files of Test user.
        List<Integer> idFilesOfUser = projectDB.getProjectsOfUser(user.getId());
        for (int i = 0; i < idFilesOfUser.size(); i++) {
            projectDB.deleteProject(idFilesOfUser.get(i));
        }
        testFileCopy.delete();
        testFile.delete();
    }

    @Test
    void originalFileHaveRightContentAfterCopy() throws DataAccessException, VersioningException, IOException, DatabaseException, ValueNotFoundException, InvalidDriverException {
        File testFile = new File("test" + File.separator + "testFile");
        testFile.createNewFile();
        FileWriter fileWriter = new FileWriter(testFile, false);
        fileWriter.write(testFileContent);
        fileWriter.close();
        Project testProject = new Project( "testFile", testUser, "", testFile.getAbsolutePath(), 0);

        model.copyProject(testProject);
        File testFileCopy = new File("test" + File.separator + "testFile_copy");
        assertTrue(testFileCopy.exists());
        String content = FileUtils.fileToString(testFile.getAbsolutePath());
        assertEquals(testFileContent, content, "copyProject method didn't executed properly");

        //Delete files of Test user.
        List<Integer> idFilesOfUser = projectDB.getProjectsOfUser(user.getId());
        for (int i = 0; i < idFilesOfUser.size(); i++) {
            projectDB.deleteProject(idFilesOfUser.get(i));
        }
        testFileCopy.delete();
        testFile.delete();
    }


    @Test
    void projectIsCorrectlyDeleted() throws DataAccessException, IOException, DatabaseException, VersioningException, CommitException {
        File testFile = new File("test" + File.separator + "testFile");
        testFile.createNewFile();
        Project testProject =  new ProjectManager().createProject(user, testFile.getName(), testFile.getAbsolutePath());
        model.deleteProject(testProject);
        assertTrue(!(testFile.exists() && projectDB.checkProjectExist(testFile.getAbsolutePath())),"deleteProject method didn't executed properly");
    }

    @Test
    void directoryIsCorrectlyUpdated() throws DataAccessException, IOException, DatabaseException, VersioningException, InvalidDriverException, CommitException {
        File changeDirectory = new File("test" + File.separator + "changeTest");
        File testFile = new File("test" + File.separator + "testFile");
        changeDirectory.mkdir();
        testFile.createNewFile();
        FileWriter fileWriter = new FileWriter(testFile, false);
        fileWriter.write(testFileContent);
        fileWriter.close();

        Project testProject =  new ProjectManager().createProject(user, "testFile", testFile.getAbsolutePath());
        model.changeDirectory(testProject,"test" + File.separator + "changeTest");
        File testFileMoved = new File("test" + File.separator + "changeTest" + File.separator + "testFile");

        assertTrue(testFileMoved.exists());
        String content = FileUtils.fileToString(testFileMoved.getAbsolutePath());
        assertEquals(testFileContent, content, "");
        assertEquals("test" + File.separator + "changeTest" + File.separator + "testFile",projectDB.getProjectData(testProject.getId())[1]);
        testFileMoved.delete();
        testFile.delete();
        changeDirectory.delete();
        File metaDataFile = new File(testProject.getPathToMetaData());
        if (metaDataFile.exists()) metaDataFile.delete();
    }

    @Test
    void projectIsCorrectlyRenamed() throws DatabaseException, IOException, InvalidDriverException {
        String path = File.separator + "test" + File.separator + "out" + File.separator + "manageProjectTest" + File.separator;
        File folders = new File(System.getProperty("user.dir") + path);
        folders.mkdirs();
        File file = new File(System.getProperty("user.dir") + path + "testFile");
        FileWriter writer;
        writer = new FileWriter(file);
        writer.write("\\draw (100, 100) circle (100pt);");
        writer.close();
        int id = projectDB.insertProject(123,"testFile", file.getAbsolutePath(), "09df3bc50707273be56ac901e534230a");
        Project project = new Project("testFile", testUser,"timestamp",file.getAbsolutePath(),id);

        String oldPath = project.getFilePath();
        String oldName = oldPath.substring(oldPath.lastIndexOf(File.separator));
        String newName = "newName";
        String newPath = System.getProperty("user.dir") + path;
        model.renameProject(newName, project);

        String[] data = projectDB.getProjectData(id);

        assertEquals(newPath+newName,data[1]);
        assertEquals(newName,data[0]);
        new File(newPath+newName).delete();
        projectDB.deleteProject(id);
    }

    @Test
    void projectContentIsCorrectlyUpdated() throws IOException {
        File testFile = new File("test"+File.separator+"out"+File.separator+"manageProjectTest"+File.separator+"testFile");
        testFile.createNewFile();
        FileWriter fileWriter = new FileWriter(testFile, false);
        fileWriter.write(testFileContent);
        fileWriter.close();

        Project testProject = new Project( "testFile", testUser, "", testFile.getAbsolutePath(), 0);
        model.editProject(testProject);
        String content = FileUtils.fileToString(testFile.getAbsolutePath());
        assertEquals(testFileContent, content, "editProject method didn't executed properly");
        testFile.delete();
    }

    @Test
    void copiedFileNameIsCorrect() {
        assertEquals("slipdebain_copy.tex", model.copyNameFile("slipdebain.tex"));
        assertEquals("slipdebain_copy", model.copyNameFile("slipdebain"));
        assertNotEquals("slipdebain.tex_copy", model.copyNameFile("slipdebain.tex")); // encountered error during development
    }
}
