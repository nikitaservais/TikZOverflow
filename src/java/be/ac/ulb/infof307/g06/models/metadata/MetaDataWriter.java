package be.ac.ulb.infof307.g06.models.metadata;

import be.ac.ulb.infof307.g06.utils.ConstantsUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes metadata in file, it verifies that the data is in correct form before writing it
 */
public class MetaDataWriter {
    private File metaData;

    /**
     * Constructor protected for the test
     */
    protected MetaDataWriter() {
        metaData = null;
    }

    public MetaDataWriter(String path) {
        metaData = new File(path);
    }

    /**
     * Writes commit in metadata file
     * @param commit : commit to write
     * @throws IOException
     */
    public void writeCommit(String commit) throws IOException {
        if (verifyCommit(commit)) {
            FileWriter writer = new FileWriter(metaData, true);
            if (metaData.length() == 0) writer.write(commit);
            else writer.write("\n"+commit);
            writer.close();
        }
        else {
            throw new IOException();   //TODO : Better type of exception
        }
    }

    /**
     * Verifies if the commit is in the correct format
     * @param commit : commit to verify the format
     * @return true if format is correct, false otherwise
     */
    protected boolean verifyCommit(String commit) {
        boolean res = true;
        boolean flagAdd = true;
        boolean flagRemove = false;
        int i = 1;
        String[] commitToVerify = commit.split("\n");
        String[] firstLine = commitToVerify[0].split(" ");
        if (!firstLine[0].equals(ConstantsUtils.commitString)) res = false;
        try {
            if (!firstLine[1].matches("[0-9]+")) res = false;
        }
        catch (IndexOutOfBoundsException e) {
            res = false;
            i = commitToVerify.length;
        }
        while (commitToVerify.length > i) {
            if (commitToVerify[i].indexOf('\t') != 0) {
                res = false;
                i = commitToVerify.length;
            }
            else {
                char addOrRemove = commitToVerify[i].charAt(1);
                if (addOrRemove == ConstantsUtils.removedLineChar && !flagRemove) {
                    flagRemove = true;
                    flagAdd = false;
                }
                else if ((addOrRemove != ConstantsUtils.addedLineChar && addOrRemove != ConstantsUtils.removedLineChar) ||
                        (addOrRemove == ConstantsUtils.addedLineChar && !flagAdd)) {
                    res = false;
                    i = commitToVerify.length;
                }
                i++;
            }
        }
        return res;
    }


    /**
     * Verifies that a branch, merge or commit statement is in the correct format
     * @param statement : the Statement to verify the format (branch, merge or revert)
     * @return true if the statement is in the correct format, false otherwise
     */
    protected boolean verifyStatement(String statement) {
        boolean res = true;
        String[] branchToVerify = statement.split(" ");
        if (branchToVerify.length != 4) res = false;
        else {
            if (branchToVerify[0].equals(ConstantsUtils.branchedString)) {
                if (!branchToVerify[2].equals("from")) res = false;
            }
            else if (branchToVerify[0].equals(ConstantsUtils.mergedString) || branchToVerify[0].equals(ConstantsUtils.revertedString)) {
                if (!branchToVerify[2].equals("to")) res = false;
                if (branchToVerify[0].equals(ConstantsUtils.revertedString) &&
                        !branchToVerify[3].substring(0, branchToVerify[3].length()-1).matches("[0-9]+")) {
                    res = false;
                }
            }
            else res = false;
            if (statement.charAt(statement.length()-1) != ';') {
                res = false;
            }
        }
        return res;
    }

    /**
     * Deletes the file used by MetaDataWriter; this has no use except in tests
     */
    public void eraseMetaDataFile() {
        if (metaData.exists()) {
            metaData.delete();
        }
    }

    /**
     * Provides the metaDataFile for external usage
     * @return metaData file
     */
    public File getMetaData() {
        return metaData;
    }
}
