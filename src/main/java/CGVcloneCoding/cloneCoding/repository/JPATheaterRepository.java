package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Branch;
import CGVcloneCoding.cloneCoding.domain.Theater;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JPATheaterRepository implements TheaterRepository {
    private final EntityManager em;

    @Override
    public List<Theater> findAll() {
        String jpql = "SELECT t FROM Theater t";
        return em.createQuery(jpql, Theater.class).getResultList();
    }

    @Override
    public Theater findTheater(String TheaterNum, Branch branch){
        String query = "SELECT t FROM Theater t WHERE t.name = :TheaterNum and t.branch = :branch";
        return em.createQuery(query, Theater.class).setParameter("TheaterNum", TheaterNum)
                .setParameter("branch", branch).getSingleResult();

    }
}
