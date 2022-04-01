package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.models.user.User;
import be.ac.ulb.infof307.g06.exceptions.DataAccessException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseConnectionException;
import be.ac.ulb.infof307.g06.utils.FileUtils;
import be.ac.ulb.infof307.g06.models.branch.Branch;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * represents a Project with its full information
 */
public class Project {
    private int id;
    private String filePath;
    private String name;
    private User user;
    private String lastModification;
    private String pathToMetaData;
    private int currentBranch;
    private List<Branch> branches;
    private String content;

    public Project() {
    }

    /**
     * Constructor of Project class.
     * @param name             string of Project name
     * @param user             string of owner name
     * @param lastModification string last modification made on Project
     * @param filePath         path to file with plain text
     * @param id               id of the Project
     */
    public Project(String name, User user, String lastModification, String filePath, int id) {
        this.name = name;
        this.lastModification = lastModification;
        this.user = user;
        this.filePath = filePath;
        this.id = id;
        currentBranch = 0;
    }

    /**
     * constructor with full attributes; this one should be used all the time now
     * @param name             string of Project name
     * @param user             string of owner name
     * @param lastModification string last modification made on Project
     * @param filePath         path to file with plain text
     * @param id               id of the Project
     * @param pathToMetaData   path to metadata file
     * @param currentBranch    id of the branch; 0 is master
     */
    public Project(String name, User user, String lastModification, String filePath, int id, String pathToMetaData, int currentBranch) {
        this.name = name;
        this.lastModification = lastModification;
        this.user = user;
        this.filePath = filePath;
        this.id = id;
        this.currentBranch = currentBranch;
        this.pathToMetaData = pathToMetaData;
    }


    /**
     * returns the text contained at the location specified by filePath attribute
     * @return the text contained at the location specified by filePath attribute
     * @throws IOException
     */
    public String getText() throws IOException {
        return FileUtils.fileToString(filePath);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * provides all the branch of the project
     * @return list of all project branches
     */
    public List<Branch> getBranches() {
        ArrayList<Branch> clonedRes = new ArrayList<>();
        for(Branch branch : branches){
            clonedRes.add(branch.clone());
        }
        return clonedRes;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLastModification() {
        return lastModification;
    }

    public void setLastModification(String lastModification) {
        this.lastModification = lastModification;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPathToMetaData() {
        return pathToMetaData;
    }

    public void setPathToMetaData(String pathToMetaData) {
        this.pathToMetaData = pathToMetaData;
    }

    public int getCurrentBranch() {
        return currentBranch;
    }

    public void setCurrentBranch(int currentBranch) {
        this.currentBranch = currentBranch;
    }

    /**
     * adds a new branch to the project
     * @param branch new branch created
     */
    public void addNewBranch(Branch branch){
        branches.add(branch);
    }

    /**
     * update the commit content for all branches
     * @throws IOException
     * @throws DataAccessException
     * @throws DatabaseConnectionException
     */
    public void updateCommits() throws IOException, DataAccessException, DatabaseConnectionException {
        for(Branch branch : branches){
            branch.updateCommits();
        }
    }

    /**
     * verifies of the given project is the project
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Project otherProject = (Project) obj;
        return otherProject.getId() == id;
    }
}
