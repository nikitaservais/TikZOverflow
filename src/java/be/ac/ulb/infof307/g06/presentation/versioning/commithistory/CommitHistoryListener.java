package be.ac.ulb.infof307.g06.presentation.versioning.commithistory;

import be.ac.ulb.infof307.g06.models.branch.Branch;

import java.util.ArrayList;
import java.util.List;

/**
 * CommitHistory button listener
 */
public interface CommitHistoryListener {
    List<Branch> getBranchesInfo();
    ArrayList<ArrayList<String>> getCommitsInfo(Integer branchId);
}
