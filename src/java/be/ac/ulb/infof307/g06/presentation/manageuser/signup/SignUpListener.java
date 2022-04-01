package be.ac.ulb.infof307.g06.presentation.manageuser.signup;

/**
 *  Sign up button listener
 */
public interface SignUpListener {
    void confirmButtonClicked(String username, String email, String firstName, String lastName, String password, String confirmPassword, boolean agreed);

    void eulaHyperlinkClicked();

    void signInHyperlinkClicked();
}
