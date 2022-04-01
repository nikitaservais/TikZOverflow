package be.ac.ulb.infof307.g06.models.integrity;

import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.exceptions.DataAccessException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.models.project.ManageProjectModel;

/**
 * Manages the conflict of integrity of a project;
 * Deletes all the infos of the project, into the database and the real files.
 */
public class IntegrityActionModel {
    private Project project;

    public IntegrityActionModel(Project project) {
        this.project = project;
    }

    /**
     * Deletes the project files and all information in database.
     * @throws DataAccessException
     * @throws DatabaseException
     * @throws InvalidDriverException
     */
    public void deleteProject() throws DataAccessException, DatabaseException, InvalidDriverException {
        deleteProjectInDatabase();
        deleteProjectFiles();
    }

    /**
     * Deletes the project in the database.
     * @throws DatabaseException
     */
    protected void deleteProjectInDatabase() throws DatabaseException {
        ProjectDB projectDB = new ProjectDB();
        projectDB.deleteProject(project.getId());
    }

    /**
     * Deletes associated files with the project.
     * @throws DataAccessException
     * @throws DatabaseException
     */
    protected void deleteProjectFiles() throws DataAccessException, DatabaseException {
        ManageProjectModel manageProjectModel = new ManageProjectModel(project.getUser());
        manageProjectModel.deleteProject(project);
    }
}
