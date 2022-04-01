package be.ac.ulb.infof307.g06.presentation;

import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Window only showing the EULA agreement
 * */
public class EulaView {

    public EulaView(Stage primaryStage) throws MalformedURLException {
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        WebView webArea = new WebView();
        initWeb(webArea);
        VBox root = new VBox();
        root.getChildren().add(webArea);
        stage.setScene(new SceneUtils(root));
        stage.setResizable(false);
        stage.setTitle("Eula agreement");
        stage.show();
    }



    /**
     * init the web area
     * @param webArea the web view instance.
     * @throws MalformedURLException
     */
    private void initWeb(WebView webArea) throws MalformedURLException {
        URL local = EulaView.class.getResource("/eula/EULA.html");
        WebEngine we = webArea.getEngine();
        we.load(local.toString());

    }

}