package be.ac.ulb.infof307.g06.presentation.help;

import javafx.scene.Group;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Handle the help window.
 */
public class HelpWindow extends Button {

    public HelpWindow() {
        setText("?");
        setOnAction(e -> display());
    }

    /**
     * Makes the help window appear.
     */
    public static void display() {
        String path = "src" + File.separator + "be.ac.ulb.infof307.g06" + File.separator + "images" + File.separator;
        Stage helpWindow = new Stage();
        helpWindow.initModality(Modality.APPLICATION_MODAL);
        helpWindow.setTitle("Help");
        //Text
        Text text = createText("Help Window", 50, 50, "verdana", 20, true, true);
        Text textTwo = createText("I'm an help window, I help people, I'm usefull.", 50, 100, "verdana", 15, false, false);
        //Image
        ImageView imageView = createImage(path + "button.jpg", 50, 200, 500, 455);
        //Gif
        ImageView gifView = createImage(path + "test.gif", 50, 500, 200, 200);
        //Group
        Group root = new Group(text, textTwo, imageView, gifView);
        //CustomScene
        SceneUtils helpScene = new SceneUtils(root, 600, 800);
        helpWindow.setScene(helpScene);
        helpWindow.show();
    }

    /**
     * Create an image that can be displayed in the help window.
     * @param path  path to your image.
     * @param posX  X coordinate where the image will be displayed.
     * @param posY  Y coordinate where the image will be displayed.
     * @param sizeX width of the image.
     * @param sizeY heigth of the image.
     * @return ImageView that will be displayed.
     */
    public static ImageView createImage(String path, int posX, int posY, int sizeX, int sizeY) {
        ImageView imageView = null;
        try {
            //Setting the image view
            Image image = new Image(new FileInputStream(path));
            imageView = new ImageView(image);
            //image view characteristics
            imageView.setX(posX);
            imageView.setY(posY);
            imageView.setFitHeight(sizeY);
            imageView.setFitWidth(sizeX);
            imageView.setPreserveRatio(true);
        } catch (FileNotFoundException ex) {
        }
        return imageView;
    }

    /**
     * Create a text area that can be displayed in the help window.
     * @param completeText text that will be displayed.
     * @param posX         X coordinate where the text will be displayed.
     * @param posY         Y coordinate where the text will be displayed.
     * @param font the font of the text displayed.
     * @param fontSize the font size of the text displayed.
     * @param underline underline the text displayed.
     * @param bold bold the text displayed.
     * @return Text that will be displayed.
     */
    public static Text createText(String completeText, int posX, int posY, String font, int fontSize, boolean underline, boolean bold) {
        Text text = new Text();
        text.setX(posX);
        text.setY(posY);
        if (bold) {
            text.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, fontSize));
        } else {
            text.setFont(Font.font(font, FontPosture.REGULAR, fontSize));
        }
        text.setUnderline(underline);
        text.setText(completeText);
        return text;
    }
}
