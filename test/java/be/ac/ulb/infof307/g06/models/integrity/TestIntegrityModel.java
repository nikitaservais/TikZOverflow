package be.ac.ulb.infof307.g06.models.integrity;

import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.models.integrity.IntegrityModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIntegrityModel extends IntegrityModel {
    private IntegrityModel integrityModel;
    private File file = new File("TestFile.tex");
    private File metaData1 = new File(".metaData1");
    private File metaData2 = new File(".metaData2");
    private File metaData3 = new File(".metaData3");
    private File metaData4 = new File(".metaData4");
    private File metaData5 = new File(".metaData5");
    private List<String> listMetaData = Arrays.asList(".metaData1", ".metaData2", ".metaData3", ".metaData4", ".metaData5");
    private List<File> listMetaDataFile = Arrays.asList(metaData1, metaData2, metaData3, metaData4, metaData5);
    private String pathToMetaData = System.getProperty("user.dir") + File.separator;

    public TestIntegrityModel() throws InvalidDriverException, DatabaseException {
    }

    @BeforeAll
    public void setUp() throws IOException, InvalidDriverException, DatabaseException {
        integrityModel = new IntegrityModel();
        FileWriter writer = new FileWriter(file);

        //text is lorem ipsum from internet
        writer.write("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Diam volutpat commodo sed egestas egestas fringilla phasellus faucibus scelerisque. Ut lectus arcu bibendum at varius vel pharetra vel turpis. Ut porttitor leo a diam sollicitudin tempor. Bibendum at varius vel pharetra. Pretium aenean pharetra magna ac placerat vestibulum lectus mauris. Dictum at tempor commodo ullamcorper a lacus vestibulum. Nisl pretium fusce id velit. Tortor consequat id porta nibh venenatis cras sed felis. Sed arcu non odio euismod. Tellus mauris a diam maecenas sed. Penatibus et magnis dis parturient montes nascetur ridiculus mus mauris.\n" +
                "\n" +
                "Neque sodales ut etiam sit amet nisl. Purus semper eget duis at. Id cursus metus aliquam eleifend mi in nulla. Congue quisque egestas diam in arcu cursus euismod. Urna nec tincidunt praesent semper. Facilisis volutpat est velit egestas dui id ornare. Egestas dui id ornare arcu odio ut sem nulla. Pulvinar pellentesque habitant morbi tristique senectus et netus et. Feugiat in ante metus dictum at tempor commodo ullamcorper a. Tristique et egestas quis ipsum suspendisse. Sed vulputate odio ut enim blandit volutpat. Tortor dignissim convallis aenean et tortor. Iaculis at erat pellentesque adipiscing.\n" +
                "\n" +
                "Etiam erat velit scelerisque in dictum. Non diam phasellus vestibulum lorem sed risus ultricies. Turpis in eu mi bibendum neque egestas congue quisque. Integer feugiat scelerisque varius morbi enim nunc faucibus a pellentesque. Elementum integer enim neque volutpat ac tincidunt vitae semper. Dui accumsan sit amet nulla. Amet est placerat in egestas erat imperdiet sed euismod. Cras semper auctor neque vitae tempus quam pellentesque. Sem et tortor consequat id porta. Blandit turpis cursus in hac habitasse platea dictumst. Turpis tincidunt id aliquet risus feugiat in ante metus dictum. Arcu non odio euismod lacinia at quis.\n" +
                "\n" +
                "Luctus accumsan tortor posuere ac. Urna neque viverra justo nec ultrices dui sapien. Rhoncus est pellentesque elit ullamcorper dignissim. Augue neque gravida in fermentum et sollicitudin ac orci phasellus. Morbi tristique senectus et netus et malesuada fames ac. Pellentesque elit ullamcorper dignissim cras tincidunt lobortis. In eu mi bibendum neque egestas congue. Morbi tristique senectus et netus et malesuada. Congue nisi vitae suscipit tellus. Blandit turpis cursus in hac habitasse platea. Sit amet justo donec enim. Id venenatis a condimentum vitae sapien pellentesque habitant morbi tristique. Cursus vitae congue mauris rhoncus aenean vel.");
        writer.close();
        writer = new FileWriter(metaData1);
        writer.write("commit 324 \n" +
                "\t+\\Draw (100, 100) circle (100pt);\n" +
                "\t-\\Fill[color=blue] (100, 100) rectangle (400, 400);");
        writer.close();
        writer = new FileWriter(metaData2);
        writer.write("commit 3 \n" +
                "\t+\\Draw (100, 120) circle (100pt);\n" +
                "\t-\\Fill[color=red] (100, 100) rectangle (400, 400);");
        writer.close();
        writer = new FileWriter(metaData3);
        writer.write("commit 24 \n" +
                "\t+\\Fill[color=blue] (100, 100) rectangle (400, 400);\n" +
                "\t-\\Draw (80, 100) circle (20pt);");
        writer.close();
        writer = new FileWriter(metaData4);
        writer.write("commit 14 \n" +
                "\t+\\draw[color=red] (100, 100) rectangle (400, 400);\n" +
                "\t+\\draw[color=blue] (100, 100) -- (273, 412) -- (318, 123);\n" +
                "\t-\\fill[color=blue] (200, 200) circle (100pt);");
        writer.close();
        writer = new FileWriter(metaData5);
        writer.write("commit 21 \n" +
                "\\fill[color=red] (100, 100) rectangle (400, 400);\n" +
                "\t+\\draw[color=0x00ff00ff,fill=0x00ff00ff] (196,89) circle (50pt);\n" +
                "\t+\\draw[color=0x00ff00ff,fill=0x00ff00ff] (330,296) circle (50pt);");
        writer.close();
    }

    @AfterAll
    void clean() {
        if (file.exists()) {
            file.delete();
        } else {
            fail("File suppression failed.");
        }
        for (int i = 0; i < listMetaDataFile.size(); i++) {
            File CurrentMetaDataFile = listMetaDataFile.get(i);
            if (CurrentMetaDataFile.exists()) {
                CurrentMetaDataFile.delete();
            } else {
                fail("File suppression failed.");
            }
        }
    }

    @Test
    public void hashingTheSameFileGeneratesTheSameHash() throws IOException {
        byte[] firsthash = integrityModel.hashFile(file);
        byte[] secondhash = integrityModel.hashFile(file);
        String firsthashString = byteToString(firsthash);
        String secondhashString = byteToString(secondhash);
        assertEquals(firsthashString, secondhashString);
    }

    @Test
    public void hashingTheSameProjectGenerateTheSameHash() throws IOException {
        byte[] projectChecksum = hashFile(file);
        for (int i = 0; i < listMetaData.size(); i++) {
            File fileToHash = new File(listMetaData.get(i));
            byte[] hash = hashFile(fileToHash);
            projectChecksum = xorByteArray(projectChecksum, hash);
        }
        byte[] firsthash = projectChecksum;
        for (int i = 0; i < listMetaData.size(); i++) {
            File fileToHash = new File(listMetaData.get(i));
            byte[] hash = hashFile(fileToHash);
            projectChecksum = xorByteArray(projectChecksum, hash);
        }
        byte[] secondhash = projectChecksum;
        String firsthashString = byteToString(firsthash);
        String secondhashString = byteToString(secondhash);
        assertEquals(firsthashString, secondhashString);
    }
    /*
    @Test
    public void verifyThatComparingTwoGoodHashReturnTrue() {
        Database database = new database();
        database.
    }

    @Test
    public void verifyThatComparingTwoWrongHashReturnFalse() {

    }
     */
}
