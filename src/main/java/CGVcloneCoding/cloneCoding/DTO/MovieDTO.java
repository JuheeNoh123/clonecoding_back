package CGVcloneCoding.cloneCoding.DTO;

import CGVcloneCoding.cloneCoding.domain.Movie;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieDTO {
    @Data
    public static class MovieDTOBuilder {
        //영화 정보 리스트
        //- 영화 포스터
        //- 영화 제목
        //- 예매율 -> 투표랑 인기도.. 는 할수있는뎁,,
        //- 개봉 날짜
        //- 관람 연령가
        private long movie_id;
        private String poster_path;
        private String title;
        private LocalDate release_date;
        private String age;
        private float vote_average;

        public MovieDTOBuilder(Movie movie) {
            this.movie_id = movie.getMovie_id();
            this.poster_path = movie.getPoster_path();
            this.title = movie.getTitle();
            this.release_date = movie.getRelease_date();
            this.age = movie.getAge();
            this.vote_average = movie.getVote_average();

        }
    }

    @Data
    public static class MovieDetailsDTO {
        private long movieId;  // 영화 ID
        private String posterPath;  // 포스터 경로
        private String title;  // 영화 제목
        private LocalDate releaseDate;  // 개봉 날짜
        private String age;  // 관람 등급
        private float voteAverage;  // 평균 투표 점수
        private String productionCountry;  // 제작 국가 (단일 값)
        private List<HashMap<String, String>> credits;  // 출연진과 감독 정보
        private List<String> genres;  // 장르 목록
        private String overview;  // 영화 개요
        private int runtime;
        public MovieDetailsDTO(Movie movie, List<String> genreList, List<HashMap<String, String>> credits) {
            this.movieId = movie.getMovie_id();
            this.posterPath = movie.getPoster_path();
            this.title = movie.getTitle();
            this.releaseDate = movie.getRelease_date();
            this.age = movie.getAge();
            this.voteAverage = movie.getVote_average();
            this.productionCountry = movie.getProduction_countries(); // 단일 값으로 설정
            this.credits = credits;
            this.genres = genreList;
            this.overview = movie.getOverview();
            this.runtime = movie.getRuntime();
        }
    }

    @Data
    public static class MovieListForTickets {
        private long movieId;
        private String title;
        private String age;
        private LocalDate releaseDate;

        public MovieListForTickets(Movie movie) {
            this.movieId = movie.getMovie_id();
            this.title = movie.getTitle();
            this.age = movie.getAge();
            this.releaseDate = movie.getRelease_date();
        }
    }

    @Data
    public static class MoviePosterDTO {
        private long movieId;
        private String title;
        private String posterPath;
        private LocalDate releaseDate;
        public MoviePosterDTO(Movie movie) {
            this.movieId = movie.getMovie_id();
            this.title = movie.getTitle();
            this.posterPath = movie.getPoster_path();
            this.releaseDate = movie.getRelease_date();
        }
    }
}
