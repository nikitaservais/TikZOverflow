package be.ac.ulb.infof307.g06.models.database;

import be.ac.ulb.infof307.g06.exceptions.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDB extends Database{
    UtilsDB utilsDB;

    public UserDB() throws DatabaseConnectionException {
        super();
        utilsDB = new UtilsDB();
    }

    /**
     * Insert a new user into the database
     *
     * @param username the new user's name
     * @param lastName the new user's last name
     * @param firstName the new user's first name
     * @param email the new user's mail address
     * @param password the new user's password
     * @return id of the inserted user
     * @throws DatabaseException
     */
    public int createUser(String username, String lastName, String firstName, String email, String password) throws DatabaseException {
        String sql = "INSERT INTO Users(USER,NAME,GIVEN_NAME,EMAIL,PASSWORD) VALUES(?,?,?,?,?)";
        int ret;

        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setString(1, username);
            statement.setString(2, lastName);
            statement.setString(3, firstName);
            statement.setString(4, email);
            statement.setString(5, password);
            statement.executeUpdate();

            ResultSet data = this.connectionDB.executeSQLQuery("SELECT ID FROM Users ORDER BY ID DESC LIMIT 1");
            ret = data.getInt("ID");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return ret;
    }

    /**
     * Deletes the user with given ID
     * @param id the user ID
     * @throws DatabaseException if the database can't execute the query
     * @throws DatabaseConnectionException if the database can't be reached
     */
    public void deleteUserWithID(int id) throws DatabaseException {
        String sql = "DELETE FROM Users WHERE ID=?";
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException throwable) {
            throw new DatabaseException(throwable);
        }
    }

    /**
     * Searches for the user ID according to his name
     * @param username the user's name
     * @return the id linked to the given user's name
     * @throws DatabaseException if the query can't be executed
     * @throws ValueNotFoundException if the user is not in the database
     */
    public int getUserID(String username) throws DatabaseException, ValueNotFoundException {
        String sql = "SELECT ID FROM Users WHERE USER=?";
        ResultSet data;
        try {
            PreparedStatement statement = this.connectionDB.getStatement(sql);
            statement.setString(1, username);
            data = statement.executeQuery();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        try {
            int ret = data.getInt("ID");
            return ret;
        } catch (SQLException e) {
            throw new ValueNotFoundException(e);
        }
    }

    /**
     * Returns all the user's informations
     * @param id user's id
     * @return username, name, given name, email, password
     * @throws DatabaseException
     */
    public String[] getInfoUser(int id) throws DatabaseException {
        String sql = "SELECT USER, NAME, GIVEN_NAME, EMAIL, PASSWORD FROM Users WHERE ID=?";
        String[] dataLabels = {"USER", "NAME", "GIVEN_NAME", "EMAIL", "PASSWORD"};
        List<Integer> idArray = new ArrayList<Integer>();
        idArray.add(id);
        return utilsDB.getManyStringQuery(sql, dataLabels, idArray);
    }

    /**
     * Checks if a username is available
     * @param username to check
     * @return true if username is available == not already taken
     * @throws DatabaseException
     */
    public boolean isUsernameAvailable(String username) throws DatabaseException {
        String sql = "SELECT * FROM Users WHERE USER=?";
        return !utilsDB.getBooleanQuery(sql, username);
    }

    /**
     * Checks if an email is available
     * @param email to check
     * @return true if email is available == not already taken
     * @throws DatabaseException
     */
    public boolean isEmailAvailable(String email) throws DatabaseException {
        String sql = "SELECT * FROM Users WHERE EMAIL=?";
        return !utilsDB.getBooleanQuery(sql, email);
    }

    /**
     * Modifies the user's email
     * @param userId the user ID to change the mail of
     * @param newEmail the new mail address
     * @throws ModifyUserDataException
     */
    public void modifyEmail(int userId, String newEmail) throws ModifyUserDataException {
        String sql = "UPDATE Users SET EMAIL = ? WHERE ID = ?";
        try {
            utilsDB.updateStringValue(sql, newEmail, userId);
        } catch (Exception e) {
            throw new ModifyUserDataException(e);
        }
    }

    /**
     * Modifies the user's password
     * @param userId the user ID to change the password of
     * @param newPassword the new password
     * @throws ModifyUserDataException
     */
    public void modifyPassword(int userId, String newPassword) throws ModifyUserDataException {
        String sql = "UPDATE Users SET PASSWORD = ? WHERE ID = ?";
        try {
            utilsDB.updateStringValue(sql, newPassword, userId);
        } catch (Exception e) {
            throw new ModifyUserDataException(e);
        }
    }

    /**
     * Modifies the user's username
     * @param userId the user ID to change the username of
     * @param newUsername the new username
     * @throws ModifyUserDataException
     */
    public void modifyUsername(int userId, String newUsername) throws ModifyUserDataException {
        String sql = "UPDATE Users SET USER = ? WHERE ID = ?";
        try {
            utilsDB.updateStringValue(sql, newUsername, userId);
        } catch (Exception e) {
            throw new ModifyUserDataException(e);
        }
    }
}