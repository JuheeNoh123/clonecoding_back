package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface MovieRepository {
    void saveMovies(Movie movie);

    Movie getMovie(long id);

    List<Movie> getAllPlayingMovies();

    List<Movie> getAllPlayingMoviesBYvote();

    List<Movie> getAllPlayingMoviesBYvoteCount();

    List<Movie> getAllUpcomingMovies();

    List<Movie> getAllMovies();

    List<Movie> getAllMoviesBYdic();

    List<Movie> getAllMoviesByVote();
}
