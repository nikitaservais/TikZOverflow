package be.ac.ulb.infof307.g06.presentation.manageproject.editing;

import be.ac.ulb.infof307.g06.exceptions.CommitException;
import be.ac.ulb.infof307.g06.exceptions.DataAccessException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.DecompressException;
import be.ac.ulb.infof307.g06.exceptions.ExportProjectException;
import be.ac.ulb.infof307.g06.exceptions.ProjectDataAccessException;
import be.ac.ulb.infof307.g06.exceptions.VersioningException;
import be.ac.ulb.infof307.g06.models.project.EditingModel;
import be.ac.ulb.infof307.g06.models.branch.Branch;
import be.ac.ulb.infof307.g06.models.commit.CommitManager;
import be.ac.ulb.infof307.g06.models.integrity.IntegrityModel;
import be.ac.ulb.infof307.g06.models.project.ExportModel;
import be.ac.ulb.infof307.g06.models.project.ImportModel;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.models.shapes.DiagramShapeModel;
import be.ac.ulb.infof307.g06.presentation.FileChooserView;
import be.ac.ulb.infof307.g06.presentation.errormessage.ErrorMessage;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.console.ConsoleController;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.console.ConsoleControllerListener;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.drawingcanvas.DrawingCanvasController;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.drawingcanvas.DrawingControllerListener;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.previewing.PreviewController;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.toolbar.ToolBarController;
import be.ac.ulb.infof307.g06.presentation.manageproject.editing.toolbar.ToolBarControllerListener;
import be.ac.ulb.infof307.g06.presentation.menubar.MenuBarController;
import be.ac.ulb.infof307.g06.presentation.versioning.createcommit.CreateCommitController;
import be.ac.ulb.infof307.g06.presentation.versioning.revert.RevertControllerListener;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.utils.StageUtils;
import be.ac.ulb.infof307.g06.utils.StringCompareUtils;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Class combining every tool needed in Project edition
 */
public class EditingController implements MenuBarEditingControllerListener, ToolBarControllerListener, DrawingControllerListener, ConsoleControllerListener, RevertControllerListener {
    private final Stage primaryStage;
    private final EditingView editingView;
    private final Project project;
    private final EditingModel editingModel;
    private String lastCommitContent;
    private ConsoleController consoleController;
    private DrawingCanvasController drawingCanvasController;
    private ToolBarController toolBarController;
    private MenuBarController menuBarController;
    private PreviewController previewController;
    private Boolean selectionMode;

    /**
     * Constructor of the EditingController class.
     * @param project the instance of the project to be edited.
     * @throws DatabaseConnectionException if there has been a problem with the database.
     */
    public EditingController(Project project) throws DatabaseConnectionException {
        selectionMode = false;
        primaryStage = StageUtils.getStage();
        this.project = project;
        lastCommitContent = project.getContent();
        initControllers();
        editingView = new EditingView(consoleController.getView(), drawingCanvasController.getView(), toolBarController.getView(), menuBarController.getMenuBarView());
        editingModel = new EditingModel(project);
    }

    /**
     * Save the current state of the project.
     */
    @Override
    public void saveProject() {
        try {
            editingModel.saveProject(consoleController.getText());
        } catch (ProjectDataAccessException e) {
            new ErrorMessage("An error occurred while saving the current project.");
        }
        try {
            IntegrityModel integrityModel = new IntegrityModel();
            integrityModel.updateHashProject(project.getId(), project.getUser());
        } catch (IOException | VersioningException | DataAccessException | DatabaseException e) {
            new ErrorMessage("Error while saving project");
        }
    }

