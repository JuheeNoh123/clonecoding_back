package CGVcloneCoding.cloneCoding.controller;

import CGVcloneCoding.cloneCoding.DTO.MovieDTO;
import CGVcloneCoding.cloneCoding.DTO.ScreeningDTO;
import CGVcloneCoding.cloneCoding.domain.Movie;
import CGVcloneCoding.cloneCoding.service.MovieScheduleService;
import CGVcloneCoding.cloneCoding.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final MovieScheduleService movieScheduleService;

    //개발 테스트 용
    @GetMapping("/test")
    public void test() throws IOException, InterruptedException {
        //movieService.PlayingMovie();
        //movieScheduleService.generateNewSchedule();
    }

    //영화 리스트 출력(상영예정 + 현재 상영중인 영화)
    @Operation(summary="영화 리스트 출력",
            description = "[상영 예정 영화]와 [현재 상영중인 영화] 다 합쳐서 인기순으로 정렬 " +
            "<br> 'tmdb list page 1' 중 우리나라에서 [현재 방영중 + 상영 에정]인 영화만 골라서 저장돼서, 출력되는 영화 양이 적을 수 있음",
            tags = {"메인페이지"})
    @GetMapping("/movies")
    public List<MovieDTO.MovieDTOBuilder> getAllMovies() throws IOException, InterruptedException {
        List<Movie> movieList = movieService.getMovies();
        List<MovieDTO.MovieDTOBuilder> movieDTOBuilderList = new ArrayList<>();
        for (Movie movie : movieList) {
            movieDTOBuilderList.add(new MovieDTO.MovieDTOBuilder(movie));
        }
        return movieDTOBuilderList;
    }

    //현재 상영중인 영화 리스트 출력
    @Operation(summary="현재 상영 중인 영화 리스트 출력", description = "[현재 상영중인 영화] 인기순으로 정렬", tags = {"메인페이지"})
    @GetMapping("/movies/nowplaying")
    public List<MovieDTO.MovieDTOBuilder> getPlayingMovies() throws IOException, InterruptedException {
        //System.out.println(movieService.getMovieCredits(1139817));
        List<Movie> movieList = movieService.getPlayingMovieList();
        List<MovieDTO.MovieDTOBuilder> movieDTOBuilderList = new ArrayList<>();
        for (Movie movie : movieList) {
            movieDTOBuilderList.add(new MovieDTO.MovieDTOBuilder(movie));
        }
        return movieDTOBuilderList;
    }


    //상영 예정인 영화 출력
    @Operation(summary="상영 예정인 영화 리스트 출력", description = "[상영 예정인 영화] 인기순으로 정렬", tags = {"메인페이지"})
    @GetMapping("/movies/upcoming")
    public List<MovieDTO.MovieDTOBuilder> getUpcomingMovies() throws IOException, InterruptedException {

        List<Movie> movieList = movieService.getUpcomingMovies();

        List<MovieDTO.MovieDTOBuilder> movieDTOBuilderList = new ArrayList<>();
        for (Movie movie : movieList) {
            movieDTOBuilderList.add(new MovieDTO.MovieDTOBuilder(movie));
        }
        return movieDTOBuilderList;
    }

    @Operation(summary="영화 상세 정보 출력",
            description = "영화 상세 정보 출력" +
                    "<br><h3>조회 결과</h1>" +
                    "<ul><li>\"movieId\" : 영화 ID</li><li>\"title\" : 영화 제목</li><li>\"posterPath\" : 포스터경로</li><li>\"releaseDate\" : 개봉 날짜</li><li>\"age\" : 관람연령</li>" +
                    "<li>\"voteAverage\" : 투표율(예매율 대체)</li><li>\"productionCountry\" : 생산 국가</li><li>\"credits\" : 출연진(<i>\"director\" : 감독, \"character\": 캐릭터 - \"name\" : 배우</i>)</li>" +
                    "<li>\"genres\" : 장르</li><li>\"overview\" : 소개 출력</li></ul>",
            tags = {"메인페이지"})
    @GetMapping("movies/details/{movieId}")
    public MovieDTO.MovieDetailsDTO getMovieDetails(@PathVariable("movieId") long movieId) throws IOException, InterruptedException {
        Movie movie = movieService.getMovie(movieId);
        System.out.println(movie);
        List<String> genreList = movieService.genreList(movieId);
        List<HashMap<String, String>> credits = movieService.movieCredits(movieId);
        //System.out.println(movieService.getMovieCredits(movieId));
        return new MovieDTO.MovieDetailsDTO(movie, genreList,credits);
    }

    //예매페이지 들어왔을때 영화 리스트 출력
    @Operation(summary = "영화 리스트, 관람 연령가, 개봉날짜 예매율 순 출력",
            description = "예매 페이지 들어왔을때 첫 화면" +
                    "<b>정렬 : 예매율(투표율) 순 (기본)"+
                    "<br><hr><i>상영 예정작도 예매 가능하기 때문에 개봉 날짜가 필요할 것 같아서 추가로 넣었어여</hr></i>",
            tags = "예매페이지")
    @GetMapping("/ticket")
    public List<MovieDTO.MovieListForTickets> getAllMoviesForTickets() throws IOException, InterruptedException {
        List<Movie> movieList = movieService.getMoviesByVote();
        List<MovieDTO.MovieListForTickets> movieListForTickets = new ArrayList<>();
        for (Movie movie : movieList) {
            movieListForTickets.add(new MovieDTO.MovieListForTickets(movie));
        }
        return movieListForTickets;
    }

    //예매페이지 들어왔을때 영화 리스트 출력
    @Operation(summary = "영화 리스트, 관람 연령가, 개봉날짜 예매율 순 출력",
            description = "예매 페이지 들어왔을때 첫 화면" +
                    "<b>정렬 : 가나다순 (선택)"+
                    "<br><hr><i>상영 예정작도 예매 가능하기 때문에 개봉 날짜가 필요할 것 같아서 추가로 넣었어여</hr></i>",
            tags = "예매페이지")
    @GetMapping("/ticket/abc")
    public List<MovieDTO.MovieListForTickets> getAllMoviesForTicketsBydic() throws IOException, InterruptedException {
        List<Movie> movieList = movieService.getMoviesBydic();
        List<MovieDTO.MovieListForTickets> movieListForTickets = new ArrayList<>();
        for (Movie movie : movieList) {
            movieListForTickets.add(new MovieDTO.MovieListForTickets(movie));
        }
        return movieListForTickets;
    }

    @Operation(summary = "예매페이지에서 영화 선택시 아래 포스터 경로 출력",
            description = "예매페이지에서 영화 선택시 아래 포스터 경로 출력",
            tags = "예매페이지")
    @GetMapping("/ticket/{movieId}")
    public MovieDTO.MoviePosterDTO getMoviePoster(@PathVariable("movieId") long movieId) {
        return new MovieDTO.MoviePosterDTO(movieService.getMovie(movieId));

    }

    //상영 가능 날짜 출력
    @GetMapping("/ticket/{movieId}/{BranchId}")
    public List<LocalDate> availableTheaterDate(@PathVariable("movieId") long movieId, @PathVariable("BranchId") long branchId ){
        return movieScheduleService.availableTheaterDate(movieId, branchId);
    }

    @GetMapping("/ticket/{movieId}/{theaterId}/{screeningDate}")
    public List<ScreeningDTO.theaterSeats> availableTheaterSeats(@PathVariable("movieId") long movieId,
                                      @PathVariable("theaterId") long theaterId,
                                      @PathVariable("screeningDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate screeningDate) {
        return movieScheduleService.availableTheaterSeats(movieId, theaterId, screeningDate);
    }


}
