package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.integrity.IntegrityModel;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Allows the import of a project
 */
public class ImportModel {


    /**
     * Constructor
     */
    public ImportModel() {}

    /**
     * First we get the destination path for the imported Project,
     * Then We get the path including the filename and extension
     * of the imported file. Finally we decompress the tar.gz in the outputFile folde
     * @param user         : user that imported the file
     * @param importedFile : file imported
     * @param outputFile   : file where to import the output
     * @throws DecompressException
     */
    public void importProject(User user, File importedFile, File outputFile) throws DecompressException {
        try {
            ProjectDB projectDB = new ProjectDB();
            String importedFileAbsolutePath = importedFile.getAbsolutePath();
            List<File> decompressedFiles = new FolderDecompressionModel().decompress(importedFileAbsolutePath, outputFile.getPath());
            for (File decompressedFile : decompressedFiles) {
                if (FilenameUtils.getExtension(decompressedFile.getName()).equals("tex")) {
                    int projectID = projectDB.insertProject(user.getId(), decompressedFile.getName(), decompressedFile.getAbsolutePath(), "tmp_checksum");
                    IntegrityModel integrityModel = new IntegrityModel();
                    integrityModel.updateHashProject(projectID, user);
                } else {
                    // TODO : method to add metadata to project (in BranchManager)
                    decompressedFile.delete();
                }
            }
        } catch (VersioningException | DecompressException | DataAccessException | DatabaseException | IOException e) {
            throw new DecompressException(e);
        }
    }

    /**
     * Get the path where we need to save the files
     * by getting the path of the compressed folder
     * @param compressedFile the compressed file.
     * @return String - The path to the compressed file.
     */
    public static String getPathFromFile(File compressedFile) {
        return compressedFile.getParent();
    }

}
