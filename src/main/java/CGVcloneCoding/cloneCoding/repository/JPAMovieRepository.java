package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Movie;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JPAMovieRepository implements MovieRepository {
    private final EntityManager em;

    @Override
    public void saveMovies(Movie movie) {
        em.persist(movie);  // 새로운 영화 정보를 DB에 저장
    }

    @Override
    public Movie getMovie(long movie_id) {
        if (em.find(Movie.class, movie_id) == null) {
            System.out.println("id = " + movie_id);
        }
        return em.find(Movie.class, movie_id);
    }

    @Override
    public List<Movie> getAllPlayingMovies() {//평점순 - 인기
        String jpql =  "SELECT m FROM Movie m WHERE m.release_date <= CURRENT_DATE ORDER BY m.popularity DESC";
        return em.createQuery(jpql, Movie.class).getResultList();
    }

    @Override
    public List<Movie> getAllPlayingMoviesBYvote() {//예매율순-투표율
        String jpql =  "SELECT m FROM Movie m WHERE m.release_date <= CURRENT_DATE ORDER BY m.vote_average DESC";
        return em.createQuery(jpql, Movie.class).getResultList();
    }

    @Override
    public List<Movie> getAllPlayingMoviesBYvoteCount() {   //관람객순 - 투표개수 순
        String jpql =  "SELECT m FROM Movie m WHERE m.release_date <= CURRENT_DATE ORDER BY m.vote_count DESC";
        return em.createQuery(jpql, Movie.class).getResultList();
    }

    @Override
    public List<Movie> getAllUpcomingMovies() {
        String jpql =  "SELECT m FROM Movie m WHERE m.release_date > CURRENT_DATE ORDER BY m.popularity DESC";
        return em.createQuery(jpql, Movie.class).getResultList();
    }

    @Override
    public List<Movie> getAllMovies() {
        String jpql =  "SELECT m FROM Movie m ORDER BY m.popularity DESC";
        return em.createQuery(jpql, Movie.class).getResultList();
    }

    @Override
    public List<Movie> getAllMoviesBYdic() {
        String jpql =  "SELECT m FROM Movie m ORDER BY m.title";
        return em.createQuery(jpql, Movie.class).getResultList();
    }

    @Override
    public List<Movie> getAllMoviesByVote() {
        String jpql =  "SELECT m FROM Movie m ORDER BY m.vote_average DESC";
        return em.createQuery(jpql, Movie.class).getResultList();
    }
}
