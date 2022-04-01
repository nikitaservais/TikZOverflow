package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.exceptions.CompressionException;
import be.ac.ulb.infof307.g06.models.project.FolderCompressionModel;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestFolderCompressionModel {
    private String pathCompressed;
    private File file;

    /**
     * Creates a sample file we are going to compress
     * The tests fail if we are unable to create the file
     */
    @BeforeAll
    void setUp() {
        try {
            pathCompressed = System.getProperty("user.dir") + File.separator + "test" + File.separator + "out" + File.separator + "compressedTest" + File.separator;
            new File(pathCompressed).mkdirs();
            file = new File(pathCompressed + "test.tex");
            FileWriter writer = new FileWriter(file);
            writer.write("\\draw (100, 100) circle (100pt);");
            writer.close();
        } catch (IOException e){
            fail("File creation failed. Directory was: " + pathCompressed + " and error was: " + e.getMessage());
        }
    }

    /**
     * Deletes the sample file created before the tests
     * Fails if we are unable to delete this file
     */
    @AfterAll
    void clean() {
        if (file.exists()) {
            file.delete();
        } else {
            fail("File suppression failed.");
        }
    }

    @BeforeEach
    void compress() {
        try {
            ArrayList<File> files = new ArrayList<>();
            files.add(file);
            FolderCompressionModel.compress(pathCompressed + "test.tar", files);
        } catch (CompressionException e){
            fail("Compression failed");
        }
    }

    @AfterEach
    void deleteArchive() {
        File tarFile = new File(pathCompressed + "test.tar");
        if (tarFile.exists()) {
            tarFile.delete();
        } else {
            fail("Suppression of compressed archive failed");
        }
    }

    /**
     * We check if the compressed file has well been created
     */
    @Test
    public void archiveExistsAfterCompression() { assertTrue((new File(pathCompressed + "test.tar")).exists()); }
}
