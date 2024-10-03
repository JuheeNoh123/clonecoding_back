package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Branch;
import CGVcloneCoding.cloneCoding.domain.Movie;
import CGVcloneCoding.cloneCoding.domain.Screening;
import CGVcloneCoding.cloneCoding.domain.Theater;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
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
    public List<LocalDate> availableDate(Movie movie, Branch branch) {
        String query = "SELECT s.screeningDate " +  // 시작 시간을 선택
                "FROM Screening s " +
                "JOIN s.movie m " +
                "JOIN s.theater t " +
                "WHERE t.branch = :branch " +  // 브랜치 ID 조건
                "AND m.movie_id = :movieId " +      // 영화 ID 조건
                "ORDER BY s.screeningDate";

        // EntityManager를 통해 쿼리 실행
        return em.createQuery(query, LocalDate.class)
                .setParameter("branch", branch)
                .setParameter("movieId", movie.getMovie_id())      // Movie의 movieId를 전달
                .getResultList();
    }

    @Override
    public List<Object[]> availableTheaterSeats(Movie movie, Branch branch, LocalDate screeningDate) {
        String query = "SELECT s, t.branch " +  // 모든 screening 정보와 movie title, popularity, theater branch
                "FROM Screening s " +
                "JOIN s.movie m " +
                "JOIN s.theater t " +
                "WHERE t.branch = :branch " +  // 브랜치 ID 조건
                "AND m.movie_id = :movieId " +  // 영화 ID 조건
                "AND s.screeningDate = :screeningDate " +  // 상영 날짜 조건
                "ORDER BY t.id, s.startTime";  // 상영관 ID 기준 정렬 후 상영 시작 시간 기준 정렬

        // EntityManager를 통해 쿼리 실행
        return em.createQuery(query, Object[].class)
                .setParameter("branch", branch)  // Theater의 branchId를 전달
                .setParameter("movieId", movie.getMovie_id())   // Movie의 movieId를 전달
                .setParameter("screeningDate", screeningDate)   // 상영 날짜를 전달
                .getResultList();
    }

    @Override
    public Screening getScreening(LocalDate screeningDate, LocalTime startTime, Movie movie, Theater theater) {
        String query = "Select s from Screening s " +
                "Where s.screeningDate = : screeningDate " +
                "AND s.startTime = :startTime " +
                "AND s.movie = :movie " +
                "AND s.theater = :theater";

        return em.createQuery(query, Screening.class)
                .setParameter("screeningDate", screeningDate)
                .setParameter("startTime", startTime)
                .setParameter("movie", movie)
                .setParameter("theater", theater)
                .getSingleResult();
    }


}
