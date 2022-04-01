package be.ac.ulb.infof307.g06.models.commit;

import be.ac.ulb.infof307.g06.utils.ConstantsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Provides the intermediate between the View/Controller
 * and the way that the metadata file is written/read
 */
public class CommitModel {
    private String commitContent;
    private ArrayList<String> newLines;
    private ArrayList<String> removedLines;
    private String commitNumber;

    /**
     * Constructor
     * @param commitContent the initial commited lines (added and removed)
     */
    public CommitModel(String commitContent) {
        this.commitContent = commitContent;
        if (!commitContent.equals("")) {
            parseCommitContent();
        }
    }

    /**
     * Parses the commitContent string and sorts the
     * lines whether they are new ones or they have been removed
     * from the file.
     */
    private void parseCommitContent(){
        newLines = new ArrayList<>();
        removedLines = new ArrayList<>();

        List<String> commitLines = Arrays.asList(commitContent.split("\n"));
        commitNumber = commitLines.get(0).split(" ")[1];

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
     * @return a list with all the new lines that have been added in the
     * last commit
     */
    public ArrayList<String> getNewLines(){
        return newLines;
    }

    /**
     * @return a list with all the removed lines from the last commit
     */
    public ArrayList<String> getRemovedLines(){
        return removedLines;
    }

    /**
     * @return the commit number/identifier
     */
    public String getCommitNumber(){
        return commitNumber;
    }

    public String getCommitContent() { return commitContent; }

}
