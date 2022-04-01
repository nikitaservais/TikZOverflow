package be.ac.ulb.infof307.g06.presentation.manageproject.editing.toolbar;

/**
 *  Toolbar button listener
 */
public interface ToolBarListener {
    void translateButtonClicked();
    void clearButtonClicked();
    void previewButtonClicked();
    void setSelectionMode(Boolean value);
}
