package be.ac.ulb.infof307.g06.presentation.manageuser.signup;

import be.ac.ulb.infof307.g06.models.ThemeModel;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SignUpView extends HBox {
    private final SceneUtils scene;
    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private CheckBox eulaCheckBox;

    @FXML
    private Hyperlink eulaHyperlink;

    @FXML
    private Button confirmButton;

    @FXML
    private Hyperlink signInHyperlink;

    private SignUpListener listener;
    @FXML
    private TextField emailTextField;

    public SignUpView() {
        FxmlUtils.loadFxml(this);
        scene = new SceneUtils(this);
        this.getScene().getStylesheets().add(getClass().getResource("/styling/" + (new ThemeModel()).getTheme() +".css").toExternalForm());
    }

    @FXML
    public void confirmButtonClicked(ActionEvent event) {
        String username = usernameTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailTextField.getText();
        boolean agreed = eulaCheckBox.isSelected();
        listener.confirmButtonClicked(username, email, firstName, lastName, password, confirmPassword, agreed);
    }

    @FXML
    public void eulaHyperlinkClicked(ActionEvent event) {
        listener.eulaHyperlinkClicked();
    }

    @FXML
    public void signInHyperlinkClicked(ActionEvent event) {
        listener.signInHyperlinkClicked();
    }

    public void setListener(SignUpListener listener) {
        this.listener = listener;
    }
}
