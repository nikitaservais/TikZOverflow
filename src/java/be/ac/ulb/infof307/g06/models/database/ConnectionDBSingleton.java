package be.ac.ulb.infof307.g06.models.database;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;

import java.sql.*;

/**
 * Allows the connection to a database
 */
public class ConnectionDBSingleton {

    private static ConnectionDBSingleton ourInstance = null;
    private static Connection connection;

    /**
     * Establish connection to the database and set up connection attribute
     * @throws InvalidDriverException if the driver is invalid
     * @throws DatabaseConnectionException if the database can't be reached
     */
    private ConnectionDBSingleton() throws InvalidDriverException, DatabaseException {
        connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new InvalidDriverException(e);
        }
        String url = "jdbc:sqlite:" + ConstantsUtils.DBPATH;
        try {
            connection = DriverManager.getConnection(url);
            Statement stmt = connection.createStatement();
            String sql = "PRAGMA foreign_keys=ON";
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
        createProjectsTable();
    }

    /**
     * Gets the instance of the ConnectionDBSingleton singleton
     * @return an Instance of the database
     * @throws InvalidDriverException if the driver is invalid
     * @throws DatabaseException if the database can't be reached
     */
    public static ConnectionDBSingleton getInstance() throws InvalidDriverException, DatabaseException {
        if (ourInstance == null) {
            ourInstance = new ConnectionDBSingleton();
        }
        return ourInstance;
    }

    /**
     * Creates an SQL statement
     * @param sqlRequest the sql request.
     * @return An SQL statement
     * @throws DatabaseException if the query is invalid
     */
    public PreparedStatement getStatement(String sqlRequest) throws DatabaseException {
        try {
            return connection.prepareStatement(sqlRequest);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Creates and execute an SQL request
     * @param sqlRequest the request applied to the database
     * @return ResultSet of the retrieved rows
     * @throws DatabaseException if the request is invalid
     */
    public ResultSet executeSQLQuery(String sqlRequest) throws DatabaseException {
        try {
            return connection.createStatement().executeQuery(sqlRequest);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Creates SQLite tables if not already existing
     * @throws DatabaseException if the query are not possible
     */
    private static void createProjectsTable() throws DatabaseException {
        try {
            Statement stmt = connection.createStatement();
            String usersTable = "CREATE TABLE IF NOT EXISTS Users (" + "ID INTEGER PRIMARY KEY AUTOINCREMENT," + "USER VARCHAR(100) NOT NULL UNIQUE," + "NAME VARCHAR(100) NOT NULL," + "GIVEN_NAME VARCHAR(100) NOT NULL," + "EMAIL VARCHAR(100) NOT NULL UNIQUE," + "PASSWORD VARCHAR(100) NOT NULL);";
            stmt.execute(usersTable);
            String projectsTable = "CREATE TABLE IF NOT EXISTS Projects (" + "ID INTEGER PRIMARY KEY AUTOINCREMENT," + "USER_ID INTEGER NOT NULL," + "NAME VARCHAR(100) NOT NULL," + "PATH_TO_FILE VARCHAR(100) NOT NULL," + "CREATION_DATE DATETIME NOT NULL," + "MODIFICATION_DATE DATETIME NOT NULL," + "CHECKSUM VARCHAR(100) NOT NULL)";
            stmt.execute(projectsTable);
            String branchTable = "CREATE TABLE IF NOT EXISTS Branchs (" + "PROJECT_ID INTEGER NOT NULL," + "BRANCH_ID INTEGER NOT NULL," + "NAME VARCHAR(100) NOT NULL," + "CREATION_DATE DATETIME NOT NULL," + "STATUS INTEGER NOT NULL," + "PRIMARY KEY(PROJECT_ID, BRANCH_ID)," + "FOREIGN KEY (PROJECT_ID) REFERENCES Projects (ID) ON DELETE CASCADE)";
            stmt.execute(branchTable);
            String commitsTable = "CREATE TABLE IF NOT EXISTS Commits (" + "PROJECT_ID INTEGER NOT NULL," + "BRANCH_ID INTEGER NOT NULL," + "COMMIT_ID INTEGER NOT NULL," + "MESSAGE VARCHAR(100) NOT NULL," + "CREATION_DATE DATETIME NOT NULL," + "PRIMARY KEY(PROJECT_ID, BRANCH_ID, COMMIT_ID)," + "FOREIGN KEY (PROJECT_ID, BRANCH_ID) REFERENCES Branchs (PROJECT_ID,BRANCH_ID) ON DELETE CASCADE)";
            stmt.execute(commitsTable);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}