package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Branch;
import CGVcloneCoding.cloneCoding.domain.Theater;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JPABranchRepository implements BranchRepository {
    private final EntityManager em;
    @Override
    public List<Branch> findAll() {
        String jpql = "SELECT b FROM Branch b";
        return em.createQuery(jpql, Branch.class).getResultList();
    }

    @Override
    public Branch findBranch(Long branchId) {
        return em.find(Branch.class, branchId);
    }
}
