package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.branch.BranchManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Exports a file and compress it.
 */
public class ExportModel {

    public ExportModel() {
    }

    /**
     * Exports project with projectId to exportPath, use folderCompressionModel
     * @param project the project to export.
     * @param exportPath the path to the place to export the project.
     * @throws ExportProjectException
     */
    public void exportProject(Project project, String exportPath) throws ExportProjectException {
        int projectId = project.getId();
        String[] projectData;
        try {
            ProjectDB projectDB = new ProjectDB();
            projectData = projectDB.getProjectData(projectId);
        } catch (DatabaseException e) {
            throw new ExportProjectException(e);
        }
        File projectFile = new File(projectData[1]);
        List<File> files = new ArrayList<>(); // files to be compress
        files.add(projectFile);
        List<String> projectBranchesMetaData = null;
        try {
            projectBranchesMetaData = new BranchManager().getProjectBranchesMetaData(projectId);
        } catch (VersioningException | DatabaseConnectionException e) {
            throw new ExportProjectException(e);
        }
        String parent = projectFile.getParent();
        for (String file : projectBranchesMetaData) {
            File temp = new File(parent + File.separator + file);
            files.add(temp);
        }
        try {
            FolderCompressionModel.compress(exportPath, files);
        } catch (CompressionException e) {
            throw new ExportProjectException(e);
        }
    }
}
