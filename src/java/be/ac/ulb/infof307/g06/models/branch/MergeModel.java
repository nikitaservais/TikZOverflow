package be.ac.ulb.infof307.g06.models.branch;

import be.ac.ulb.infof307.g06.models.database.CommitDB;
import be.ac.ulb.infof307.g06.exceptions.CommitException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.models.project.Project;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.models.VersionTextBuilder;
import be.ac.ulb.infof307.g06.models.commit.Commit;
import be.ac.ulb.infof307.g06.models.commit.CommitModel;
import be.ac.ulb.infof307.g06.models.metadata.MetaDataReader;
import be.ac.ulb.infof307.g06.models.metadata.MetaDataWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Merges one Project into another; protected methods are for test purposes only
 */
public class MergeModel {
    private Project master;
    private Project branch;
    private String masterMetaData;
    private String branchMetaData;
    private ArrayList<String> linesToAddToMaster = null;
    private ArrayList<String> linesToRemoveFromMaster = null;

    /**
     * Constructor that will launch the merge between the two specified projects
     * @param master
     * @param branch
     * @throws IOException
     */
    public MergeModel(Project master, Project branch) throws IOException {
        this.master = master;
        this.branch = branch;

        this.mergeProjects();
    }

    /**
     * Constructor for tests
     */
    protected MergeModel() {}

    /**
     * prepares the merge between the two projects in attributes but do not start the merge
     * this is make like this so we can extract the information about the incoming merge
     * @throws IOException
     */
    protected void mergeProjects() throws IOException {
        this.masterMetaData = master.getPathToMetaData();
        this.branchMetaData = branch.getPathToMetaData();

        String masterContent = master.getText(); // contains all the text of last master version

        ArrayList<ArrayList<String>> branchLines = getModifiedLines(branchMetaData);
        ArrayList<String> branchNewLines = branchLines.get(0);
        ArrayList<String> branchRemovedLines = branchLines.get(1);

        linesToAddToMaster = this.verifyPresenceInText(masterContent,branchNewLines,false);
        linesToRemoveFromMaster = this.verifyPresenceInText(masterContent,branchRemovedLines,true);
    }


    /**
     * @return returns the lines added and deleted during the merge process
     */
    public ArrayList<ArrayList<String>> getDeletedAndInsertedLines(){
        ArrayList<ArrayList<String>> sol = new ArrayList<>();
        sol.add((ArrayList<String>)linesToRemoveFromMaster.clone());
        sol.add((ArrayList<String>) linesToAddToMaster.clone());
        return sol;
    }

    /**
     * Updates the content of all files database, metadata,...
     * @throws CommitException
     */
    public void update() throws CommitException {
        try {
            if(linesToAddToMaster == null || linesToRemoveFromMaster == null) throw new IOException("Error merge lines.");
            this.updateMasterMetaData(linesToAddToMaster,linesToRemoveFromMaster);
            this.updateMasterFile();
            this.updateDatabase();
        } catch (DatabaseException | IOException e) {
            throw new CommitException(e);
        }
    }

     /**
     * Adds the commit/merge to master metaData file
     * @param linesToAdd lines that will be added to master
     * @param linesToRemove lines that will be removed from master
     * @throws IOException
     */
     protected void updateMasterMetaData(ArrayList<String> linesToAdd, ArrayList<String> linesToRemove) throws IOException {
         Commit commit = new Commit();
         commit.addNewLines(linesToAdd);
         commit.addRemovedLines(linesToRemove);

         MetaDataReader metaDataReader = new MetaDataReader(masterMetaData);
         int lastCommitID = metaDataReader.getLastCommitID();

         String commitText = commit.toString(lastCommitID+1); // the commit text with correct commit id
         MetaDataWriter metaDataWriter = new MetaDataWriter(masterMetaData);
         metaDataWriter.writeCommit(commitText);
    }

    /**
     * Updates the real master file with the new text after the merge is done
     * @throws IOException
     * @throws DatabaseConnectionException
     */
    protected void updateMasterFile() throws IOException, DatabaseConnectionException {
        MetaDataReader metaDataReader = new MetaDataReader(masterMetaData);
        int lastCommitID = metaDataReader.getLastCommitID();

        String newTextToStorage = new VersionTextBuilder(masterMetaData).getTextFrom(lastCommitID) + "\n";
        // \n is there because of FileWriter.write

        FileWriter fileWriter = new FileWriter(master.getFilePath(),false);
        fileWriter.write(newTextToStorage);
        fileWriter.close();
    }

    /**
     * Update the database with a new merge commit.
     * @throws DatabaseException
     */
    protected void updateDatabase() throws DatabaseException {
        CommitDB commitDB = new CommitDB();
        commitDB.insertCommit(master.getId(), master.getCurrentBranch(), ConstantsUtils.mergeString);
    }

    /**
     * Checks if the line is or is not is specified text, this is used to know if we have to add (or not) a line and
     * to remove (or not) a line from  text
     * @param text to check is a line's matching or not
     * @param toCheck line to match with text
     * @param inText determine if we want that the line is in text or not in text
     * @return line that to add or removed from specified text
     */
    protected ArrayList<String> verifyPresenceInText(String text, ArrayList<String> toCheck, Boolean inText) {
        ArrayList<String> lineToReturn = new ArrayList<>();
        for(int i = 0; i < toCheck.size(); i++){
            if(!inText) {
                if(!text.contains(toCheck.get(i)))
                    lineToReturn.add(toCheck.get(i));
            }
            else {
                if (text.contains(toCheck.get(i)))
                    lineToReturn.add(toCheck.get(i));
            }
        }
        return lineToReturn;
    }


    /**
     * Gets all the modified lines (basic all lines) of the lines
     * @param path the path to the metadata file.
     * @return ArrayList that contains ArrayList of new and removed lines
     * @throws IOException
     */
    protected ArrayList<ArrayList<String>> getModifiedLines(String path) throws IOException {
        MetaDataReader metaDataReader = new MetaDataReader(path);
        ArrayList<String> newLines = new ArrayList<>();
        ArrayList<String> removedLines = new ArrayList<>();
        for(int i = 1; i <= metaDataReader.getLastCommitID(); i++) {
            CommitModel currentCommit = metaDataReader.getCommitFromID(i);
            newLines.addAll(currentCommit.getNewLines());
            removedLines.addAll(currentCommit.getRemovedLines());
        }
        return new ArrayList<>(Arrays.asList(newLines,removedLines));
    }

    public void setMasterMetaData(String masterMetaData) {
        this.masterMetaData = masterMetaData;
    }

    public void setMaster(Project master) {
        this.master = master;
    }

    public void setBranch(Project branch) {
        this.branch = branch;
    }
}