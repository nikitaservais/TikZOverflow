package be.ac.ulb.infof307.g06.models.user;

import be.ac.ulb.infof307.g06.exceptions.DataAccessException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.HashingException;
import be.ac.ulb.infof307.g06.exceptions.ModifyUserDataException;
import be.ac.ulb.infof307.g06.exceptions.SignInException;
import be.ac.ulb.infof307.g06.exceptions.SignUpException;
import be.ac.ulb.infof307.g06.exceptions.ValueNotFoundException;
import be.ac.ulb.infof307.g06.utils.PasswordUtils;
import be.ac.ulb.infof307.g06.models.database.UserDB;

import java.util.Arrays;
import java.util.Objects;

/**
 * manages a user account and links it to the database
 */
public class UserManager {
    private final UserDB userDB;

    public UserManager() throws DatabaseConnectionException {
        userDB = new UserDB();
    }

    /**
     * creates a new user inside the database from the given parameters
     * checks that both username and email are available before signing up
     * @param username the username of the user.
     * @param lastName the last name of the user.
     * @param firstName the first name of the user.
     * @param email the email of the user.
     * @param password the password of the user.
     * @return id of the inserted user
     * @throws SignUpException
     */
    public int signUp(String username, String lastName, String firstName, String email, String password) throws SignUpException {
        try {
            if (userDB.isEmailAvailable(email) && userDB.isUsernameAvailable(username)) {
                return userDB.createUser(username, lastName, firstName, email, password);
            }
            Exception e = new Exception();
            throw new SignUpException(e);
        } catch (DatabaseException e) {
            throw new SignUpException(e);
        }
    }

    /**
     * signs in a user by checking username and password match
     * If they do not match or if an error occurred during the verification process an error will be thrown.
     * @param username the username of the user.
     * @param password the password of the user.
     * @return user's id
     * @throws SignInException
     */
    public int signIn(String username, String password) throws SignInException {
        try {
            int id = userDB.getUserID(username);
            String[] data = userDB.getInfoUser(id);
            if (!Objects.equals(data[4], password)) {
                Exception e = new Exception();
                throw new SignInException(e);
            }
            return id;
        } catch (DatabaseException | ValueNotFoundException e) {
            throw new SignInException(e);
        }
    }

    /**
     * modifies the username of the specified user
     * @param id of the user.
     * @param newUsername username that will replace the old one.
     * @return message that will be displayed if there is an error.
     * @throws ModifyUserDataException
     */
    public String updateUsername(int id, String newUsername) throws ModifyUserDataException {
        String message = "";
        if (!newUsername.isEmpty()) {
            try {
                if (userDB.isUsernameAvailable(newUsername)) {
                    if (verifyUsername(newUsername)) {
                        userDB.modifyUsername(id, newUsername);
                    } else { message = "Please enter a valid username (without special characters and upper cases)."; }
                } else { message = "Username not available."; }
            } catch (DatabaseException e) {
                throw new ModifyUserDataException(e);
            }
        } else { message = "Please enter a new username."; }
        return message;
    }

    /**
     * Checks if username contains only lower cases (at least one letter) and no special characters.
     * @param username that will be verified.
     * @return true is username is valid, false otherwise.
     */
    private static boolean verifyUsername(String username) {
        return username.matches("[a-z]+");
    }

    /**
     * modifies the email of the specified user
     * @param id of the user.
     * @param newEmail email that will replace the old one.
     * @return message that will be displayed if there is an error.
     * @throws ModifyUserDataException
     */
    public String updateEmail(int id, String newEmail) throws ModifyUserDataException {
        String message = "";
        if (!newEmail.isEmpty()) {
            if (verifyEmail(newEmail)) {
                try {
                    if (userDB.isEmailAvailable(newEmail)) {
                        userDB.modifyEmail(id, newEmail);
                    } else { message = "Email not available."; }
                } catch (DatabaseException e) {
                    throw new ModifyUserDataException(e);
                }
            } else { message = "The email is not valid."; }
        } else { message = "Please enter a new email."; }
        return message;
    }

    /**
     * verifies if the mail is in a correct format.
     * @param email string of the email.
     * @return true if email is valid, false if not.
     */
    private static boolean verifyEmail(String email) {
        int firstAt = email.indexOf('@');
        if (firstAt == -1 || firstAt == 0) {
            return false;
        }
        int secondAt = email.lastIndexOf('@');
        if (firstAt != secondAt) {
            return false;
        }
        int dot = email.lastIndexOf('.');
        if (dot == -1 || dot < firstAt || dot == firstAt + 1) {
            return false;
        }
        String domain = email.substring(dot + 1);
        String[] domains = {"be", "com", "fr", "lu", "org", "eu", "nl", "de", "edu"};
        return Arrays.stream(domains).anyMatch(dom -> domain.toLowerCase().equals(dom));
    }

    /**
     * modifies the password of the specified user
     * @param id of the user.
     * @param oldPassword current password that will be changed.
     * @param newPasswordOne new password.
     * @param newPasswordTwo confirmation of the new password.
     * @return message that will be displayed if there is an error.
     * @throws ModifyUserDataException
     */
    public String updatePassword(int id, String oldPassword, String newPasswordOne, String newPasswordTwo) throws ModifyUserDataException {
        String[] data;
        String hashedNewPassword;
        try {
            data = userDB.getInfoUser(id);
            hashedNewPassword = PasswordUtils.hashPassword(newPasswordOne);
        } catch (DatabaseException | HashingException e) {
            throw new ModifyUserDataException(e);
        }
        String message = "";
        if (!oldPassword.isEmpty() && oldPassword.equals(data[4])) { //oldPassword is correct
            if (!newPasswordOne.isEmpty()) {
                if (!newPasswordTwo.isEmpty()) {
                    if (newPasswordOne.equals(newPasswordTwo)) {
                        if (newPasswordOne.length() >= 8) {
                            userDB.modifyPassword(id, hashedNewPassword);
                        } else { message = "Please enter a password of at least 8 characters."; }
                    } else { message = "Please enter the same password to confirm."; }
                } else { message = "Please confirm your new password."; }
            } else { message = "Please enter a new password."; }
        } else { message = "Your old password is incorrect."; }
        return message;
    }

    /**
     * create a User from the specified id and contacts the database to get all information
     * @param id id of the user
     * @return user with all information
     * @throws DataAccessException
     */
    public User getUser(int id) throws DataAccessException {
        String[] infoUser;
        try {
            infoUser = userDB.getInfoUser(id);
        } catch (DatabaseException e) {
            throw new DataAccessException(e);
        }
        User user = new User();
        user.setId(id);
        user.setUsername(infoUser[0]);
        user.setLastName(infoUser[1]);
        user.setFirstName(infoUser[2]);
        user.setEmail(infoUser[3]);
        user.setPassword(infoUser[4]);
        return user;
    }

    /**
     * used for insert a user without verification
     * @param user user with all information
     * @return user updated with its id
     * @throws ModifyUserDataException
     */
    public User save(User user) throws ModifyUserDataException {
        int id = 0;
        try {
            id = userDB.createUser(user.getUsername(), user.getLastName(), user.getFirstName(), user.getEmail(), user.getPassword());
        } catch (DatabaseException e) {
            throw new ModifyUserDataException(e);
        }
        user.setId(id);
        return user;
    }
}
