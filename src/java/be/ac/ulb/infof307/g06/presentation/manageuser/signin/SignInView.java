package be.ac.ulb.infof307.g06.presentation.manageuser.signin;

import be.ac.ulb.infof307.g06.models.ThemeModel;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

/**
 * view for sign in menu; allowing the user to log into the application
 */
public class SignInView extends HBox {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button confirmButton;
    @FXML
    private Hyperlink signUpHyperLink;

    private final SceneUtils scene;
    private SignInListener listener;

    public SignInView() {
        FxmlUtils.loadFxml(this);
        this.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                confirmButton.fire();
                ev.consume();
            }
        });
        scene = new SceneUtils(this);
        this.getScene().getStylesheets().add(getClass().getResource("/styling/" + (new ThemeModel()).getTheme() +".css").toExternalForm());
    }

    @FXML
    /**
     * called when user tries to log in
     */
    public void confirmButtonClicked(ActionEvent event){
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        listener.confirmButtonClicked(username,password);
    }

    @FXML
    /**
     * link to the singUp menu
     */
    public void signUpHyperlinkClicked(ActionEvent event) {
        listener.signUpHyperlinkClicked();
    }


    public void setListener(SignInListener listener) {
        this.listener = listener;
    }
}
