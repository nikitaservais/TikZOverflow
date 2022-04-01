package be.ac.ulb.infof307.g06.presentation.manageproject.editing.console;

import be.ac.ulb.infof307.g06.models.ThemeModel;
import be.ac.ulb.infof307.g06.utils.FxmlUtils;
import be.ac.ulb.infof307.g06.models.tikz.TikzHighlight;
import be.ac.ulb.infof307.g06.utils.SceneUtils;
import javafx.scene.layout.StackPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

/**
 * Displays the console in the editing window.
 */
public class ConsoleView extends StackPane {
    private final SceneUtils scene;
    private static CodeArea codeArea;

    public ConsoleView() {
        FxmlUtils.loadFxml(this);
        initCodeArea();
        scene = new SceneUtils(this);
    }

    /**
     * Changes the text in the console.
     * @param text the text new text to set in the console.
     */
    public void changeText(String text) {
        codeArea.replaceText(text);
    }

    /**
     * Appends the given text in the console.
     * @param text the text to append in the console.
     */
    public void addCode(String text) {
        int len = codeArea.getLength();
        String oldText = codeArea.getText();
        if (len != 0) {
            oldText += "\n";
        }
        String newText = oldText + text;
        codeArea.replaceText(0, len, newText);
    }

    /**
     * Construct the code area.
     */
    private void initCodeArea() {
        codeArea = new CodeArea();
        VirtualizedScrollPane virtualizedScrollPane = new VirtualizedScrollPane<>(codeArea);
        getChildren().add(virtualizedScrollPane);
        getStylesheets().add(ConsoleView.class.getResource("/styling/" + (new ThemeModel()).getTheme() + ".css").toExternalForm());
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.textProperty().addListener((obs, oldText, newText) -> codeArea.setStyleSpans(0, new TikzHighlight().highlightText(newText)));
    }

    public String getText() {
        return codeArea.getText();
    }

    public static CodeArea getCodeArea() {
        return codeArea;
    }
}