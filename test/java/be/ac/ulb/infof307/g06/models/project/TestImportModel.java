package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.exceptions.CompressionException;
import be.ac.ulb.infof307.g06.models.project.FolderCompressionModel;
import be.ac.ulb.infof307.g06.models.project.ImportModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class TestImportModel {
    private ImportModel importModel;
    private String outputPath;
    private User user;
    private File outputFile;


    @BeforeEach
    public void setUp() {
        user = new User("username", "firstname", "lastname", "email@email.com", "12345678");
        user.setId(1);
        importModel = new ImportModel();
        outputPath = System.getProperty("user.dir") + File.separator + "test" + File.separator + "out" + File.separator + "ImportProjectTest" + File.separator;
        outputFile = new File(outputPath);
        outputFile.mkdirs();
    }

    /**
     * Creates an odd sample .tex file
     */
    private String createSampleFile() {
        String tarFile = outputPath + "importTest.tar";
        try {
            File file = new File(outputPath + "test.tex");
            FileWriter writer;
            writer = new FileWriter(file);
            writer.write("\\draw (100, 100) circle (100pt);");
            writer.close();
            ArrayList<File> files = new ArrayList<>();
            files.add(file);
            FolderCompressionModel.compress(tarFile, files);
        } catch (IOException | CompressionException e) {
            fail("Unable to create sample file");
        }
        return tarFile;
    }

    /**
     * Test the output path getter, where to save imported files
     */
    @Test
    void pathIsCorrectAfterImport() {
        File filePath = new File(outputPath + "sample.jpg");
        assertEquals(outputPath, ImportModel.getPathFromFile(filePath) + File.separator);
    }

    /**
     * Tests if the imported file exists
     * try/catch are here to prevent test from crashing
     */
    @Test
    void projectIsCorrectlyImported() {
        try {
            File file = new File(createSampleFile());
            importModel.importProject(user, file, outputFile);
            assertTrue(new File(outputPath + "test.tex").exists());
            file.delete();
            new File(outputPath + "test.tex").delete();
        } catch (Exception e) {
            fail("Test importProject: unable to import Project");
        }
    }
}