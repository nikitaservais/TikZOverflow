package be.ac.ulb.infof307.g06.models.database;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.exceptions.ValueNotFoundException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the access to the commit part of the database
 */
public class CommitDB extends Database{
    UtilsDB utilsDB;

    public CommitDB() throws DatabaseConnectionException {
        super();
        utilsDB = new UtilsDB();
    }

    /**
     * @param projectID id of the Project
     * @param branchID  id of the branch
     * @param message   commit's message
     * @return id of the inserted commit
     * @throws DatabaseException
     */
    public int insertCommit(int projectID, int branchID, String message) throws DatabaseException {
        String sql = "INSERT INTO Commits(PROJECT_ID,BRANCH_ID,COMMIT_ID,MESSAGE,CREATION_DATE) VALUES(?,?,?,?,DATETIME('NOW'))";
        int commitID;
        try {
            commitID = getLastCommitID(projectID, branchID);
            commitID += 1;
        } catch (ValueNotFoundException e) {
            commitID = 0;
        }
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setInt(1, projectID);
            statement.setInt(2, branchID);
            statement.setInt(3, commitID);
            statement.setString(4, message);
            statement.executeUpdate();
            return commitID;
        } catch (Exception throwable) {
            throw new DatabaseException(throwable);
        }
    }

    /**
     * Gets the last commit ID
     * @param projectID the project ID
     * @param branchID the branch ID
     * @return the last commit ID
     * @throws DatabaseException if the database can't be reached
     * @throws ValueNotFoundException if there is no commit
     */
    public int getLastCommitID(int projectID, int branchID) throws DatabaseException, ValueNotFoundException {
        String sql = "SELECT COMMIT_ID FROM Commits WHERE PROJECT_ID = ? AND BRANCH_ID = ? ORDER BY COMMIT_ID DESC LIMIT 1;";
        List<Integer> identifiers = new ArrayList<Integer>();
        identifiers.add(projectID);
        identifiers.add(branchID);
        return utilsDB.getSingleIntQuery(sql, "COMMIT_ID", identifiers);
    }

    /**
     * Provides every commits id's of a given branch
     * @param projectID id of the Project
     * @param branchID  id of the branch
     * @return list of all commits of a given branch
     * @throws DatabaseException
     * @throws ValueNotFoundException
     */
    public List<Integer> getCommits(int projectID, Integer branchID) throws DatabaseException, ValueNotFoundException {
        String sql = "SELECT COMMIT_ID FROM Commits WHERE PROJECT_ID = ? AND BRANCH_ID = ? ";
        List<Integer> identifiers = new ArrayList<Integer>();
        identifiers.add(projectID);
        identifiers.add(branchID);
        return utilsDB.getManyIntegerQuery(sql, "COMMIT_ID", identifiers);
    }

    /**
     * Provides all data of a specified branch for a given Project
     * @param projectID id of the Project
     * @param branchID  if of the branch
     * @param commitID  if of the commit
     * @return projectID, branchID, commitID, message of the commit, creation date
     * @throws DatabaseException
     */
    public String[] getCommitData(int projectID, int branchID, int commitID) throws DatabaseException {
        String sql = "SELECT * FROM Commits WHERE PROJECT_ID = ? AND BRANCH_ID = ? AND COMMIT_ID = ?";
        String[] dataLabels = {"PROJECT_ID", "BRANCH_ID", "COMMIT_ID", "MESSAGE", "CREATION_DATE"};
        List<Integer> identifiers = new ArrayList<Integer>();
        identifiers.add(projectID);
        identifiers.add(branchID);
        identifiers.add(commitID);
        return utilsDB.getManyStringQuery(sql, dataLabels, identifiers);
    }

    /**
     * Deletes a specified commit
     * @param projectID Project Id of the commit to delete
     * @param branchID  Branch Id of the commit to delete
     * @param commitID  Commit Id of the commit to delete
     * @throws DatabaseException
     */
    public void deleteCommit(int projectID, int branchID, int commitID) throws DatabaseException {
        String sql = "DELETE FROM Commits WHERE PROJECT_ID = ? AND BRANCH_ID = ? AND COMMIT_ID = ? ";
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setInt(1, projectID);
            statement.setInt(2, branchID);
            statement.setInt(3, commitID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}