package be.ac.ulb.infof307.g06.presentation.manageproject.editing.previewing;

import be.ac.ulb.infof307.g06.presentation.manageproject.editing.drawingcanvas.DrawingCanvasController;
import be.ac.ulb.infof307.g06.models.project.PreviewModel;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Preview the drawing part of the Project in a pdf.
 */
public class PreviewController {
    private final User user;
    private final DrawingCanvasController drawingCanvasController;
    private final PreviewModel previewModel;

    public PreviewController(User user, DrawingCanvasController drawingCanvasController) {
        this.user = user;
        this.drawingCanvasController = drawingCanvasController;
        this.previewModel = new PreviewModel(this.drawingCanvasController);
    }

    /**
     * Compile the pdf of the Project, and shows it
     * on the default software used by user computer for pdf
     * @throws IOException
     */
    public void preview() throws IOException {
        String previewFilePath = previewModel.generateFullPDF();
        File file = new File(previewFilePath);
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file);
        } else {
            showErrorMessage("Desktop is not supported!");
        }
        file.deleteOnExit();
    }

    /**
     * Show a new error message
     * @param message String - The message to display.
     */
    private void showErrorMessage(String message) {
        new ErrorMessage(message);
    }

}
