package be.ac.ulb.infof307.g06;

import be.ac.ulb.infof307.g06.presentation.manageuser.signin.SignInController;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class Main extends Application {
    private static Main instance;

    /**
     * Classe main qui sera la première classe lancée par l'application
     * @param args parametre reçu
     * */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initialise l'application
     * @param primaryStage ...
     * */
    @Override
    public void start(Stage primaryStage) {
        instance = this;   //Used for pdf previewing.
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
        StageUtils.setStage(primaryStage);
        SignInController signIn = new SignInController();
        signIn.show();
    }

    public static Main getInstance() { return instance; }
}

