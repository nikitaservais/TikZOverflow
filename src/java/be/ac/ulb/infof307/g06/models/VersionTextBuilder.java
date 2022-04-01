package be.ac.ulb.infof307.g06.models;

import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.models.commit.CommitManager;
import be.ac.ulb.infof307.g06.models.metadata.MetaDataReader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * builds a plain text from metaData file : commits, merges, branches, ...
 */
public class VersionTextBuilder {
    private MetaDataReader metaDataReader;
    private CommitManager commitManager;

    /**
     * constructor setting the class attributes
     * @param path to metaData file
     */
    public VersionTextBuilder(String path) throws DatabaseConnectionException {
        this.metaDataReader = new MetaDataReader(path);
        this.commitManager = new CommitManager();
    }

    /**
     * builds the text from commit 0 to specified commit
     * @param versionID last commit taken into account
     * @return complete text until the specified commit
     */
    public String getTextFrom(int versionID) throws IOException {
        ArrayList<String> text = new ArrayList<String>();
        String commitText;
        ArrayList<String> removedLines;

        for(int i = 0; i <= versionID; i++) {
            commitText = metaDataReader.getCommitFromID(i).getCommitContent();
            if (!commitText.equals(ConstantsUtils.emptyString)) {
                commitManager.parseCommitContent(commitText);
                ArrayList<String> newLines = commitManager.getNewLines();
                text.addAll(newLines);
                removedLines = commitManager.getRemovedLines();
                for (int lineToRemove = 0; lineToRemove < removedLines.size(); lineToRemove++) {
                    for (int line = 0; line < text.size(); line++) {
                        // if the line to delete is found
                        if (text.get(line).equals(removedLines.get(lineToRemove))) {
                            text.remove(line);
                            break;
                        }
                    }
                }
            }
        }
        return this.listToString(text);
    }

    /**
     * converts an ArrayList to a string with all elements concatenated
     * @param list of strings
     * @return string with all elements concatenated
     */
    private String listToString(ArrayList<String> list) {
        StringBuilder finalText = new StringBuilder();
        for(int i = 0; i < list.size(); i++) {
            finalText.append(list.get(i));
            if(i < list.size()-1) finalText.append("\n"); //prevent newline for the last instruction
        }
        return finalText.toString();
    }
}
