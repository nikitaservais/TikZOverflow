package be.ac.ulb.infof307.g06.models.commit;

import be.ac.ulb.infof307.g06.models.database.BranchDB;
import be.ac.ulb.infof307.g06.models.database.CommitDB;
import be.ac.ulb.infof307.g06.exceptions.CommitException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.exceptions.ValueNotFoundException;
import javafx.util.Pair;

import java.util.*;


/**
 * Creates a list with all the commits of a certain Project.
 * Contains a list of all the branch of the Project, followed by a pair composed of the identifier of the branch, and his content.
 * The content of the branch is all his commits, which is an array.
 * This array of all the commits, contains an array of strings with all the infos about this commit.
 */
public class CommitHistoryModel {
    private int projectID;
    private ArrayList<Pair<Integer, ArrayList<ArrayList<String>>>> commitHistory;

    /**
     * Constructor
     * @param projectID the project ID
     * @throws CommitException if the branch metadata file can't be read
     */
    public CommitHistoryModel(int projectID) throws CommitException {
        this.projectID = projectID;
        createCommitHistory();
    }

    /**
     * Gets the infos of all commits of all branches for one specific Project.
     */
    private void createCommitHistory() throws CommitException {
        commitHistory = new ArrayList<>();
        try {
            CommitDB commitDB = new CommitDB();
            BranchDB branchDB = new BranchDB();
            List<Integer> allActivesBranches = branchDB.getActiveBranches(projectID);
            Iterator<Integer> branchIterator = allActivesBranches.iterator();
            while(branchIterator.hasNext()) {  //All branches
                Integer branchID = branchIterator.next();
                List<Integer> allCommits = commitDB.getCommits(projectID, branchID);
                ArrayList<ArrayList<String>> thisBranchInfo = new ArrayList<>();       //Infos of this branch
                Iterator<Integer> commitIterator = allCommits.iterator();
                while (commitIterator.hasNext()) {   //All commits of a branch
                    Integer commitID = commitIterator.next();
                    String[] commitData = commitDB.getCommitData(projectID, branchID, commitID);
                    ArrayList<String> thisCommitHistory = new ArrayList<>();
                    thisCommitHistory.addAll(Arrays.asList(commitData));     //Transform a String[] to an ArrayList<String>
                    thisBranchInfo.add(thisCommitHistory);      //Add info of this commit
                }
                commitHistory.add(new Pair<>(branchID, thisBranchInfo));  //Add all infos of this branch
            }
        } catch (DatabaseException | ValueNotFoundException | InvalidDriverException e) {
            throw new CommitException(e);
        }
    }


    /**
     * Get the infos of a specific branch of a Project.
     * @param branchID branch number.
     * @return array with all the commits infos, if the branch doesn't exist, returns null.
     */
    public ArrayList<ArrayList<String>> getBranchInfo(Integer branchID) {
        ArrayList<ArrayList<String>> returnValue = null;
        Iterator<Pair<Integer, ArrayList<ArrayList<String>>>> projectIterator = commitHistory.iterator();
        while(projectIterator.hasNext()) {
            Pair<Integer, ArrayList<ArrayList<String>>> branch = projectIterator.next();
            if (branch.getKey() == branchID) {
                returnValue = branch.getValue();
                return returnValue;
            }
        }
        return returnValue;
    }
}



