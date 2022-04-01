package be.ac.ulb.infof307.g06.models.database;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.exceptions.ValueNotFoundException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the access to the project part of the database
 */
public class ProjectDB extends Database{
    UtilsDB utilsDB;

    public ProjectDB() throws DatabaseConnectionException {
        super();
        utilsDB = new UtilsDB();
    }

    /**
     * Saves the project into the database
     * @param projectID the project ID
     * @param text the text to save as content of the project
     * @throws DatabaseException if the database can't be reached
     * @throws IOException if the file can't be saved with text content
     * @throws InvalidDriverException if the driver is invalid
     */
    public void saveProject(int projectID, String text) throws DatabaseException, IOException, InvalidDriverException {
        this.updateModificationDate(projectID);
        String[] fileInfo;
        fileInfo = this.getProjectData(projectID);
        File fileOld = new File(fileInfo[1]);
        fileOld.delete();
        File fileNew = new File(fileInfo[1]);

        FileWriter newFile;
        newFile = new FileWriter(fileNew, false);
        newFile.write(text);
        newFile.close();

    }

    /**
     * Inserts a new project into the database
     * @param userID     owner's id of the file
     * @param name       name of the file
     * @param pathToFile absolute path to the file in user's computer
     * @param hash the hash code of the project.
     * @return id of the inserted Project
     * @throws DatabaseException
     */
    public int insertProject(int userID, String name, String pathToFile, String hash) throws DatabaseException {
        try {
            String sql = "INSERT INTO Projects(USER_ID, NAME, PATH_TO_FILE, CREATION_DATE, MODIFICATION_DATE, CHECKSUM) VALUES(?,?,?, DATETIME('NOW'),DATETIME('NOW'), ?)";
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setInt(1, userID);
            statement.setString(2, name);
            statement.setString(3, pathToFile);
            statement.setString(4, hash);
            statement.executeUpdate();
            // provides the id of the new Project
            ResultSet data = this.connectionDB.executeSQLQuery("SELECT ID FROM Projects ORDER BY ID DESC LIMIT 1");
            int ret = data.getInt("ID");
            return ret;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Updates the checksum related to project with given ID
     * @param projectID the project ID
     * @param newChecksum the new checksum to store
     * @throws DatabaseException if the database can't be used
     */
    public void updateChecksum(int projectID, String newChecksum) throws DatabaseException {
        String sql = "UPDATE Projects SET CHECKSUM = ? WHERE ID = ?";
        utilsDB.updateStringValue(sql, newChecksum, projectID);
    }

    public String getProjectChecksum(int projectID) throws DatabaseException, InvalidDriverException {
        return this.getProjectData(projectID)[4];
    }

    /**
     * Provides all the projects id's of the user with given ID
     * @param userID the user ID
     * @return list of all the projects id's of the user
     * @throws DatabaseException
     * @throws ValueNotFoundException
     */
    public List<Integer> getProjectsOfUser(int userID) throws DatabaseException, ValueNotFoundException {
        String sql = "SELECT ID FROM Projects WHERE USER_ID = ?";
        List<Integer> identifier = new ArrayList<Integer>();
        identifier.add(userID);
        return utilsDB.getManyIntegerQuery(sql, "ID", identifier);
    }

    /**
     * Provides all related data of a specified project
     * @param id of the project
     * @return the name, the path to the file, the date of creation, the date of last modification
     * @throws DatabaseException if the database rejects the query
     */
    public String[] getProjectData(int id) throws DatabaseException {
        String sql = "SELECT * FROM Projects WHERE ID = ?";
        String[] dataLabels = {"NAME", "PATH_TO_FILE", "CREATION_DATE", "MODIFICATION_DATE", "CHECKSUM"};
        List<Integer> identifier = new ArrayList<Integer>();
        identifier.add(id);
        return utilsDB.getManyStringQuery(sql, dataLabels, identifier);
    }

    public String getProjectPath(int id) throws DatabaseException, InvalidDriverException {
        return this.getProjectData(id)[1];
    }

    public String getProjectName(int id) throws DatabaseException, InvalidDriverException {
        return this.getProjectData(id)[0];
    }

    /**
     * Provides the path to the metadata file linked to the branch
     * @param projectID The project id.
     * @param branchID The branch id.S
     * @return the path to the metadata file
     * @throws  DatabaseException
     */
    public String getMetadataPath(int projectID, int branchID) throws DatabaseException {
        String[] projectData = this.getProjectData(projectID);
        String projectPath = Paths.get(projectData[1]).getParent().toString();
        return projectPath + File.separator + ".project" + projectID + "_branch" + branchID;
    }

    /**
     * Renames a project withe given ID
     * @param id      id of the Project
     * @param newName new name of the Project
     * @throws DatabaseException
     */
    public void renameProject(int id, String newName) throws DatabaseException {
        String sql = "UPDATE Projects SET NAME = ? WHERE ID = ?";
        utilsDB.updateStringValue(sql, newName, id);
    }

    /**
     * Updates the project modification date to current date
     * @param id id of the Project
     * @throws DatabaseException
     */
    private void updateModificationDate(int id) throws DatabaseException {
        String sql = "UPDATE Projects SET MODIFICATION_DATE = DATETIME('NOW') WHERE ID = ?";
        utilsDB.resultlessQuery(id, sql);
    }

    /**
     * Modifies the project path
     * @param id      id of the project
     * @param newPath new path of the project
     * @throws DatabaseException
     */
    public void updatePath(int id, String newPath) throws DatabaseException {
        String sql = "UPDATE Projects SET PATH_TO_FILE = ? WHERE ID = ?";
        utilsDB.updateStringValue(sql, newPath, id);
    }

    /**
     * Deletes a project.
     * @param id the project ID
     * @throws DatabaseException
     */
    public void deleteProject(int id) throws DatabaseException {
        String sql = "DELETE FROM Projects WHERE ID = ?";
        utilsDB.resultlessQuery(id, sql);
    }

    /**
     * Checks if path exists in data access
     * @param path of the file
     * @return true if the project exists
     * @throws DatabaseException
     */
    public boolean checkProjectExist(String path) throws DatabaseException {
        String sql = "SELECT * FROM Projects WHERE PATH_TO_FILE=?";
        return utilsDB.getBooleanQuery(sql, path);
    }
}