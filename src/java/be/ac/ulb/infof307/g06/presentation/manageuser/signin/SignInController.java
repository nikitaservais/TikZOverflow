package be.ac.ulb.infof307.g06.presentation.manageuser.signin;

import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.mainmenu.MainMenuController;
import be.ac.ulb.infof307.g06.models.user.UserManager;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.presentation.manageuser.signup.SignUpController;
import be.ac.ulb.infof307.g06.utils.PasswordUtils;
import be.ac.ulb.infof307.g06.utils.StageUtils;


/**
 * Control the sign in action, verify the information given to see if the user can sign in.
 * Returns error if infos are incorrect.
 */
public class SignInController implements SignInListener {
    private final SignInView view;

    /**
     * Class constructor
     */
    public SignInController() {
        view = new SignInView();
        view.setListener(this);
    }

    public void show() {
        StageUtils.showScene(view.getScene());
    }

    /**
     * @param username of the user
     * @param password of the user
     */
    @Override
    public void confirmButtonClicked(String username, String password){
        try {
            String hashedPassword = PasswordUtils.hashPassword(password);
            attemptSignIn(username, hashedPassword);
        } catch (HashingException e) {
            new ErrorMessage("Error while processing your password.");
        }
    }

    @Override
    public void signUpHyperlinkClicked(){
        SignUpController signUp = new SignUpController();
        signUp.show();
    }

    /**
     * Verify if username and password match with an existing user and connects
     * @param username of the user
     * @param password of the user
     */
    private void attemptSignIn(String username, String password) {
        if (!verifyIfEmpty(username,password)) {
            try {
                UserManager model = new UserManager();
                int userId = model.signIn(username,password);

                if (userId != -1) {
                    User user = new UserManager().getUser(userId);
                    MainMenuController mainMenuController = new MainMenuController(user);
                    mainMenuController.show();
                } else {
                    new ErrorMessage("Invalid username or password");
                }
            } catch (SignInException | DataAccessException | DatabaseConnectionException e) {
                new ErrorMessage("Error while attempt to sign in.");
            }
        }
        else {
            new ErrorMessage("Please complete each field");
        }
    }

    /**
     * Verify if any field were left empty
     * @param username of the user
     * @param password of the user
     * @return true if the empty, false otherwise.
     */
    protected boolean verifyIfEmpty(String username, String password) {
        return username.isEmpty() || password.isEmpty();
    }
}
