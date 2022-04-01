package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.models.integrity.IntegrityModel;
import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.utils.FileUtils;
import be.ac.ulb.infof307.g06.models.branch.Branch;
import be.ac.ulb.infof307.g06.models.branch.BranchManager;
import be.ac.ulb.infof307.g06.models.commit.CommitManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows the managing of a project, including creating, getting, ...
 */
public class ProjectManager {
    private final ProjectDB projectDB;

    public ProjectManager() throws DatabaseConnectionException {
        try {
            projectDB = new ProjectDB();
        }
        catch (DatabaseConnectionException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    /**
     * Creates a new project
     * @param user the user creating the project
     * @param name the name of the new project
     * @param path the path of the new project
     * @return the created project
     * @throws DataAccessException if the data can't be read
     * @throws VersioningException if the branches are not valid
     * @throws IOException if the content of the project is invalid (or the file can't be read)
     * @throws CommitException if the commit are not valid
     * @throws DatabaseConnectionException if the database can't be reached
     */
    public Project createProject(User user, String name, String path) throws DataAccessException, VersioningException, IOException, CommitException, DatabaseConnectionException {
        int projectID = 0;
        try {
            IntegrityModel integrityModel = new IntegrityModel();
            projectID = projectDB.insertProject(user.getId(), name, path, "tmp_checksum");
            String checksum = integrityModel.hashProject(projectID, user);
            projectDB.updateChecksum(projectID, checksum);
        } catch (DatabaseException e) {
            throw new DataAccessException(e);
        }
        Project project = new Project();
        project.setId(projectID);
        project.setName(name);
        project.setFilePath(path);
        project.setContent("");
        project.setUser(user);
        project.setCurrentBranch(0);
        BranchManager branchManager = new BranchManager();
        int branchID = 0;
        branchID = branchManager.newBranch(project.getId(), ConstantsUtils.masterString);
        CommitManager commitManager = new CommitManager(projectID, branchID, new ArrayList<>(), new ArrayList<>());
        commitManager.createNewCommit("Master branch created");
        List<Branch> branchesFromProject = branchManager.getBranchesFromProject(project.getId());
        project.setBranches(branchesFromProject);
        try {
            String metaDataPath = projectDB.getMetadataPath(projectID, branchID);
            project.setPathToMetaData(metaDataPath);
        } catch (DatabaseException e) {
            throw new DataAccessException(e);
        }
        return project;
    }

    /**
     * Gets a project with the given ID
     * @param user owner of the project
     * @param projectId the project ID
     * @return the project asked for
     * @throws DataAccessException if the data can't be read
     * @throws DatabaseConnectionException if the database can't be reached
     */
    public Project getProject(User user, int projectId) throws DataAccessException, DatabaseConnectionException {
        String[] data;
        try {
            data = projectDB.getProjectData(projectId);
        } catch (DatabaseException e) {
            throw new DataAccessException(e);
        }
        Project project = new Project();
        project.setId(projectId);
        project.setName(data[0]);
        project.setFilePath(data[1]);
        project.setLastModification(data[3]);
        project.setUser(user);
        List<Branch> branches = new BranchManager().getBranchesFromProject(projectId);
        project.setBranches(branches);
        String content = null;
        try {
            content = FileUtils.fileToString(project.getFilePath());
        } catch (IOException e) {
            throw new DataAccessException(e);
        }
        project.setContent(content);
        project.setCurrentBranch(0);
        return project;
    }

    /**
     * Gets all the projects of an user
     * @param user the owner of the projects
     * @return a list containing the project of the project
     * @throws DataAccessException if the data can't be read
     * @throws DatabaseConnectionException if the database can't be reached
     */
    public List<Project> getAllProjects(User user) throws DataAccessException, DatabaseConnectionException {
        List<Integer> projectsOfUser;
        try {
            projectsOfUser = projectDB.getProjectsOfUser(user.getId());
        } catch (DatabaseException | ValueNotFoundException e) {
            throw new DataAccessException(e);
        }
        List<Project> projects = new ArrayList<>();
        for (Integer i : projectsOfUser) {
            Project project = getProject(user, i);
            projects.add(project);
        }
        return projects;
    }
}
