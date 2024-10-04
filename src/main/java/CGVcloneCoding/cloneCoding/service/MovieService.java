package CGVcloneCoding.cloneCoding.service;

import CGVcloneCoding.cloneCoding.domain.Movie;
import CGVcloneCoding.cloneCoding.repository.MovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {
    String today = LocalDate.now().toString();
    // 한 달 전 날짜 계산
    String oneMonthAgo = LocalDate.now().minusMonths(1).toString();
    private final MovieRepository movieRepository;

    @Value("${movieAPI.key}")
    private String API_KEY;

    //현재 상영중인 영화 리스트 api 호출
    public HttpResponse<String> nowPlaying() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        // 최근 한 달 동안 한국에서 개봉된 영화 요청
        HttpRequest recentMoviesRequest_page1 = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/discover/movie?language=ko-KR&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&primary_release_date.gte=" + oneMonthAgo + "&primary_release_date.lte=" + today + "&region=KR"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        // 현재 상영 중인 영화 응답 받기
        return client.send(recentMoviesRequest_page1, HttpResponse.BodyHandlers.ofString());
    }

    //상영 예정인 영화 리스트 api 호출
    public HttpResponse<String> upcoming() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest upcomingRequest_page1 = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/discover/movie?language=ko-KR&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&primary_release_date.gte=" + today))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+API_KEY)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        // 상영 예정 영화 응답 받기
        return client.send(upcomingRequest_page1, HttpResponse.BodyHandlers.ofString());

    }

    //영화 상세정보 뽑아오기 api 호출
    public HttpResponse<String> movieDetails(long movieId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + movieId + "?language=ko-KR"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    //관람 연령가 구하기 api 호출
    public String getMovieRating(int movieId) throws IOException, InterruptedException {
        // TMDB Release Dates 엔드포인트를 호출하는 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + movieId + "/release_dates"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        // HTTP 클라이언트 생성
        HttpClient client = HttpClient.newHttpClient();

        // 응답 받기
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // JSON 파싱하여 한국 관람 등급 정보 추출
        return extractKoreanRating(response.body());
    }

    // 출연 정보 api 호출
    public HttpResponse<String> credits(long movieId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // Credits API 요청 (API 키를 URL에 포함)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + movieId + "/credits?language=ko-KR"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();


        // 응답 받기
        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }









    // 매일 자정에 실행되도록 설정
    @Scheduled(cron = "0 0 0 * * *")
    //영화 인기도 순 정렬 후 db 저장 (제목, 소개, 포스터, 상영날짜, 영화 ID, 투표율, 투표수, 인기도)
    @Transactional
    public void PlayingMovie() throws IOException, InterruptedException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        HttpResponse<String> nowPlayingResponse1 = nowPlaying();
        HttpResponse<String> upcomingResponse1 = upcoming();


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode nowPlayingJsonPage1 = objectMapper.readTree(nowPlayingResponse1.body());
        JsonNode upcomingJsonPage1 = objectMapper.readTree(upcomingResponse1.body());


        ArrayNode combinedResults = objectMapper.createArrayNode();

        // 현재 상영 중인 영화 추가
        for (JsonNode movie : nowPlayingJsonPage1.get("results")) {
            combinedResults.add(movie);
        }

        // 상영 예정 영화 추가
        for (JsonNode movie : upcomingJsonPage1.get("results")) {
            combinedResults.add(movie);
        }



        System.out.println(combinedResults);

        //영화 정보
        for (JsonNode movie : combinedResults) {
            int movieId = movie.get("id").asInt();
            String age = getMovieRating(movieId);
            if (age.equals("등급 정보 없음") || age.equals("한국에서의 관람 등급 정보가 없습니다.")) {
                System.out.println("관람 등급 정보가 없어 저장하지 않습니다: " + movieId);
                continue;  // 관람 등급 정보가 없으면 저장하지 않음
            }
            String title =  movie.get("title").asText();
            String overview = movie.get("overview").asText();
            String poster_path = movie.get("poster_path").asText();
            String release_date = movie.get("release_date").asText();
            LocalDate releaseDate = LocalDate.parse(release_date, formatter);


            int runtime = (int) details(movieId).get(0);
            String production_countries = (String) details(movieId).get(1);
            System.out.println(movieId);


            //실시간 정보 (인기도, 예매율 등)
            float popularity = movie.get("popularity").floatValue();
            float vote_average = movie.get("vote_average").floatValue();
            int vote_count = movie.get("vote_count").intValue();

            saveOrUpdateMovie(movieId, title, releaseDate, runtime, overview,
                            poster_path, age, production_countries, popularity, vote_average, vote_count);

        }
    }

    // 기존 영화가 있으면 정보 업데이트
    @Transactional
    public void saveOrUpdateMovie(long movieId, String title, LocalDate releaseDate, int runtime, String overview,
                                  String posterPath, String age, String productionCountries, float popularity,
                                  float voteAverage, int voteCount) {
        // movieId로 영화 객체를 가져옴 (기존 영화가 있는지 확인)
        Movie movie = movieRepository.getMovie(movieId);

        if (movie != null) {
            movie.updateMovieInfo(popularity, voteAverage, voteCount);
        } else {
            // 기존 영화가 없으면 새로운 영화 객체 생성 후 정보 설정
            movie = new Movie(movieId, title, releaseDate, runtime,
                    posterPath, age, overview, productionCountries,
                    popularity,voteAverage, voteCount);

            // 새로운 영화 저장
            movieRepository.saveMovies(movie);
        }

        System.out.println("영화 정보가 업데이트 되었습니다.");
    }

    //관람 연령가 구하기(한국연령 뽑아내기)
    private String extractKoreanRating(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode resultsNode = rootNode.path("results");

        for (JsonNode resultNode : resultsNode) {
            String countryCode = resultNode.path("iso_3166_1").asText();
            if (countryCode.equals("KR")) { // 한국(KR)의 관람 등급 정보만 추출
                JsonNode releaseDatesNode = resultNode.path("release_dates");
                for (JsonNode releaseDateNode : releaseDatesNode) {
                    String certification = releaseDateNode.path("certification").asText();
                    return certification.isEmpty() ? "등급 정보 없음" : certification;
                }
            }
        }
        return "한국에서의 관람 등급 정보가 없습니다.";
    }

    //영화 출연진 구하기
    public List<HashMap<String, String>> movieCredits(long movieId) throws IOException, InterruptedException {
        HttpResponse<String> response = credits(movieId);
        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.readTree(response.body());

        // 감독 정보 추출
        String director = "";
        JsonNode crew = responseBody.get("crew");
        if (crew != null) { // crew가 null인지 확인
            for (JsonNode member : crew) {
                String job = member.get("job").asText();
                if ("Director".equals(job)) {
                    director = member.get("name").asText();
                    break; // 감독을 찾았으면 루프 종료
                }
            }
        }

        // 출연진 정보 추출
        List<HashMap<String, String>> castList = new ArrayList<>();
        JsonNode castArray = responseBody.get("cast");
        if (castArray != null) { // castArray가 null인지 확인
            for (JsonNode actor : castArray) {
                HashMap<String, String> actorInfo = new HashMap<>();
                actorInfo.put("name", actor.get("name").asText());
                actorInfo.put("character", actor.get("character").asText());
                castList.add(actorInfo); // HashMap 객체 추가
            }
        }

        // 감독 정보를 추가하여 리스트의 첫 번째 요소에 넣기
        HashMap<String, String> directorInfo = new HashMap<>();
        directorInfo.put("director", director);
        castList.add(0, directorInfo); // 리스트의 첫 번째 요소로 감독 정보 추가

        return castList;
    }


    //영화 상세 정보 출력( 런타임, 국가 정보 불러오기 용)
    public ArrayList<Object> details(long movieId) throws IOException, InterruptedException {
        HttpResponse<String> response = movieDetails(movieId);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.readTree(response.body());
        ArrayList<Object> a = new ArrayList<>();

        a.add(responseBody.get("runtime").asInt()); // runtime 추가
        a.add(responseBody.get("origin_country").get(0).asText()); // 첫 번째 origin_country 추가

        System.out.println(responseBody);
        System.out.println(a);

        return a;
    }

    //장르 출력 용
    public List<String> genreList(long movieId) throws IOException, InterruptedException {
        HttpResponse<String> response = movieDetails(movieId);
        ArrayList<String> genre_list = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.readTree(response.body());
        JsonNode genres= responseBody.get("genres");
        for (JsonNode genre : genres) {
            genre_list.add(genre.get("name").asText());
        }
        return genre_list;
    }

    //현재 상영중인 영화 출력 - 평점순(인기도순)
    public List<Movie> getPlayingMovieList() {
        return movieRepository.getAllPlayingMovies();
    }

    //현재 상영중인 영화 출력 - 예매율순(투표율순)
    public List<Movie> getPlayingMovieListBYvote() {
        return movieRepository.getAllPlayingMoviesBYvote();
    }

    //현재 상영중인 영화 출력 - 관람객순(투표개수)
    public List<Movie> getPlayingMovieListBYvoteCount() {
        return movieRepository.getAllPlayingMoviesBYvoteCount();
    }

    //상영 예정 영화 출력
    public List<Movie> getUpcomingMovies() throws IOException, InterruptedException {
        return movieRepository.getAllUpcomingMovies();
    }

    //전체 영화 순위 출력 - 인기순
    public List<Movie> getMovies() throws IOException, InterruptedException {
        return movieRepository.getAllMovies();
    }

    //전체 영화 순위 출력 - 예매율순(투표율순)
    public List<Movie> getMoviesByVote() throws IOException, InterruptedException {
        return movieRepository.getAllMoviesByVote();
    }

    //전체 영화 순위 출력 - 사전순
    public List<Movie> getMoviesBydic() throws IOException, InterruptedException {
        return movieRepository.getAllMoviesBYdic();
    }
    //영화 출연정보 출력(구)
//    public String getMovieCredits(long movieId) throws IOException, InterruptedException {
//        // TMDB Movie Credits 엔드포인트를 호출하는 요청 생성
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://api.themoviedb.org/3/movie/" + movieId + "/credits"))
//                .header("accept", "application/json")
//                .header("Authorization", "Bearer " + API_KEY)
//                .method("GET", HttpRequest.BodyPublishers.noBody())
//                .build();
//
//        // HTTP 클라이언트 생성
//        HttpClient client = HttpClient.newHttpClient();
//
//        // 응답 받기
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
//        return response.body();
//    }

    //영화 객체 가져오기
    public Movie getMovie(long movieId) {
        return movieRepository.getMovie(movieId);
    }
}
