package be.ac.ulb.infof307.g06.models.database;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.exceptions.ValueNotFoundException;
import be.ac.ulb.infof307.g06.models.branch.Branch;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the access to the branch part of the database
 */
public class BranchDB extends Database{
    UtilsDB utilsDB;

    public BranchDB() throws DatabaseConnectionException {
        super();
        utilsDB = new UtilsDB();
    }

    /**
     * Inserts a new branch into the dataaccess
     * @param projectID id of the project
     * @param name      of the branch
     * @return branchID
     * @throws DatabaseException
     */
    public int insertBranch(int projectID, String name) throws DatabaseException {
        String sql = "INSERT INTO Branchs(PROJECT_ID,BRANCH_ID,NAME,CREATION_DATE,STATUS) VALUES(?,?,?,DATETIME('NOW'),?)";
        int branchID;
        try {
            branchID = getLastBranchID(projectID);
            branchID += 1;
        } catch (ValueNotFoundException e) {
            branchID = 0; //insert branch Master
        }
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setInt(1, projectID);
            statement.setInt(2, branchID);
            statement.setString(3, name);
            statement.setInt(4, 1);
            statement.executeUpdate();
            return branchID;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Deletes a branch
     * @param projectID the project ID
     * @param branchID the branch ID
     * @throws DatabaseException if the database does not allow it
     */
    public void deleteBranch(int projectID, int branchID) throws  DatabaseException {
        String sql = "DELETE FROM Branchs WHERE PROJECT_ID = ? AND BRANCH_ID = ?";
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setInt(1, projectID);
            statement.setInt(2, branchID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * @param projectID id of the Project
     * @return the last branch ID
     * @throws DatabaseException
     * @throws ValueNotFoundException
     */
    public int getLastBranchID(int projectID) throws DatabaseException, ValueNotFoundException {
        String sql = "SELECT BRANCH_ID FROM Branchs WHERE PROJECT_ID = ? ORDER BY BRANCH_ID  DESC LIMIT 1;";
        List<Integer> identifier = new ArrayList<Integer>();
        identifier.add(projectID);
        return utilsDB.getSingleIntQuery(sql, "BRANCH_ID", identifier);
    }

    /**
     * Updates branch status to indicates if a branch is deleted or still active
     * @param projectID    id of the Project
     * @param branchID     id of the branch
     * @param branchStatus 0 if "deleted", 1 if still active
     * @throws DatabaseException
     */
    public void updateBranchStatus(int projectID, int branchID, int branchStatus) throws DatabaseException {
        String sql = "UPDATE Branchs SET STATUS = ? WHERE PROJECT_ID = ? AND BRANCH_ID = ?";
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setInt(1, branchStatus);
            statement.setInt(2, projectID);
            statement.setInt(3, branchID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Gets list of branches of a given Project
     * @param projectID id of the Project
     * @return list of branches' ID
     * @throws DatabaseException
     * @throws ValueNotFoundException
     * @throws InvalidDriverException
     */
    public List<Integer> getBranches(int projectID) throws DatabaseException, ValueNotFoundException, InvalidDriverException {
        String sql = "SELECT BRANCH_ID FROM Branchs WHERE PROJECT_ID = ?";
        return utilsDB.getIntegers(projectID, sql);
    }

    /**
     * Gets list of active branches of a given Project
     * @param projectID id of the Project
     * @return list of active branches' ID
     * @throws DatabaseException
     * @throws ValueNotFoundException
     * @throws InvalidDriverException
     */
    public List<Integer> getActiveBranches(int projectID) throws DatabaseException, ValueNotFoundException, InvalidDriverException {
        String sql = "SELECT BRANCH_ID FROM Branchs WHERE PROJECT_ID = ? AND STATUS = '1'";
        return utilsDB.getIntegers(projectID, sql);
    }

    /**
     * Provides all data of a specified branch of Project
     * @param projectID id of the Project
     * @param branchID  id of the branch
     * @return projectID, branchID, name, creation date, status (0 if deleted, 1 if still active)
     * @throws DatabaseException
     */
    public Branch getBranchData(int projectID, int branchID) throws DatabaseException {
        Branch branchToReturn;
        String sql = "SELECT * FROM Branchs WHERE PROJECT_ID = ? AND BRANCH_ID = ?";
        String[] dataLabels = {"PROJECT_ID", "BRANCH_ID", "NAME", "CREATION_DATE", "STATUS"};
        List<Integer> identifiers = new ArrayList<Integer>();
        identifiers.add(projectID);
        identifiers.add(branchID);
        String[] result;
        result = utilsDB.getManyStringQuery(sql, dataLabels, identifiers);
        if (result[1] == null) {
            branchToReturn = new Branch(-1, -1, "", "", 1);
        } else {
            branchToReturn = new Branch(Integer.parseInt(result[0]), Integer.parseInt(result[1]), result[2], result[3], Integer.parseInt(result[4]));
        }
        return branchToReturn;
    }

    /**
     * Fetches the id of a branch given its name.
     * @param branchName String - The name of the branch.
     * @param projectID the id of the project.
     * @return Integer - The branch id.
     * @throws DatabaseException
     */
    public Integer getBranchID(String branchName, int projectID) throws DatabaseException {
        String sql = "SELECT BRANCH_ID FROM Branchs WHERE NAME = ? AND PROJECT_ID = ?";
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setString(1, branchName);
            statement.setInt(2, projectID);
            ResultSet data = statement.executeQuery();
            int ret = data.getInt("BRANCH_ID");
            return ret;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}