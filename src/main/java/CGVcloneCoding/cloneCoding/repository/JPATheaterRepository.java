package CGVcloneCoding.cloneCoding.repository;

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
    public Theater findTheater(Long TheaterId){
        return em.find(Theater.class, TheaterId);
    }
}
