package be.ac.ulb.infof307.g06.models.commit;

import be.ac.ulb.infof307.g06.utils.ConstantsUtils;

import java.util.ArrayList;

/**
 * Implements the metadata of a commit
 */
public class Commit {
    protected int projectID;
    protected int branchID;
    protected int commitID;
    protected String message;
    protected String date;
    protected ArrayList<String> newLines;
    protected ArrayList<String> removedLines;

    public Commit() {
        newLines = new ArrayList<>();
        removedLines = new ArrayList<>();
    }

    /**
     * Constructors used for
     * @param projectID the project ID
     * @param branchID the branch ID
     * @param newLines the lines added by the commit
     * @param removedLines the lines removed by commit
     */
    public Commit(int projectID, int branchID, ArrayList<String> newLines, ArrayList<String> removedLines) {
        this.projectID = projectID;
        this.branchID = branchID;
        this.newLines = newLines;
        this.removedLines = removedLines;
    }
    /**
     * Constructor used in display history commit to display the commit info in the table.
     * @param commitDate The date and time of the commit.
     * @param commitMessage The message of the commit.
     */
    public Commit(String commitDate, String commitMessage){
        this.date = commitDate;
        this.message = commitMessage;
    }


    /**
     * Builds the commit text that will be added to the metaData file with a precise syntax
     * full syntax is detailed in team/metadata.md
     * @return commit text with correct syntax
     */
    public String toString() {
        return this.toString(this.getCommitID());
    }


    /**
     * Polymorphism of toString() method, except we specify the commit ID we want to write
     * @param commitID id of the commit
     * @return commit text with correct syntax and specified commit id
     */
    public String toString(int commitID) {
        String asString = ConstantsUtils.commitString + " " + commitID + "\n";
        for(int i = 0; i < newLines.size(); i++)
            asString += "\t" + ConstantsUtils.addedLineChar + newLines.get(i) + "\n";
        for(int i = 0; i < removedLines.size(); i++)
            asString += "\t" + ConstantsUtils.removedLineChar + removedLines.get(i) + "\n";
        return asString;
    }

    /**
     * @return the Project identifier
     */
    public int getProjectID(){
        return projectID;
    }

    /**
     * @return the branch identifier
     */
    public int getBranchID(){
        return branchID;
    }

    /**
     * @return the commit identifier
     */
    public int getCommitID(){
        return commitID;
    }

    /**
     * Updates commitID after insert into the database
     * @param commitID
     */
    public void setCommitID(int commitID) {this.commitID = commitID; }

    /**
     * @return message of the commit
     */
    public String getMessage() { return message; }

    /**
     * Updates message of the commit after the user entered it
     * @param message
     */
    public void setMessage(String message) { this.message = message; }

    /**
     * @return a list with all the new lines that have been added in the
     * last commit
     */
    public ArrayList<String> getNewLines(){
        return (ArrayList<String>) newLines.clone();
    }

    /**
     * @return a list with all the removed lines from the last commit
     */
    public ArrayList<String> getRemovedLines(){
        return (ArrayList<String>)removedLines.clone();
    }

    /**
     * Returns the date of the commit.
     * @return the date of the commit.
     */
    public String getDate(){
        return this.date;
    }

    /**
     * Adds the line in parameter to newLines
     * @param newLine line to add to newLines
     */
    public void addNewLine(String newLine) {
        newLines.add(newLine);
    }

    /**
     * Adds the line in parameter to removedLines
     * @param removedLine line to add to removedLines
     */
    public void addRemovedLine(String removedLine) {
        removedLines.add(removedLine);
    }

    /**
     * Adds new lines from a container; this allows to add more than one line at the time
     * @param newLines to add
     */
    public void addNewLines(ArrayList<String> newLines) {
        this.newLines.addAll(newLines);
    }

    /**
     * Adds removed lines from a container; this allows to add more than one line at the time
     * @param removedLines to add
     */
    public void addRemovedLines(ArrayList<String> removedLines) {
        this.removedLines.addAll(removedLines);
    }

    public void setRemovedLines(ArrayList<String> removedLines) {
        this.removedLines = removedLines;
    }

    public void setNewLines(ArrayList<String> newLines) {
        this.newLines = newLines;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    @Override
    public Commit clone(){
        return new Commit(projectID,branchID,getNewLines(),getRemovedLines());
    }

}
