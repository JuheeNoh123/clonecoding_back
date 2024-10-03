package CGVcloneCoding.cloneCoding.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
public class Movie {
    @Id
    private long movie_id;  // 영화 ID는 TMDB의 영화 ID를 그대로 사용

    private String title;  // 영화 제목
    private LocalDate release_date;  // 개봉일
    private int runtime;  // 상영 시간
    private String poster_path;  // 포스터 경로
    private String age;  // 관람 연령

    @Lob
    @Column(columnDefinition = "TEXT")  // 줄거리를 TEXT로 저장
    private String overview;  // 영화 설명

    private String production_countries;  // 제작 국가
    private float popularity;  // 인기도
    private float vote_average;  // 평점
    private int vote_count;  // 투표 수
    private LocalDateTime created_at;  // 데이터 생성 시간
    private LocalDateTime updated_at;  // 데이터 업데이트 시간

    // 모든 필드를 초기화하는 생성자
    public Movie(long movie_id, String title, LocalDate release_date, int runtime,
                 String poster_path, String age, String overview, String production_countries,
                 float popularity, float vote_average, int vote_count) {
        this.movie_id = movie_id;
        this.title = title;
        this.release_date = release_date;
        this.runtime = runtime;
        this.poster_path = poster_path;
        this.age = age;
        this.overview = overview;
        this.production_countries = production_countries;
        this.popularity = popularity;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.created_at = LocalDateTime.now();
        this.updated_at = this.created_at;
    }

    // 영화 정보 업데이트 메서드
    public void updateMovieInfo(float popularity, float vote_average, int vote_count) {

        this.popularity = popularity;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.updated_at = LocalDateTime.now();  // 정보가 업데이트되면 updated_at 갱신
    }


}
