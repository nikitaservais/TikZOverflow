package be.ac.ulb.infof307.g06.presentation.manageuser.signin;

/**
 *  Sign in button listener
 */
public interface SignInListener {
    void confirmButtonClicked(String username, String password);

    void signUpHyperlinkClicked();
}
