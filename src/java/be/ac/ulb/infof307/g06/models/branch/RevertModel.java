package be.ac.ulb.infof307.g06.models.branch;

import be.ac.ulb.infof307.g06.models.database.BranchDB;
import be.ac.ulb.infof307.g06.models.database.CommitDB;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.models.VersionTextBuilder;
import be.ac.ulb.infof307.g06.models.commit.Commit;

import java.io.*;
import java.util.List;

/**
 * Allows to revert to a previous commit
 */
public class RevertModel {
    private Project master;
    private int projectID;
    private BranchDB branchDB;

    /**
     * Class Constructor
     * @param project the current project
     * @throws DatabaseConnectionException if the database can't be reached
     */
    public RevertModel(Project project, int projectID) throws DatabaseConnectionException {
        this.master = project;
        this.projectID = projectID;
        this.branchDB = new BranchDB();
    }

    /**
     * Constructor for tests
     */
    protected RevertModel() {
    }

    /**
     * Reverts to a previous commit
     * @param commitID the ID of the commit we want to revert to
     * @param projectID the project ID of the project to revert
     * @param branchName the name of the branch
     * @return String the result of the revert.
     * @throws CommitException if the content cannot be read nor updated
     */
    public String launchRevert(int commitID, int projectID, String branchName) throws CommitException {
        String updatedCode;
        try {
            int branchID = branchDB.getBranchID(branchName, projectID);
            this.updateMetadataReverted(commitID);
            updatedCode = this.revertTIKZCode(commitID);

            deleteForwardCommits(commitID, projectID, branchID);
        } catch (DatabaseException | VersioningException | IOException | DataAccessException | InvalidDriverException e) {
            throw new CommitException(e);
        }
        return updatedCode;
    }

    /**
     * Deletes the commits after the revert in the database
     * @param commitID last commit after method called
     * @param projectID the project id of the project
     * @param branchID the branch id where the revert is made
     * @throws DatabaseException if reading the database gives an error
     * @throws IOException if reading the branch metadata file gives an error
     * @throws DataAccessException if reading data gives an error
     * @throws InvalidDriverException
     */
    private void deleteForwardCommits(int commitID, int projectID, int branchID) throws DatabaseException, IOException, DataAccessException, InvalidDriverException {
        Branch branch = new BranchManager().getBranch(projectID, branchID);
        CommitDB commitDB = new CommitDB();
        for(Commit commit: branch.getCommits()){
            if(commit.getCommitID() > commitID) {
                commitDB.deleteCommit(projectID, branchID, commit.getCommitID());
            }
            }
    }

    /**
     * Reverts the TikZ code to a previous commit version
     * @param commitID the id of the commit we want to revert to
     * @throws VersioningException if the commit is invalid
     * @return String - the reverted code.
     */
    protected String revertTIKZCode(int commitID) throws VersioningException {
        try {
            VersionTextBuilder versionTextBuilder = new VersionTextBuilder(this.master.getPathToMetaData());
            String revertedTIKZ = versionTextBuilder.getTextFrom(commitID);
            FileWriter writer = new FileWriter(this.master.getFilePath(), false);
            writer.write(revertedTIKZ);
            writer.close();
            return versionTextBuilder.getTextFrom(commitID);
        } catch (IOException | DatabaseConnectionException e) {
            throw new VersioningException(e);
        }
    }

    /**
     * Changes the content of the metadataFile to a previous commit version
     * @param commitID the id of the commit we want to revert to
     * @throws IOException if the branch metadata file is unreadable
     */
    protected void updateMetadataReverted(int commitID) throws IOException {
        String newMetadataContent = "";
        String endingLine = "commit " + (commitID+1);
        BufferedReader reader = new BufferedReader(new FileReader(this.master.getPathToMetaData()));
        String line = reader.readLine();
        while (line != null) {
            if (!(line.equals(endingLine))) {
                newMetadataContent += line;
                newMetadataContent += "\n";
            }
            else
                break;
            line = reader.readLine();
        }
        reader.close();
        FileWriter writer = new FileWriter(this.master.getPathToMetaData(), false);
        writer.write(newMetadataContent);
        writer.close();
    }

    /**
     * Sets the master file only used for tests
     * @param project the project to revert to
     */
    protected void setFileInfo(Project project) {
        this.master = project;
    }

    public List<Integer> getCommits(String branchName) throws CommitException {
        Integer branchIdSelected;
        try {
            CommitDB commitDB = new CommitDB();
            branchIdSelected = branchDB.getBranchID(branchName, projectID);
            return commitDB.getCommits(this.projectID, branchIdSelected);
        } catch (DatabaseException | ValueNotFoundException e) {
            throw new CommitException(e);
        }
    }
}
