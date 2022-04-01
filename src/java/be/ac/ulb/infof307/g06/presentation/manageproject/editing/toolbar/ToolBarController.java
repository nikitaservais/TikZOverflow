package be.ac.ulb.infof307.g06.presentation.manageproject.editing.toolbar;

import javafx.scene.paint.Color;

/**
 * Controller of the top's toolbar
 */
public class ToolBarController implements ToolBarListener {
    private final ToolBarView toolBarView;
    private ToolBarControllerListener listener;

    public ToolBarController() {
        toolBarView = new ToolBarView();
        toolBarView.setListener(this);
    }

    /**
     * Asks to translate the code in diagrams
     */
    @Override
    public void translateButtonClicked() {
        listener.translate();
    }

    /**
     * Asks to clear the canvas
     */
    @Override
    public void clearButtonClicked() {
        listener.clear();
    }

    /**
     * Displays the previewing of the pdf
     */
    @Override
    public void previewButtonClicked(){
        listener.preview();
    }

    /**
     * @return the associated view
     */
    public ToolBarView getView() {
        return toolBarView;
    }

    /**
     * Getter to get the shape size selected.
     * @return int the size of the shape required.
     */
    public int getShapeSize() {
        return toolBarView.getShapeSize();
    }

    /**
     * Getter to get the shape type.
     * @return string the name of the button name selected.
     */
    public String getShapeType() {
        return toolBarView.getShapeType();
    }

    /**
     * Getter that gives the color wanted.
     * @return color the color selected.
     */
    public Color getShapeColor() {
        return toolBarView.getShapeColor();
    }

    /**
     * @param listener the link between controller and view
     */
    public void setListener(ToolBarControllerListener listener) {
        this.listener = listener;
    }

    /**
     * Activates the selection mode
     */
    @Override
    public void setSelectionMode(Boolean value) {
        this.listener.setSelectionMode(value);
    }
}
