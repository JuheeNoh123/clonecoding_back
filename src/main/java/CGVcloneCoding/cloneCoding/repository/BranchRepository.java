package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Branch;

import java.util.List;

public interface BranchRepository {
    List<Branch> findAll();
    Branch findBranch(Long branchId);
}
