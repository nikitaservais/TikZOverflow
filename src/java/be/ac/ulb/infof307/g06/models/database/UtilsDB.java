package be.ac.ulb.infof307.g06.models.database;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.exceptions.ValueNotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the utils part of the database
 */
public class UtilsDB extends Database{

    public UtilsDB() throws DatabaseConnectionException {
        super();
    }

    /**
     * Updates a row whose value is a String
     * @param sql        update instruction
     * @param newVal     new string value to set
     * @param identifier identifier of the row to update
     * @throws DatabaseException
     */
     void updateStringValue(String sql, String newVal, int identifier) throws DatabaseException {
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setString(1, newVal);
            statement.setInt(2, identifier);
            statement.executeUpdate();
        } catch (SQLException throwable) {
            throw new DatabaseException(throwable);
        }
    }

    /**
     * Checks if a value exists in the given table
     * @param sql query instruction
     * @param val the value of interest
     * @return true if value exists
     * @throws DatabaseException
     */
     boolean getBooleanQuery(String sql, String val) throws DatabaseException {
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setString(1, val);
            ResultSet value = statement.executeQuery();
            boolean ret = value.next();
            return ret;
        } catch (DatabaseException throwable) {
            throw new DatabaseException(throwable);
        } catch (Exception e) {
            // If it is any other error we know it comes form the connection method
            throw new DatabaseConnectionException(e);
        }
    }

    /**
     * Executes a query that has to return many integers
     * @param projectID the project ID
     * @param sql the query to execute
     * @return the integers resulting of the query
     * @throws DatabaseException if the query is invalid
     * @throws ValueNotFoundException if the project is not in the database
     * @throws InvalidDriverException if the driver is invalid
     */
     List<Integer> getIntegers(int projectID, String sql) throws DatabaseException, ValueNotFoundException, InvalidDriverException {
        List<Integer> identifier = new ArrayList<>();
        identifier.add(projectID);
        return getManyIntegerQuery(sql, "BRANCH_ID", identifier);
    }

    /**
     * Execute a query that has to return only one integer
     * @param sql         query instruction
     * @param dataLabel   field names of the table you want to select data from
     * @param identifiers list of identifiers of the rows to select
     * @return the integer resulting of the query
     * @throws DatabaseException if the query is invalid
     * @throws ValueNotFoundException if the indentifiers do not exist
     */
     int getSingleIntQuery(String sql, String dataLabel, List<Integer> identifiers) throws DatabaseException, ValueNotFoundException {
        ResultSet data;
        int val;
        data = getResultSet(sql, identifiers);
        try {
            val = data.getInt(dataLabel);
            return val;
        } catch (SQLException throwable) {
            throw new ValueNotFoundException(throwable);
        }
    }

    /**
     * Executes a query that has to return multiple strings
     * @param sql         query instruction
     * @param dataLabels  fields names of the table you want to select data from
     * @param identifiers list of identifiers of the rows to select
     * @return the strings resulting of the query
     * @throws DatabaseException if the query is not valid
     */
     String[] getManyStringQuery(String sql, String[] dataLabels, List<Integer> identifiers) throws DatabaseException {
        int nbData = dataLabels.length;
        String[] tab = new String[nbData];
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            for (int i = 0; i < identifiers.size(); i++) {
                statement.setInt(i + 1, identifiers.get(i));
            }
            ResultSet data = statement.executeQuery();
            while (data.next()) {
                for (int i = 0; i < nbData; i++) {
                    tab[i] = data.getString(dataLabels[i]);
                }
            }
            return tab;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Execute a query that has to return multiple integers
     * @param sql         query instruction
     * @param dataLabel   field name of the table you want to select data from
     * @param identifiers list of identifiers of the rows to select
     * @return the integers resulting of the query
     * @throws ValueNotFoundException if the identifiers do not exist
     * @throws DatabaseException if the query is not valid
     */
     List<Integer> getManyIntegerQuery(String sql, String dataLabel, List<Integer> identifiers) throws ValueNotFoundException, DatabaseException {
        List<Integer> data = new ArrayList<Integer>();
        ResultSet results;
        results = getResultSet(sql, identifiers);
        try {
            while (results.next()) {
                data.add(results.getInt(dataLabel));
            }
        } catch (SQLException e) {
            throw new ValueNotFoundException(e);
        }
        return data;
    }

    /**
     * Executes a query that do not return any result
     * @param id the id of the row to execute the query on
     * @param sql the query
     * @throws DatabaseException if the query is not valid
     */
     void resultlessQuery(int id, String sql) throws DatabaseException {
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Executes a query returning a non-defined set of results
     * @param sql the query
     * @param identifiers the identifiers of the rows to fetch
     * @return the set of results
     * @throws DatabaseException if the query is not valid
     */
    private ResultSet getResultSet(String sql, List<Integer> identifiers) throws DatabaseException {
        ResultSet results;
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            for (int i = 0; i < identifiers.size(); i++) {
                statement.setInt(i + 1, identifiers.get(i));
            }
            results = statement.executeQuery();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return results;
    }
}