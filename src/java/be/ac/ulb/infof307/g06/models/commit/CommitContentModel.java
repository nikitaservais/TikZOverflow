package be.ac.ulb.infof307.g06.models.commit;

import be.ac.ulb.infof307.g06.models.database.BranchDB;
import be.ac.ulb.infof307.g06.models.database.CommitDB;
import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.models.branch.Branch;
import be.ac.ulb.infof307.g06.utils.StringCompareUtils;
import be.ac.ulb.infof307.g06.models.metadata.MetaDataReader;
import be.ac.ulb.infof307.g06.models.VersionTextBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CommitContentModel class to get the data
 */
public class CommitContentModel {
    private CommitModel commitModel;
    private Project project;
    private int projectID;
    private BranchDB branchDB;

    public CommitContentModel(Project project) throws DatabaseConnectionException {
        this.project = project;
        this.projectID = project.getId();
        this.branchDB = new BranchDB();
    }

    /**
     * Gets a model of data containing the commit content
     *
     * @param projectID
     * @param branchID
     * @param commitID
     * @return commitModel
     */
    public CommitModel getCommitContent(Integer projectID, Integer branchID, Integer commitID) throws VersioningException {
        MetaDataReader metaDataReader;
        try {
            ProjectDB projectDB = new ProjectDB();
            metaDataReader = new MetaDataReader(projectDB.getMetadataPath(projectID, branchID));
        } catch (DatabaseException e) {
            throw new VersioningException(e);
        }
        try {
            commitModel = metaDataReader.getCommitFromID(commitID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commitModel;
    }

    /**
     * @param commitA   ID of the commit we are starting from
     * @param commitB   ID of the commit we want to get to
     * @param projectID ID of the current Project
     * @param branchID  ID of the current branch
     * @return textDifferences an array containing the common lines[0], the new lines[1]
     * and the removed ones[2] between commit A and B
     */
    public ArrayList<ArrayList<String>> getDifferencesBetweenTwoCommits(int commitA, int commitB, Integer projectID, Integer branchID) throws VersioningException {
        String commitPath;
        try {
            ProjectDB projectDB = new ProjectDB();
            commitPath = projectDB.getMetadataPath(projectID, branchID);
        } catch (DatabaseException e) {
            throw new VersioningException(e);
        }
        String commitAText;
        String commitBText;
        try {
            VersionTextBuilder versionTextBuilder = new VersionTextBuilder(commitPath);
            commitBText = versionTextBuilder.getTextFrom(commitB);
            commitAText = versionTextBuilder.getTextFrom(commitA);
        } catch (IOException | DatabaseConnectionException e) {
            throw new VersioningException(e);
        }
        ArrayList<String> commonLines = StringCompareUtils.getCommon(commitAText, commitBText); //common lines between commit A and B
        ArrayList<String> newLines = StringCompareUtils.getInsertions(commitAText, commitBText); //New lines between commit A to B
        ArrayList<String> removedLines = StringCompareUtils.getDeletions(commitAText, commitBText); //Removed lines between commit A to B
        ArrayList<ArrayList<String>> textDifferences = new ArrayList<ArrayList<String>>();
        textDifferences.add(commonLines);
        textDifferences.add(newLines);
        textDifferences.add(removedLines);
        return textDifferences;
    }

    /**
     * Gets the new lines added from the commit
     *
     * @return newLines
     */
    public ArrayList<String> getNewLines() {
        return commitModel.getNewLines();
    }

    /**
     * Gets the removed lines from the commit
     *
     * @return removedLines
     */
    public ArrayList<String> getRemovedLines() {
        return commitModel.getRemovedLines();
    }

    public int getBranchID(String branchName) throws VersioningException {
        try {
            return this.branchDB.getBranchID(branchName, projectID);
        } catch (DatabaseException e) {
            throw new VersioningException(e);
        }
    }

    public List<String> getBranches() {
        List<String> branchesNames = new ArrayList<>();
        for (Branch branch : project.getBranches()){
            branchesNames.add(branch.getBranchName());
        }
        return branchesNames;
}

    public List<Integer> getCommits(String branchName) throws VersioningException {
        try {
            CommitDB commitDB = new CommitDB();
            Integer branchIdSelected = branchDB.getBranchID(branchName, projectID);
            return commitDB.getCommits(projectID, branchIdSelected);
        } catch (DatabaseException | ValueNotFoundException e) {
            throw new VersioningException(e);
        }
    }
}
