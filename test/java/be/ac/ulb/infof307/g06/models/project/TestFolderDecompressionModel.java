package be.ac.ulb.infof307.g06.models.project;


import be.ac.ulb.infof307.g06.models.database.UserDB;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.user.UserManager;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.models.project.FolderCompressionModel;
import be.ac.ulb.infof307.g06.models.project.FolderDecompressionModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestFolderDecompressionModel {
    private UserDB userDB;
    private int userID;
    private String pathCompressed;
    private String pathDecompressed;
    private File file;
    private File tarFile;

    /**
     * Here, we set up the dataaccess and the test's user
     */
    @BeforeAll
    void setUp() throws DatabaseConnectionException {
        userDB = new UserDB();
        try {
            userID = new UserManager().signUp("slipdebain","firstname", "lastname", "test@jambom.com","12345678");
        } catch (SignUpException | DatabaseConnectionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Of course, we cancel everything at the end
     */
    @AfterAll
    void clean() throws DatabaseException {
        userDB.deleteUserWithID(userID);
    }

    /**
     * Creates the test's compress and  test's decompress directories
     */
    private void createDirectories() {
        pathCompressed = System.getProperty("user.dir") + File.separator + "test" + File.separator + "out" + File.separator + "compressedTest" + File.separator;
        new File(pathCompressed).mkdirs();
        pathDecompressed = System.getProperty("user.dir") + File.separator + "test" + File.separator + "out" + File.separator +"decompressedTest" + File.separator;
        new File(pathDecompressed).mkdirs();
    }
    /**
     * This is only used in createFileCompressItThenDecompressIt()
     * Creates a sample .tex file
     */
    private void createFile() {
        file = new File(pathCompressed + "test.tex");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("\\draw (100, 100) circle (100pt);");
            writer.close();
        } catch (Exception e) {
            fail("File creation failed. Directory was: " + pathCompressed + " and error was: " + e.getMessage());
        }
    }

    /**
     * This is only used in createFileCompressItThenDecompressIt()
     * It compresses the .tex file
     */
    private void compressFile() {
        tarFile = new File(pathCompressed + File.separator + "test.tar");
        if (!tarFile.exists()) {
            try {
                ArrayList<File> files = new ArrayList<>();
                files.add(file);
                FolderCompressionModel.compress(pathCompressed + File.separator + "test.tar", files);
            } catch (CompressionException e){
                fail("Compression failed");
            }
        }
    }

    /**
     * This is only used in createFileCompressItThenDecompressIt()
     * It compresses the .tar file
     */
    private void decompressFile() throws VersioningException, DataAccessException {
        try {
            (new FolderDecompressionModel()).decompress(pathCompressed + "test.tar", pathDecompressed);
        } catch (DecompressException e) {
            e.printStackTrace();
        }
    }

    /**
     * Before each test, we compress the archive, and then we decompress it (in another folder)
     */
    @BeforeEach
    void createFileCompressItThenDecompressIt() throws VersioningException, DataAccessException {
        createDirectories();
        createFile();
        compressFile();
        decompressFile();
    }

    /**
     * And, of course, we cancel everything at the end
     */
    @AfterEach
    void deleteArchivesAndFiles() {
        new File(pathDecompressed + "test.tex").delete();
        new File(pathCompressed + "test.tex").delete();
        tarFile.delete();
    }

    /**
     * Checks if the file exists
     */
    @Test
    public void fileExistsAfterDecompression() {
        assertTrue(new File(pathDecompressed + "test.tex").exists());
    }
    /**
     * Check if the decompressed file has the right content
     */
    @Test
    public void contentIsCorrectAfterDecompression() {
        try {
            String stringTest = new String(Files.readAllBytes(Paths.get(pathDecompressed + "test.tex")));
            assertEquals("\\draw (100, 100) circle (100pt);", stringTest);
        } catch (IOException e) {
            fail("Unable to read the extracted sample file");
        }
    }
}
