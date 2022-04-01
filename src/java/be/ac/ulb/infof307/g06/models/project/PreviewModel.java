package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.presentation.manageproject.editing.drawingcanvas.DrawingCanvasController;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * PDF file previewing Model
 */
public class PreviewModel {
    private final Canvas diagramCanvas;

    /**
     * Constructor
     * @param drawingCanvasController the controller generating the Preview Model
     */
    public PreviewModel(DrawingCanvasController drawingCanvasController){
        this.diagramCanvas = drawingCanvasController.getCanvas();
    }

    /**
     * Makes a PNG image of the current Canvas object with the diagram
     * @return File image PNG
     * @throws IOException if the files can't be read
     */
    public File getCanvasImage() throws IOException{
        File imageOutput;
        WritableImage writableImage = new WritableImage(800, 800);
        this.diagramCanvas.snapshot(null, writableImage);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        imageOutput = new File("canvas" + new Date().getTime() + ".png");
        ImageIO.write(renderedImage, "png", imageOutput);

        return imageOutput;
    }

    /**
     * Generates a PDF with the text and the canvas image
     * @return String filename the path of the generated PDF
     * @throws IOException if the files can't be read
     */
    public String generateFullPDF() throws IOException {
        String filename = "diagramPreview.pdf";
        File canvasImage = this.getCanvasImage();
        PDDocument document = new PDDocument();
        PDPage imagePage = new PDPage();
        PDImageXObject pdfImage;
        PDPageContentStream content;
        pdfImage = PDImageXObject.createFromFile(canvasImage.getAbsolutePath(), document);
        content = new PDPageContentStream(document, imagePage);
        float scale = 0.7f;
        content.drawImage(pdfImage, 20, 200, pdfImage.getWidth() * scale, pdfImage.getHeight() * scale);
        content.close();
        document.addPage(imagePage);
        String previewFilePath = System.getProperty("user.home") + File.separator + filename;
        document.save(previewFilePath);
        document.close();
        canvasImage.delete();
        return previewFilePath;
    }
}
