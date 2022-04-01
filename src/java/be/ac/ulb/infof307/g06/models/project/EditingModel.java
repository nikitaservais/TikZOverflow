package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.models.database.BranchDB;
import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.models.user.User;

/**
 * link between database and controller
 * helps the communication between database and the rest of application
 */
public class EditingModel {
    private BranchDB branchDB;
    private ProjectDB projectDB;
    private Project project;

    /**
     * constructor setting class attributes
     * @param project
     * @throws DatabaseConnectionException
     */
    public EditingModel(Project project) throws DatabaseConnectionException {
        this.project = project;
        branchDB = new BranchDB();
        projectDB = new ProjectDB();
    }

    /**
     * provides the branch id from a branch name
     * @param branchName the name of the branch.
     * @return id of a branch
     * @throws VersioningException
     */
    public int getBranchID(String branchName) throws VersioningException {
        try {
            return this.branchDB.getBranchID(branchName, project.getId());
        } catch (Exception e) {
            throw new VersioningException(e);
        }
    }

    /**
     * provides the branch name from a branch id
     * @param branchID the id of the branch
     * @return name of a branch
     * @throws VersioningException
     */
    public String getBranchName(int branchID) throws VersioningException {
        try {
            return this.branchDB.getBranchData(project.getId(), branchID).getBranchName();
        } catch (Exception e) {
            throw new VersioningException(e);
        }
    }

    /**
     * updates the text a project in database
     * @param code text of a project
     * @throws ProjectDataAccessException
     */
    public void saveProject(String code) throws ProjectDataAccessException {
        try {
            this.projectDB.saveProject(project.getId(), code);
        } catch (Exception e) {
            throw new ProjectDataAccessException(e);
        }
    }

    /**
     * provides the project id of the project in attribute
     * @return title of a project
     * @throws ProjectDataAccessException
     */
    public String getProjectTitle() throws ProjectDataAccessException {
        try {
            return this.projectDB.getProjectData(project.getId())[0];
        } catch (Exception e) {
            throw new ProjectDataAccessException(e);
        }
    }

    /**
     * provides a branch encapsulated in Project object from its branch name
     * @param branchName name of a branch
     * @return Project that contains the branch information
     * @throws DataAccessException
     */
    public Project getBranch(String branchName) throws DataAccessException {
        try {
            String[] basicData = this.projectDB.getProjectData(project.getId());
            int branchID = this.branchDB.getBranchID(branchName, project.getId());
            String metaDataPath = this.projectDB.getMetadataPath(project.getId(), branchID);
            return new Project(basicData[0], new User(), basicData[3], basicData[1], project.getId(), metaDataPath, branchID);
        } catch (DatabaseException e) {
            throw new DataAccessException(e);
        }
    }
}
