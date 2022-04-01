package be.ac.ulb.infof307.g06.models.branch;

import be.ac.ulb.infof307.g06.models.database.BranchDB;
import be.ac.ulb.infof307.g06.models.database.CommitDB;
import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.models.VersionTextBuilder;
import be.ac.ulb.infof307.g06.models.commit.CommitManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows the managing of a branch, including getting content, managing the metadatas, ...
 */
public class BranchManager {
    private final BranchDB branchDB;

    public BranchManager() throws DatabaseConnectionException {
        branchDB = new BranchDB();
    }

    /**
     * Searches for all the branches
     * @return an array list containing the branches name
     */
    public List<Branch> getBranchesFromProject(int projectId) throws DataAccessException {
        List<Branch> branches = new ArrayList<>();
        List<Integer> branchesID = null;
        try {
            branchesID = branchDB.getBranches(projectId);
        } catch (DatabaseException | ValueNotFoundException | InvalidDriverException e) {
            throw new DataAccessException(e);
        }
        for (int id : branchesID) {
            Branch branch = null;
            try {
                branch = getBranch(projectId, id);
            } catch (DatabaseException | IOException | InvalidDriverException e) {
                throw new DataAccessException(e);
            }
            branches.add(branch);
        }
        return branches;
    }


    /**
     * Method that returns the text from the last commit of a given branchID.
     * @param branchName String - The branch name of the specified branch.
     * @return String - The content of the Project.
     */
    public String getTextFromBranch(int projectId, String branchName) throws VersioningException, ValueNotFoundException {
        try {
            ProjectDB projectDB = new ProjectDB();
            CommitDB commitDB = new CommitDB();
            int branchID = branchDB.getBranchID(branchName, projectId);
            String pathToMedataFile = projectDB.getMetadataPath(projectId, branchID);
            int lastCommitID = commitDB.getLastCommitID(projectId, branchID);
            VersionTextBuilder versionTextBuilder = new VersionTextBuilder(pathToMedataFile);
            return versionTextBuilder.getTextFrom(lastCommitID);
        } catch (DatabaseException | IOException e) {
            throw new VersioningException(e);
        }
    }

    /**
     * @param projectId
     * @return list of all path to metaData
     */
    public List<String> getProjectBranchesMetaData(int projectId) throws VersioningException {
        List<Integer> branchesID = null;
        try {
            branchesID = branchDB.getBranches(projectId);
        } catch (DatabaseException | ValueNotFoundException | InvalidDriverException e) {
            throw new VersioningException(e);
        }
        List<String> metadataPaths = new ArrayList<>();
        for (int branch : branchesID) {
            metadataPaths.add(metadataFileName(projectId, branch));
        }
        return metadataPaths;
    }

    /**
     * Creates the name of the metadata file related to a project and a branch
     * @param projectId the project ID
     * @param branch the branch ID
     * @return the name of the related metadata file
     */
    public String metadataFileName(int projectId, int branch) {
        return ConstantsUtils.projectExtensionString + projectId + ConstantsUtils.branchFileString + branch;
    }

    /**
     * Creates a metadata file related to a project and a branch
     * @param projectID the project ID
     * @param branchName the name of the Branch
     * @throws IOException if the file cannot be created
     * @throws VersioningException if the branch or the project is invalid
     */
    public void createMetadataFile(int projectID, String branchName) throws IOException, VersioningException {
        int branchID = 0;
        try {
            branchID = branchDB.getBranchID(branchName, projectID);
        } catch (DatabaseException e) {
            throw new VersioningException(e);
        }
        Writer writer;
        writer = getWriter(projectID, branchID);
        writer.write(ConstantsUtils.emptyString);
        writer.close();
    }

    /**
     * Gets a writer to write in a metadata file
     * @param projectID the project ID
     * @param branchID the branch ID
     * @return the writer
     * @throws FileNotFoundException if the file was not found
     * @throws VersioningException if the branch or the project is invalid
     */
    private Writer getWriter(int projectID, int branchID) throws FileNotFoundException, VersioningException {
        Writer writer;
        try {
            ProjectDB projectDB = new ProjectDB();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    new File(projectDB.getProjectPath(projectID)).getParent() +
                    File.separator + metadataFileName(projectID, branchID)), StandardCharsets.UTF_8));
        } catch (DatabaseException | InvalidDriverException e) {
            throw new VersioningException(e);
        }
        return writer;
    }

    /**
     * Create metadata file with initial content
     * @param projectID the project ID
     * @param branchID the branch ID
     * @param content the initial content
     * @throws IOException if the file is not found
     * @throws VersioningException if the branch or the project is invalid
     */
    public void createMetadataFile(int projectID, int branchID, String content) throws IOException, VersioningException {
        Writer writer = getWriter(projectID, branchID);
        writer.write(content);
        writer.close();
    }

    /**
     * Creates a new branch
     * @param projectID the project ID
     * @param branchName the new name of project
     * @return the new branch ID
     * @throws VersioningException if the project is invalid
     */
    public int newBranch(int projectID, String branchName) throws VersioningException {
        int branchID = 0;
        try {
            branchID = branchDB.insertBranch(projectID, branchName);
            this.createMetadataFile(projectID, branchName);
        } catch (DatabaseException | IOException e) {
            throw new VersioningException(e);
        }
        return branchID;
    }

    /**
     * Gets the branch with given id
     * @param projectId the project ID
     * @param id the ID of the branch we want to get
     * @return the branch
     * @throws DatabaseException if the branch is not present in the database
     * @throws IOException if the branch metadata file is unreadable
     * @throws DataAccessException if the data can't be reached
     * @throws InvalidDriverException if the driver is invalid
     */
    public Branch getBranch(int projectId, int id) throws DatabaseException, IOException, DataAccessException, InvalidDriverException {
        Branch branch;
        branch = branchDB.getBranchData(projectId, id);
        branch.setCommits(new CommitManager().getAllCommits(branch));
        return branch;
    }
}
