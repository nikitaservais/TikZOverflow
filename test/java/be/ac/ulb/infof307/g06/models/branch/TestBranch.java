package be.ac.ulb.infof307.g06.models.branch;

import be.ac.ulb.infof307.g06.models.branch.Branch;
import be.ac.ulb.infof307.g06.models.commit.Commit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBranch {

    @Test
    void doesSetCommitsSetupTheRightOnes(){
        Branch branch = new Branch(1,2,"testBranch","now",0);
        ArrayList<Commit> commits = new ArrayList<>();
        commits.add(new Commit("now","firstCommit"));
        branch.setCommits(commits);
        assertEquals("firstCommit",branch.getCommits().get(0).getMessage());
    }

    @Test
    void isProjectIdEqualsTo1(){
        Branch branch = new Branch(1,2,"testBranch","now",0);
        assertEquals(1,branch.getProjectID());
    }
    @Test
    void isProjectIdSetTo11(){
        Branch branch = new Branch(1,2,"testBranch","now",0);
        branch.setProjectID(11);
        assertEquals(11,branch.getProjectID());
    }
    @Test
    void isBranchStatusEqualsToFalse(){
        Branch branch = new Branch(1,2,"testBranch","now",0);
        assertFalse(branch.getStatus());
    }
    @Test
    void isBranchStatusSetToTrue(){
        Branch branch = new Branch(1,2,"testBranch","now",0);
        branch.setStatus(true);
        assertTrue(branch.getStatus());
    }
    @Test
    void isBranchIdEqualsTo2(){
        Branch branch = new Branch(1,2,"testBranch","now",0);
        assertEquals(2,branch.getBranchID());
    }
    @Test
    void isBranchIdEqualsTo22(){
        Branch branch = new Branch(1,2,"testBranch","now",0);
        branch.setBranchID(22);
        assertEquals(22,branch.getBranchID());
    }

    @Test
    void isBranchNameEqualsToTestBranch(){
        Branch branch = new Branch(1,2,"testBranch","now",0);
        assertEquals("testBranch",branch.getBranchName());
    }
    @Test
    void isBranchNameSetToNotAnymore(){
        Branch branch = new Branch(1,2,"testBranch","now",0);
        branch.setBranchName("NotAnymore");
        assertEquals("NotAnymore",branch.getBranchName());
    }
    @Test
    void isCreationDateEqualsToNow(){
        Branch branch = new Branch(1,2,"testBranch","now",0);
        assertEquals("now",branch.getCreationDate());
    }
    @Test
    void isCreationDateSetToTomorrow(){
        Branch branch = new Branch(1,2,"testBranch","now",0);
        branch.setCreationDate("tomorrow");
        assertEquals("tomorrow",branch.getCreationDate());
    }



}

