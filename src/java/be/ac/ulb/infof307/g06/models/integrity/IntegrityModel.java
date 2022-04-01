package be.ac.ulb.infof307.g06.models.integrity;

import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.models.project.ProjectManager;
import be.ac.ulb.infof307.g06.models.branch.BranchManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Allows to check the integrity of a project (including its name, content, branches files, ...)
 */
public class IntegrityModel {
    private MessageDigest messageDigest;
    private ProjectDB projectDB;

    public IntegrityModel() throws DatabaseConnectionException {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            //This cannot be thrown since the algorithm is hard-coded and not a parameter
        }
        projectDB = new ProjectDB();
    }

    /**
     * Hashes a File
     * @param fileToHash : file to hash
     * @return bytes of the hash
     * @throws IOException if the files can't be read
     * Source : https://howtodoinjava.com/java/io/how-to-generate-sha-or-md5-file-checksum-hash-in-java/
     */
    public byte[] hashFile(File fileToHash) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(fileToHash);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            messageDigest.update(byteArray, 0, bytesCount);
        }

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = messageDigest.digest();

        //return complete hash
        return bytes;
    }

    /**
     * Hashes a project
     * @param projectId : ID of the project we want to hash
     * @param user : User's project
     * @return String of the hash of the project
     * @throws IOException if the files can't be read
     * @throws VersioningException
     * @throws DataAccessException
     * @throws DatabaseConnectionException
     */
    public String hashProject(int projectId, User user) throws IOException, VersioningException, DataAccessException, DatabaseConnectionException {
        byte[] projectChecksum = new byte[1024];
        ProjectManager projectManager = new ProjectManager();
        BranchManager branchManager = new BranchManager();
        List<String> metadataPaths = branchManager.getProjectBranchesMetaData(projectId);
        Project texFileToHash = projectManager.getProject(user, projectId);
        String pathToTexFile = texFileToHash.getFilePath();
        File texFile = new File(pathToTexFile);
        projectChecksum = hashFile(texFile);
        for (int i = 0; i < metadataPaths.size(); i++) {
            File fileToHash = new File(texFile.getParent() + File.separator + metadataPaths.get(i));
            byte[] hash = hashFile(fileToHash);
            projectChecksum = xorByteArray(projectChecksum, hash);
        }
        return byteToString(projectChecksum);
    }

    /**
     * Does a XOR between two byte[]
     * @param projectChecksum : byte[] where we stock the result of the xor
     * @param arrayToAdd : right-side byte[] for the xor
     * @return projectChecksum
     */
    protected byte[] xorByteArray(byte[] projectChecksum, byte[] arrayToAdd) {
        for (int i = 0; i < projectChecksum.length; i++){
            //xor provide int so need to do some casting
            //Source : https://stackoverflow.com/questions/24004579/xor-bytes-in-java
            projectChecksum[i] = (byte) ((int)projectChecksum[i] ^ (int)arrayToAdd[i]);
        }
        return projectChecksum;
    }

    /**
     * Converts a byte array into a string
     * @param byteToConvert : byte to Convert into String
     * @return String converted
     */
    protected String byteToString(byte[] byteToConvert) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< byteToConvert.length ;i++)
        {
            sb.append(Integer.toString((byteToConvert[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Updates the checksum in the database
     * @param projectID : ID of the project
     * @param newchecksum : the new checksum to update
     * @throws DatabaseException
     */
    private void updateChecksum(int projectID, String newchecksum) throws DatabaseException {
        projectDB.updateChecksum(projectID, newchecksum);
    }

    /**
     * Updates the hash of the project
     * @param projectID : ID of the project
     * @param user : Owner of the project
     * @throws DataAccessException if the data can't be reached
     * @throws IOException if the file project can't be read
     * @throws VersioningException if the branches are not valid
     * @throws DatabaseException if the database can't be read
     */
    public void updateHashProject(int projectID, User user) throws DataAccessException, IOException, VersioningException, DatabaseException {
        String newchecksum = hashProject(projectID, user);
        updateChecksum(projectID, newchecksum);
    }

    /**
     * Verifies if the project is corrupted
     * @param project : the project to be verified
     * @return true if not corrupted, false otherwise
     * @throws IOException if the files can't be read
     * @throws DatabaseException
     * @throws VersioningException
     * @throws DataAccessException
     * @throws InvalidDriverException
     */
    public boolean verifyChecksum(Project project) throws IOException, DatabaseException, VersioningException, DataAccessException, InvalidDriverException {
        int projectID = project.getId();
        boolean res = false;
        String initialChecksum = projectDB.getProjectChecksum(projectID);
        String actualChecksum = hashProject(projectID, project.getUser());
        if (initialChecksum.equals(actualChecksum)) res = true;
        return res;
    }
}
