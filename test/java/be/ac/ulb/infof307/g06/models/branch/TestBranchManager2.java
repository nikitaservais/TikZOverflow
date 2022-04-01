package be.ac.ulb.infof307.g06.models.branch;

import be.ac.ulb.infof307.g06.models.database.BranchDB;
import be.ac.ulb.infof307.g06.models.database.ProjectDB;
import be.ac.ulb.infof307.g06.exceptions.*;
import be.ac.ulb.infof307.g06.models.branch.Branch;
import be.ac.ulb.infof307.g06.models.branch.BranchManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestBranchManager2 {
    private BranchManager branchManager;
    BranchDB branchDB;
    ProjectDB projectDB;
    Integer projectId;
    private int branchID;
    List<Branch> branches;

    @BeforeAll
    public void setUp() throws DatabaseException, InvalidDriverException {
        branches = new ArrayList<>();
        branchManager = new BranchManager();
        branchDB = new BranchDB();
        projectDB = new ProjectDB();
        projectId = projectDB.insertProject(42, "projectTest", "./michaelJackson.tex", "09df3bc50707273be56ac901e534230a");
        branchID = branchDB.insertBranch(projectId, "branchTest");
        Branch branchData = branchDB.getBranchData(projectId, branchID);
        branches.add(branchData);
    }

    @AfterAll
    public void tearDown() throws DatabaseException {
        projectDB.deleteProject(projectId);
        branchDB.deleteBranch(projectId, branchID);
    }

    @Test
    void getProjectBranchesMetaDataReturnsRightAmountOfBranches() throws VersioningException, DatabaseException, InvalidDriverException {
        projectDB.getProjectData(projectId);
        List<String> projectBranchesMetaData = branchManager.getProjectBranchesMetaData(projectId);
        assertEquals(1, projectBranchesMetaData.size());
    }

    @Test
    void getProjectBranchesMetaDataReturnsRightMetadataName() throws VersioningException, DatabaseException, ValueNotFoundException, InvalidDriverException {
        List<Integer> branches = branchDB.getBranches(projectId);
        List<String> projectBranchesMetaData = branchManager.getProjectBranchesMetaData(projectId);
        for (int i = 0; i < branches.size(); i++) {
            assertEquals(projectBranchesMetaData.get(i), branchManager.metadataFileName(projectId, branches.get(i)));
        }
    }

    @Test
    void getBranchesFromProjectReturnsTheRightID() throws DataAccessException {
        assertEquals("branchTest", branchManager.getBranchesFromProject(projectId).get(0).getBranchName());
    }
}