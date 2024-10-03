package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Movie;
import CGVcloneCoding.cloneCoding.domain.Screening;
import CGVcloneCoding.cloneCoding.domain.Theater;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JPAScreeningRepository implements ScreeningRepository {
    private final EntityManager em;
    @Override
    public void save(Screening screeningEntry) {
        em.persist(screeningEntry);
    }

    @Override
    public List<LocalDate> availableDate(Movie movie, Theater theater) {
        String query = "SELECT s.screeningDate " +  // 시작 시간을 선택
                "FROM Screening s " +
                "JOIN s.movie m " +
                "JOIN s.theater t " +
                "WHERE t.branch = :branchId " +  // 브랜치 ID 조건
                "AND m.movie_id = :movieId " +      // 영화 ID 조건
                "ORDER BY s.screeningDate";

        // EntityManager를 통해 쿼리 실행
        return em.createQuery(query, LocalDate.class)
                .setParameter("branchId", theater.getBranch())  // Theater의 branchId를 전달
                .setParameter("movieId", movie.getMovie_id())      // Movie의 movieId를 전달
                .getResultList();
    }

}
