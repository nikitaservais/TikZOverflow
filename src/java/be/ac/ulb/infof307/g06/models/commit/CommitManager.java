package be.ac.ulb.infof307.g06.models.commit;

import be.ac.ulb.infof307.g06.models.database.CommitDB;
import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.models.branch.Branch;
import be.ac.ulb.infof307.g06.models.metadata.MetaDataReader;
import be.ac.ulb.infof307.g06.models.metadata.MetaDataWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * allows to create a commit or display the content of a commit
 * provides the intermediate layer between CreateCommit View/Controller
 * and the way that the metadata file is written/read
 */
public class CommitManager extends Commit {
    CommitDB commitDB;

    /**
     * Constructor used to get a commit content
     * @throws DatabaseConnectionException if the database can't be reached
     */
    public CommitManager() throws DatabaseConnectionException {
        super(-1,-1, new ArrayList<>(), new ArrayList<>());
        this.commitDB = new CommitDB();
    }

    /**
     * Constructor used to insert a new commit
     * @param projectID
     * @param branchID
     * @param newLines
     * @param removedLines
     */
    public CommitManager(int projectID, int branchID, ArrayList<String> newLines, ArrayList<String> removedLines) throws DatabaseConnectionException {
        super(projectID, branchID, newLines, removedLines); //creates a commit object
        this.commitDB = new CommitDB();
    }

    /**
     * Inserts a new commit into the database and the metaData file
     * @param message of the commit
     * @return commitID
     * @throws CommitException
     */
    public int createNewCommit(String message) throws CommitException {
        this.setMessage(message);
        try {
            commitDB.insertCommit(projectID, branchID, message);
        } catch (DatabaseException e) {
            throw new CommitException(e);
        }
        commitID = new MetaDataReader(this.getMetaDataFileName()).getLastCommitID()+1;

        String structuredCommitForStorage = toString();

        MetaDataWriter metaDataWriter = new MetaDataWriter(this.getMetaDataFileName());
        try {
            metaDataWriter.writeCommit(structuredCommitForStorage);
        }
        catch (java.io.IOException e) {
            throw new CommitException(e);
        }
        return commitID;
    }

    /**
     * Gets the metadata file name
     * @return the name of the metadata file name
     * @throws CommitException if the branch metadata file cannot be read
     */
    private String getMetaDataFileName() throws CommitException {
        try {
            ProjectDB projectDB = new ProjectDB();
            String path = projectDB.getProjectPath(this.projectID);
            String name = projectDB.getProjectName(this.projectID);
            String metaDataPath = path.split(name)[0];
            return metaDataPath + File.separator + ConstantsUtils.projectExtensionString + projectID + ConstantsUtils.branchFileString + branchID;
        } catch (DatabaseException | InvalidDriverException e) {
            throw new CommitException(e);
        }
    }


    /**
     * This method parses the commitContent string and sorts the
     * lines whether they are new ones or they have been removed
     * from the file.
     * @param structuredCommitForStorage to parse
     */
    public void parseCommitContent(String structuredCommitForStorage){
        newLines = new ArrayList<>();
        removedLines = new ArrayList<>();

        List<String> commitLines = Arrays.asList(structuredCommitForStorage.split("\n"));
        commitID = Integer.parseInt(commitLines.get(0).split(" ")[1]);

        for (Iterator<String> i = commitLines.iterator(); i.hasNext();) {
            String line = i.next();
            if(line.charAt(1) == ConstantsUtils.addedLineChar){
                // We don't want the + so we just add everything afterwards
                newLines.add(line.substring(line.indexOf(ConstantsUtils.addedLineChar) + 1));
            } else if (line.charAt(1) == ConstantsUtils.removedLineChar){
                removedLines.add(line.substring(line.indexOf(ConstantsUtils.removedLineChar) + 1));
            }
        }
    }

    /**
     * Searches for all the commits of a branch
     * @param branch the branch to get the commits of
     * @return a list containing the branches
     * @throws IOException if the metadata file can't be read
     * @throws DataAccessException if the database can't be reached
     */
    public List<Commit> getAllCommits(Branch branch) throws IOException, DataAccessException {
        List<Integer> commitsId;
        try {
            commitsId = commitDB.getCommits(branch.getProjectID(), branch.getBranchID());
        } catch (DatabaseException | ValueNotFoundException e) {
            throw new DataAccessException(e);
        }
        List<Commit> commits = new ArrayList<>();
        for (Integer id : commitsId) {
            MetaDataReader metaDataReader;
            try {
                ProjectDB projectDB = new ProjectDB();
                metaDataReader = new MetaDataReader(projectDB.getMetadataPath(branch.getProjectID(), branch.getBranchID()));
            } catch (DatabaseException e) {
                throw new DataAccessException(e);
            }
            CommitModel commitModel = metaDataReader.getCommitFromID(id);
            Commit commit = new Commit();
            commit.setCommitID(id);
            commit.setNewLines(commitModel.getNewLines());
            commit.setRemovedLines(commitModel.getRemovedLines());
            commit.setProjectID(branch.getProjectID());
            commits.add(commit);
        }
        return commits;
    }
}