    /**
     * Manage the importation of a project when import project clicked.
     */
    @Override
    public void importProjectButtonClicked() {
        FileChooserView fileChooserView = new FileChooserView("Import Project", "Tar files", ConstantsUtils.targzExtensionString);
        File compressedFile = fileChooserView.showOpenDialogInStage(primaryStage);
        if (compressedFile != null) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Imported Files Save Destination");
            File folder = directoryChooser.showDialog(this.primaryStage);
            if (folder != null) {
                ImportModel importModel = new ImportModel();
                try {
                    importModel.importProject(project.getUser(), compressedFile, folder);
                } catch (DecompressException e) {
                    new ErrorMessage("An error occurred while trying to decompress\n" + "Error : " + e);
                }
            }
        }
    }
    /**
     * Manage the exportation of a project when export project clicked.
     */
    @Override
    public void exportProjectButtonClicked() {
        FileChooserView fileChooserView = new FileChooserView("Export Project", "Tar files", ConstantsUtils.targzExtensionString);
        File file = fileChooserView.showSaveDialogInStage(primaryStage);
        if (file != null) {
            try {
                new ExportModel().exportProject(project, file.getAbsolutePath());
            } catch (ExportProjectException e) {
                new ErrorMessage("An error occurred while trying to compress");
            }
        }
    }

    /**
     * extracts insertions and deletions since the last commit and send them
     * to the create commit controller
     */
    @Override
    public void createCommit() {
        try {
            String newText = consoleController.getText();
            ArrayList<String> newLines = StringCompareUtils.getInsertions(lastCommitContent, newText);
            ArrayList<String> removedLines = StringCompareUtils.getDeletions(lastCommitContent, newText);
            lastCommitContent = newText;
            CreateCommitController createCommitController = new CreateCommitController(project.getId(), project.getCurrentBranch(), newLines, removedLines);
            createCommitController.show();
            saveProject();
        } catch (DatabaseConnectionException e) {
            new ErrorMessage("Error while trying to create a commit");
        }
    }

    /**
     *
     * @return the id of the project currently open.
     */
    @Override
    public int getProjectID() {
        return project.getId();
    }

    /**
     * Update the branchID when branch changed.
     * @param branchName String - The new current branch.
     */
    @Override
    public void updateBranchID(String branchName) throws VersioningException {
        int id = editingModel.getBranchID(branchName);
        project.setCurrentBranch(id);
        String thisBranchText = consoleController.getText();
        lastCommitContent = thisBranchText;
    }

    /**
     * Method that update the console and canvas content with a given content.
     * @param projectContent String of the content to update the console and the canvas.
     */
    @Override
    public void updateConsoleCanvas(String projectContent) {
        consoleController.changeText(projectContent);
        drawingCanvasController.clear();
        consoleController.translate();
        saveProject();
    }

    /**
     * Makes the first commit after a new branch is created.
     * @param branch the new branch created.
     */
    @Override
    public void commitAfterNewBranch(Branch branch) {
        try {
            project.setCurrentBranch(branch.getBranchID());
            project.addNewBranch(branch);
            String newText = consoleController.getText();
            ArrayList<String> newLines = StringCompareUtils.getInsertions("", newText);
            CommitManager commitManager = new CommitManager(project.getId(), project.getCurrentBranch(), newLines, new ArrayList<>());
            commitManager.createNewCommit("New branch created");
        } catch (CommitException | DatabaseConnectionException e) {
            new ErrorMessage("Error while trying to commit after new branch creation");
        }
    }

    /**
     * @return The current branch name
     */
    @Override
    public String getBranchName() throws VersioningException {
        return editingModel.getBranchName(project.getCurrentBranch());
    }

    @Override
    public Project getBranch(String branchName) throws DataAccessException {
        return editingModel.getBranch(branchName);
    }

    /**
     * Translate the code in the console into drawing.
     */
    @Override
    public void translate() {
        drawingCanvasController.clear();
        consoleController.translate();
        saveProject();
    }

    /**
     * Clears the content in the drawing.
     */
    @Override
    public void clear() {
        drawingCanvasController.clear();
    }

    /**
     * Displays the preview of the project in pdf.
     */
    @Override
    public void preview() {
        try {
            previewController.preview();
        } catch (IOException e) {
            new ErrorMessage("An error has occurred during the pdf creation process\n" + "Error : " + e);
        }
    }

    /**
     * Add the code of new shape in drawing in the console.
     * @param shape the shape model to add to the console code.
     */
    @Override
    public void addTikZCodeFrom(DiagramShapeModel shape) {
        consoleController.setText(shape.getTikZCode());
    }

    /**
     * @return the size of the shape from DrawingCanvasView toolbar
     */
    @Override
    public Integer getShapeSize() {
        return toolBarController.getShapeSize();
    }

    /**
     * @return the color from DrawingCanvasView toolBar
     */
    @Override
    public Color getShapeColor() {
        return toolBarController.getShapeColor();
    }

    /**
     * @return the shape from DrawingCanvasView toolBar
     */
    @Override
    public String getShapeType() {
        return toolBarController.getShapeType();
    }

    /**
     * Draws the given rectangle
     * @param type       the type
     * @param color      of the outline
     * @param size       of the outline
     * @param coordinate of the rectangle
     */
    @Override
    public void drawRectangle(String type, Color color, Integer size, Double[][] coordinate) {
        drawingCanvasController.drawRectangle(type, color, size, coordinate);
    }

    /**
     * Draws the given circle
     * @param color      of the outline
     * @param size       of the outline
     * @param coordinate of the center
     */
    @Override
    public void drawCircle(Color color, Integer size, Double[][] coordinate) {
        drawingCanvasController.drawCircle(color, coordinate[1][0].intValue(), coordinate);
    }

    /**
     * Draws the given shape
     * @param type             of the outline
     * @param color            of the outline
     * @param size             of the outline
     * @param firstCoordinate  of the shape
     * @param secondCoordinate of the shape
     */
    @Override
    public void drawShape(String type, Color color, Integer size, Double[] firstCoordinate, Double[] secondCoordinate) {
        drawingCanvasController.drawShape(type, color, size, firstCoordinate, secondCoordinate);
    }

    /**
     * Fills the given rectangle
     * @param colorFill  to fill
     * @param size       of the rectangle
     * @param coordinate of the rectangle
     */
    @Override
    public void fillRectangle(Color colorFill, Integer size, Double[][] coordinate) {
        drawingCanvasController.fillRectangle(colorFill, size, coordinate);
    }

    /**
     * Fills the given circle
     * @param colorFill   to fill
     * @param size        of the circle
     * @param coordinates of the center
     */
    @Override
    public void fillCircle(Color colorFill, Integer size, Double[][] coordinates) {
        drawingCanvasController.fillCircle(colorFill, coordinates[1][0].intValue(), coordinates);
    }

    /**
     * Displays the linked view
     */
    public void show() {
        try {
            primaryStage.setTitle(editingModel.getProjectTitle());
        } catch (ProjectDataAccessException e) {
            new ErrorMessage("Error while getting project title");
        }
        primaryStage.setScene(editingView.getScene());
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * Initializes all the controllers and the views for the editingView.
     * Display the editingView afterwards.
     */
    private void initControllers() {
        drawingCanvasController = new DrawingCanvasController();
        drawingCanvasController.setListener(this);
        consoleController = new ConsoleController(project.getContent());
        consoleController.setListener(this);
        consoleController.translate();
        toolBarController = new ToolBarController();
        toolBarController.setListener(this);
        menuBarController = new MenuBarController(project);
        menuBarController.setMenuBarEditingControllerListener(this);
        previewController = new PreviewController(project.getUser(), drawingCanvasController);
    }

    //TODO 1. GET THE COORDINATES OF SELECTION CLICK
    // 2. CHANGE THE COPY PASTE CONTROLLER STATUS SO THAT IT CAN TREAT THE SHAPES

    /**
     * Activates the selection mode
     * @param value the new selected value
     */
    @Override
    public void setSelectionMode(Boolean value) {
        selectionMode = value;
        drawingCanvasController.setSelectionMode(value);
    }

    /**
     * @return the code in the console.
     */
    @Override
    public String getCode() {
        return consoleController.getText();
    }

    /**
     * Extract the information from a given code line.
     * @param instruction the the code line to translate
     * @param instOp instop
     * @param coord the coordinate of the shape
     * @return corresponding instance of DiagramShapeModel
     */
    @Override
    public DiagramShapeModel extractInformationFromCodeLine(String instruction, String[] instOp, Double[][] coord) {
        return consoleController.extractInformationFromCodeLine(instruction, instOp, coord);
    }
}
