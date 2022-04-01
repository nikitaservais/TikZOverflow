package be.ac.ulb.infof307.g06.models.branch;

import be.ac.ulb.infof307.g06.models.database.BranchDB;
import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.exceptions.DataAccessException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.models.user.User;

/**
 * Gets all the branches related to a project
 */
public class BranchChooserModel {
    private int projectId;

    /**
     * Constructor
     * @param projectId the project ID
     */
    public BranchChooserModel(int projectId) {
        this.projectId = projectId;
    }

    /**
     * provides the chosen branch
     * @return Project completed with all infos except user's name
     */
    public Project getBranch(String branchName) throws DataAccessException, DatabaseConnectionException {
        ProjectDB projectDB = new ProjectDB();
        BranchDB branchDB = new BranchDB();
        String[] basicData;
        String metaDataPath;
        int branchID;
        try {
            basicData = projectDB.getProjectData(projectId);
            branchID = branchDB.getBranchID(branchName,projectId);
            metaDataPath = projectDB.getMetadataPath(projectId,branchID);
            return new Project(basicData[0], new User(), basicData[3],
                    basicData[1], projectId, metaDataPath, branchID);
        } catch (DatabaseException e) {
            throw new DataAccessException(e);
        }
    }
}
