package be.ac.ulb.infof307.g06.presentation.help;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Class that display the help window
 */
public class HelpView extends BorderPane {
    private final HelpController helpController;
    private final SceneUtils scene;
    private final TreeItem rootItem;
    @FXML
    private TreeView<?> sideBarTreeView;
    @FXML
    private Label helpContent;

    /**
     * Constructor of HelpView class.
     * @param helpController The instance of the controller of this class.
     */
    public HelpView(HelpController helpController) {
        scene = new SceneUtils(this);
        this.helpController = helpController;
        loadFxml();
        rootItem = new TreeItem("Root node");
        setTreeItems();
        sideBarTreeView.setRoot(rootItem);
        sideBarTreeView.setShowRoot(false);
        helpContent.setId("helpText");
        sideBarTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setHelpContent());
        helpContent.setText(this.helpController.getHelpFileContent("/help/gettingStartedHelp.html"));
    }

    /**
     * Set the different tree items displayed in the help menu.
     */
    private void setTreeItems() {
        TreeItem gettingStartedItem = new TreeItem("Pour commencer");

        TreeItem overViewItem = new TreeItem("Aperçu de l'interface");
        overViewItem.getChildren().add(new TreeItem("Création d'un projet"));
        overViewItem.getChildren().add(new TreeItem("Ouverture d'un projet existant"));

        TreeItem editUserItem = new TreeItem("Modification des données utilisateur");
        editUserItem.getChildren().add(new TreeItem("Modification du nom d'utilisateur"));
        editUserItem.getChildren().add(new TreeItem("Modification de l'adresse mail"));
        editUserItem.getChildren().add(new TreeItem("Modification du mot de passe"));
        overViewItem.getChildren().add(editUserItem);

        TreeItem editionItem = new TreeItem("L'édition de projet");
        editionItem.getChildren().add(new TreeItem("La console d'édition"));
        editionItem.getChildren().add(new TreeItem("Le canvas d'édition"));
        editionItem.getChildren().add(new TreeItem("La barre d'outils"));
        gettingStartedItem.getChildren().add(overViewItem);
        rootItem.getChildren().add(gettingStartedItem);
        rootItem.getChildren().add(editionItem);

        TreeItem tikZItem = new TreeItem("Le language TikZ");
        tikZItem.getChildren().add(new TreeItem("Syntaxe primaire"));
        tikZItem.getChildren().add(new TreeItem("Dessiner des formes"));
        tikZItem.getChildren().add(new TreeItem("Dessiner des liens"));
        rootItem.getChildren().add(tikZItem);

        TreeItem versioningItem = new TreeItem("Système de version");
        versioningItem.getChildren().add(new TreeItem("Commit"));
        versioningItem.getChildren().add(new TreeItem("Revert"));
        versioningItem.getChildren().add(new TreeItem("Branch"));
        versioningItem.getChildren().add(new TreeItem("Merge"));
        versioningItem.getChildren().add(new TreeItem("Contenu des commits"));
        versioningItem.getChildren().add(new TreeItem("Historique des commits"));
        rootItem.getChildren().add(versioningItem);
    }

    /**
     * Set the items on the side bar of the help window.
     */
    public void setHelpContent() {
        String filePath;
        switch (sideBarTreeView.getSelectionModel().getSelectedItems().get(0).getValue().toString()) {
            case ("Aperçu de l'interface"):
                filePath = "interfaceOverviewHelp.html";
                break;
            case ("Création d'un projet"):
                filePath = "createProjectHelp.html";
                break;
            case ("Ouverture d'un projet existant"):
                filePath = "openProjectHelp.html";
                break;
            case ("Modification des données utilisateur"):
                filePath = "editUserProfileHelp.html";
                break;
            case ("Modification du nom d'utilisateur"):
                filePath = "changeUsernameHelp.html";
                break;
            case ("Modification de l'adresse mail"):
                filePath = "changeMailHelp.html";
                break;
            case ("Modification du mot de passe"):
                filePath = "changePasswordHelp.html";
                break;
            case ("L'édition de projet"):
                filePath = "editingHelp.html";
                break;
            case ("La console d'édition"):
                filePath = "consoleHelp.html";
                break;
            case ("Le canvas d'édition"):
                filePath = "drawingCanvasHelp.html";
                break;
            case ("La barre d'outils"):
                filePath = "toolBarHelp.html";
                break;
            case ("Le language TikZ"):
                filePath = "TikZHelp.html";
                break;
            case ("Syntaxe primaire"):
                filePath = "tikZPrimarySyntaxHelp.html";
                break;
            case ("Dessiner des formes"):
                filePath = "tikZShapeHelp.html";
                break;
            case ("Dessiner des liens"):
                filePath = "tikZBindHelp.html";
                break;
            case ("Système de version"):
                filePath = "versioningSystemHelp.html";
                break;
            case ("Commit"):
                filePath = "commitHelp.html";
                break;
            case ("Revert"):
                filePath = "revertHelp.html";
                break;
            case ("Branch"):
                filePath = "branchHelp.html";
                break;
            case ("Merge"):
                filePath = "mergeHelp.html";
                break;
            case ("Contenu des commits"):
                filePath = "commitContentHelp.html";
                break;
            case ("Historique des commits"):
                filePath = "commitHistoryHelp.html";
                break;
            default:
                filePath = "gettingStartedHelp.html";
                break;
        }
        helpContent.setText(helpController.getHelpFileContent("/help/" + filePath));
    }

    /**
     * Loads the fmxl of this class.
     */
    private void loadFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelpView.class.getResource("/be/ac/ulb/infof307/g06/presentation/help/HelpView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(HelpView -> this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
