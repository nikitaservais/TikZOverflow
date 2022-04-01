package be.ac.ulb.infof307.g06.models.commit;

import be.ac.ulb.infof307.g06.models.database.*;
import be.ac.ulb.infof307.g06.exceptions.CommitException;
import be.ac.ulb.infof307.g06.exceptions.DatabaseException;
import be.ac.ulb.infof307.g06.exceptions.InvalidDriverException;
import be.ac.ulb.infof307.g06.models.commit.CommitHistoryModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TestCommitHistoryModel {
    private UserDB userDB;
    private ProjectDB projectDB;
    private BranchDB branchDB;
    private CommitDB commitDB;
    private CommitHistoryModel commitHistoryModel;
    private int projectID;
    private int userID;

    @BeforeEach
    void setUp() throws DatabaseException {
        userDB = new UserDB();
        projectDB = new ProjectDB();
        branchDB = new BranchDB();
        commitDB = new CommitDB();
        userID = userDB.createUser("gandalf", "le", "gris", "adresse@email.com", "YouShallNotPass");
        projectID = projectDB.insertProject(userID, "test", "test.tex", "09df3bc50707273be56ac901e534230a");
    }

    @AfterEach
    void tearDown() throws DatabaseException {
        projectDB.deleteProject(projectID);
        userDB.deleteUserWithID(userID);
    }

    @Test
    void getBranchOnActiveBranchReturnsInfo() throws DatabaseException, CommitException {
        int branchID1 = branchDB.insertBranch(projectID, "BranchOne");
        commitDB.insertCommit(projectID, branchID1, "one");
        CommitHistoryModel commitHistoryModel = new CommitHistoryModel(projectID);
        ArrayList<ArrayList<String>> emptyList = new ArrayList<>();
        assertNotEquals(emptyList, commitHistoryModel.getBranchInfo(branchID1));
    }

    @Test
    void getBranchOnInactiveBranchReturnsNull() throws DatabaseException, CommitException {
        int branchID1 = branchDB.insertBranch(projectID, "BranchOne");
        branchDB.insertBranch(projectID, "BranchTwo");
        branchDB.updateBranchStatus(projectID, branchID1, 0);
        CommitHistoryModel commitHistoryModel = new CommitHistoryModel(projectID);
        assertEquals(null, commitHistoryModel.getBranchInfo(branchID1));
    }

    @Test
    void getBranchOnEmptyBranchReturnsEmptyArray() throws CommitException, DatabaseException {
        int branchID1 = branchDB.insertBranch(projectID, "BranchOne");
        CommitHistoryModel commitHistoryModel = new CommitHistoryModel(projectID);
        ArrayList<ArrayList<String>> emptyList = new ArrayList<>();
        assertEquals(emptyList, commitHistoryModel.getBranchInfo(branchID1));
    }

    @Test
    void getBranchOnBranchWithACommitReturnsCorrectInfos() throws DatabaseException, CommitException, InvalidDriverException {
        int branchID1 = branchDB.insertBranch(projectID, "BranchOne");
        int commitID1 = commitDB.insertCommit(projectID, branchID1, "one");
        CommitHistoryModel commitHistoryModel = new CommitHistoryModel(projectID);
        String[] commitData = commitDB.getCommitData(projectID, branchID1, commitID1);
        ArrayList<String> commit = new ArrayList<>();
        commit.addAll(Arrays.asList(commitData));     //Transform a String[] to an ArrayList<String>
        assertEquals(commit, commitHistoryModel.getBranchInfo(branchID1).get(0));
    }
}