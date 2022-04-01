package be.ac.ulb.infof307.g06.models.metadata;

import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import be.ac.ulb.infof307.g06.models.commit.CommitModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Reads MetaData files and get information about commits made
 */
public class MetaDataReader {
    private String path;

    /**
     * Constructor
     * @param path the path to the metadata file
     */
    public MetaDataReader(String path) {
        this.path = path;
    }

    /**
     * Returns the content of a specified commit
     * @param ID : ID of the commit to find
     * @return : CommitModel which represents the commit with the correct ID
     * @throws IOException
     */
    public CommitModel getCommitFromID(int ID) throws IOException {
        String commitToStore = "";
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine();
        while (line != null) {
            String[] lineToCheck = line.split(" ");
            if (lineToCheck[0].equals(ConstantsUtils.commitString)) {
                if (Integer.parseInt(lineToCheck[1]) == ID) {
                    do {
                        commitToStore += line;
                        commitToStore += "\n";
                        line = reader.readLine();
                    } while (line != null && (line.length() < 7 || !line.substring(0, 6).equals(ConstantsUtils.commitString)));
                    // line.length() < 7 is there to avoid error if string is too short : it's not a commit line anyway
                    break;
                }
            }
            line = reader.readLine();
        }
        reader.close();
        return new CommitModel(commitToStore);
    }

    /**
     * Searches for the last commit ID
     * @return the the last commit ID
     */
    public Integer getLastCommitID() {
        Integer id = -1;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null) {
                String[] lineToCheck = line.split(" ");
                if (lineToCheck[0].equals(ConstantsUtils.commitString)) {
                    if (Integer.parseInt(lineToCheck[1]) > id) {
                        id = Integer.parseInt(lineToCheck[1]);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
