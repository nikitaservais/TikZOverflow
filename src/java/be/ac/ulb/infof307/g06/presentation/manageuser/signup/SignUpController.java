package be.ac.ulb.infof307.g06.presentation.manageuser.signup;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.HashingException;
import be.ac.ulb.infof307.g06.exceptions.SignUpException;
import be.ac.ulb.infof307.g06.presentation.manageuser.signin.SignInController;
import be.ac.ulb.infof307.g06.utils.PasswordUtils;
import be.ac.ulb.infof307.g06.models.user.UserManager;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.EulaView;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.util.regex.Pattern;

/**
 * Control the sign up action, verify the infos integrity, and store it into the database if
 * everything is ok.
 */
public class SignUpController implements SignUpListener {
    private final SignUpView signUpView;
    private final Stage primaryStage;

    /**
     * Main constructor
     */
    public SignUpController() {
        this.primaryStage = StageUtils.getStage();
        signUpView = new SignUpView();
        signUpView.setListener(this);
    }

    /**
     * Used for test purposes.
     * @param testing true if constructor used in test, false otherwise.
     */
    public SignUpController(Boolean testing) {
        this.primaryStage = StageUtils.getStage();
        signUpView = null;
    }

    /**
     * called when trying to sign up
     * @param username of the user
     * @param email of the user
     * @param firstName of the user
     * @param lastName of the user
     * @param password of the user
     * @param confirmPassword of the user
     * @param agreed true if user accepted the conditions of utilisation.
     */
    @Override
    public void confirmButtonClicked(String username, String email, String firstName, String lastName, String password, String confirmPassword, boolean agreed) {
        try {
            attemptSignUp(username, email, firstName, lastName, password, confirmPassword, agreed);
        } catch (HashingException e) {
            new ErrorMessage("Error while processing your password.");
        }
    }

    /**
     * Displays the EULA text
     */
    @Override
    public void eulaHyperlinkClicked() {
        try{
            new EulaView(primaryStage);
        } catch (MalformedURLException e){
            new ErrorMessage("An error has occurred while trying to read the EULA file");
        }
    }

    /**
     * Displays the sign in form
     */
    @Override
    public void signInHyperlinkClicked() {
        SignInController signIn = new SignInController();
        signIn.show();
    }

    /**
     * Displays the associated view
     */
    public void show() {
        primaryStage.setScene(signUpView.getScene());
        primaryStage.show();
    }

    /**
     * Verify if passwords are matching is if so try to create an account
     * @param username of the user
     * @param email of the user
     * @param firstName of the user
     * @param lastName of the user
     * @param password of the user
     * @param confirmPassword same as previous
     * @param agreed if agreed is checked
     * @throws HashingException
     */
    private void attemptSignUp(String username, String email, String firstName, String lastName, String password, String confirmPassword, boolean agreed) throws HashingException {
        if (!verifyIfEmpty(username, email, firstName, lastName, password, confirmPassword)) {
            if(verifyUsername(username)) {
                if (verifyPassword(password, confirmPassword)) {
                    if (verifyEmail(email)) {
                        if (agreed) {
                            // Pass the hashed password only after it has been verified that the password follows the conventions
                            try {
                                new UserManager().signUp(username, lastName, firstName, email, PasswordUtils.hashPassword(password));
                                SignInController signIn = new SignInController();
                                signIn.show();
                            } catch (SignUpException | DatabaseConnectionException e) {
                                new ErrorMessage("Email or username already exists");
                            }
                        } else {
                            new ErrorMessage(
                                    "Please agree to our terms of utilisation before creating your account");
                        }
                    } else {
                        new ErrorMessage(
                                "Please insert a valid email address!" +
                                        " Accepted domains are .be, .com, .fr, .lu, .org, .eu, .nl, .de, .edu");
                    }
                }
                else {
                    new ErrorMessage(
                            "Passwords are not matching and must be at least 8 characters long");
                }
            }
            else {
                new ErrorMessage(
                        "Username should only contain lower cases and no special characters");
            }
        }
        else {
            new ErrorMessage( "Please fill each field");
        }
    }

    /**
     * Verify if any field were left empty
     * @param username of the user
     * @param email of the user
     * @param firstName of the user
     * @param lastName of the user
     * @param password of the user
     * @param confirmPassword the same than the previous
     * @return true if at least one field is empty else return false.
     */
    protected boolean verifyIfEmpty(String username, String email, String firstName, String lastName, String password, String confirmPassword) {
        return username.isEmpty() || email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty();
    }

    /**
     * Verify if the mail is in a correct format
     * @param email the email of the user.
     * @return true if email is a valid one. False otherwise
     */
    protected boolean verifyEmail(String email) {
        // https://www.geeksforgeeks.org/check-email-address-valid-not-java/
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        } return pat.matcher(email).matches();
    }

    /**
     * Check if username contains only lower cases (at least one letter) and no special characters
     * @param username the username of the user.
     * @return true is username is valid, else otherwise
     */
    protected boolean verifyUsername(String username) {
        return username.matches("[a-z]+");
    }

    /**
     * Check if passwords are the same and are at least 8 characters long
     * @param password the password.
     * @param confirmPassword the true password.
     * @return true if passwords are valid, else otherwise
     */
    protected boolean verifyPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword) && password.length() >= 8;
    }
}
