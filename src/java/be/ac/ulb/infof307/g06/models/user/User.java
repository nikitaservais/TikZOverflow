package be.ac.ulb.infof307.g06.models.user;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * used to represents a user with all his/her information
 */
public class User {
    private final IntegerProperty id;
    private final StringProperty username;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final StringProperty password;

    public User() {
        id = new SimpleIntegerProperty();
        username = new SimpleStringProperty();
        firstName = new SimpleStringProperty();
        lastName = new SimpleStringProperty();
        email = new SimpleStringProperty();
        password = new SimpleStringProperty();
    }

    /**
     * deprecated constructor setting the class attributes but not id of the user
     * @param username the username of the user.
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @param email the email of the user.
     * @param password the password of the user.
     */
    public User(String username, String firstName, String lastName, String email, String password) {
        this();
        this.username.set(username);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.email.set(email);
        this.password.set(password);
    }

    /**
     * should be used all the time, setting all the class attributes
     * @param username the username of the user.
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @param email the email of the user.
     * @param password the password of the user.
     * @param userId the user if of the user.
     */
    public User(String username, String firstName, String lastName, String email, String password, int userId) {
        this();
        this.username.set(username);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.email.set(email);
        this.password.set(password);
        this.id.set(userId);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

}
