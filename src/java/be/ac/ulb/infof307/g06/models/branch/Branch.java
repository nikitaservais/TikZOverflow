package be.ac.ulb.infof307.g06.models.branch;

import be.ac.ulb.infof307.g06.exceptions.DataAccessException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.models.commit.Commit;
import be.ac.ulb.infof307.g06.models.commit.CommitManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gives every data about a branch in a structured and testable structure
 */
public class Branch {
    private int branchID;
    private int projectID;
    private String branchName;
    private String creationDate;
    private Boolean status;
    private List<Commit> commits;

    /**
     * Constructor of the class
     * @param projectID the project ID
     * @param branchID the branch ID
     * @param branchName the branch name
     * @param creationDate the date of the creation of the branch
     * @param status defines whether if the branch is opened or closed
     */
    public Branch(Integer projectID, Integer branchID, String branchName, String creationDate, Integer status) {
        this.branchID = branchID;
        this.projectID = projectID;
        this.branchName = branchName;
        this.creationDate = creationDate;
        this.status = (status != 0);
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public int getBranchID() {
        return branchID;
    }

    public int getProjectID() {
        return projectID;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * Converting a branch to String gives the name of the branch
     * @return the name of the branch
     */
    @Override
    public String toString() {
        return branchName;
    }

    /**
     * Clones the branch into a new one
     * @return the cloned branch
     */
    @Override
    public Branch clone(){
        Branch clone = new Branch(projectID,branchID,branchName,creationDate,1);
        clone.setStatus(status);
        ArrayList<Commit> clonedCommit = new ArrayList<>();
        for(Commit commit : commits){
            clonedCommit.add(commit.clone());
        }
        clone.setCommits(clonedCommit);
        return clone;
    }

    /**
     * Update the commits content in the branch
     */
    public void updateCommits() throws IOException, DataAccessException, DatabaseConnectionException {
        setCommits(new CommitManager().getAllCommits(this));
    }
}
