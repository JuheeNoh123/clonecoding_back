package CGVcloneCoding.cloneCoding.service;

import CGVcloneCoding.cloneCoding.DTO.BookingDTO;
import CGVcloneCoding.cloneCoding.DTO.ScreeningDTO;
import CGVcloneCoding.cloneCoding.domain.Branch;
import CGVcloneCoding.cloneCoding.domain.Movie;
import CGVcloneCoding.cloneCoding.domain.Screening;
import CGVcloneCoding.cloneCoding.domain.Theater;
import CGVcloneCoding.cloneCoding.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MovieScheduleService {
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;
    private final TheaterRepository theaterRepository;
    private final ScreeningRepository screeningRepository;
    private final BranchRepository branchRepository;
    private final BookingService bookingService;

    @Scheduled(cron = "0 0 1 1 * ?") // 매월 1일 오전 1시에 스케줄 재생성
    @Transactional
    public void resetSchedule() {
        generateNewSchedule();
    }

    @Transactional
    public void generateNewSchedule() {
        LocalDate now = LocalDate.now();
        List<Movie> movies = movieRepository.getAllMovies();
        List<Theater> theaters = theaterRepository.findAll();
        Random random = new Random();

        // 개봉한 영화와 개봉하지 않은 영화를 분리
        List<Movie> releasedMovies = new ArrayList<>();
        List<Movie> unreleasedMovies = new ArrayList<>();
        categorizeMovies(movies, releasedMovies, unreleasedMovies, now);


        // 상영 가능한 시간 리스트 생성
        List<LocalTime> availableShowtimes = generateAvailableShowtimes();

        //상영관 별로 상영 시간표 생성
        for (Theater theater : theaters) {
            Map<LocalDate, Long> lastScreeningMap = new HashMap<>(); // 마지막 상영 영화 ID를 저장할 맵

            // 랜덤으로 상영할 시간 10개 선택
            List<LocalTime> selectedShowtimes = getRandomShowtimes(availableShowtimes,90);


            // 개봉일 기준으로 날짜 범위 설정
            LocalDate startDate = now.withDayOfMonth(1); // 현재 월의 첫 날
            LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth()); // 현재 월의 마지막 날

            //한달 범위 내의 시간표 생성
            for (LocalDate screeningDate = startDate; screeningDate.isBefore(endDate.plusDays(1)); screeningDate = screeningDate.plusDays(1)) {
                scheduleMovieForDate(selectedShowtimes, releasedMovies, unreleasedMovies, lastScreeningMap, theater, screeningDate,random );
            }
        }
    }

    //개봉한영화와 개봉하지 않은 영화 분리
    public void categorizeMovies(List<Movie> movies, List<Movie> releasedMovies, List<Movie> unreleasedMovies, LocalDate now){
        for (Movie movie : movies) {
            if (movie.getRelease_date().isBefore(now)) {
                releasedMovies.add(movie);
            } else {
                unreleasedMovies.add(movie);
            }
        }
    }


    //상영 가능 시간 리스트 생성
    public List<LocalTime> generateAvailableShowtimes() {
        List<LocalTime> availableShowtimes = new ArrayList<>();
        for (int hour = 9; hour <= 23; hour++) { // 9:00 AM ~ 23:00 PM
            for (int minute : new int[]{0, 15, 30, 45}) { // 15분 간격
                availableShowtimes.add(LocalTime.of(hour, minute));
            }
        }

        return availableShowtimes;
    }

    //시간표 10개만 랜덤으로 생성
    public List<LocalTime> getRandomShowtimes(List<LocalTime> availableShowtimes, int count){
        List<LocalTime> selectedShowtimes = new ArrayList<>();
        Collections.shuffle(availableShowtimes); // 모든 가능한 상영 시간을 랜덤으로 섞음
        for (int i = 0; i < count && i < availableShowtimes.size(); i++) { // 최대 5개
            selectedShowtimes.add(availableShowtimes.get(i));
        }

        return selectedShowtimes;
    }

    //상영 시간표 스케줄짜기
    public void scheduleMovieForDate(List<LocalTime> selectedShowtimes, List<Movie> releasedMovies, List<Movie> unreleasedMovies,
                                     Map<LocalDate, Long> lastScreeningMap,Theater theater, LocalDate screeningDate, Random random){
        for (LocalTime showtime : selectedShowtimes) {

            //영화 선택
            Movie selectedMovie = chooseMovie(releasedMovies, unreleasedMovies, screeningDate, random);

            if (selectedMovie==null){
                continue;
            }

            // 동일한 영화가 연속해서 상영되지 않도록 체크(해쉬맵 이용)
            Long lastScreenedMovieId = lastScreeningMap.getOrDefault(screeningDate, null);
            while (lastScreenedMovieId != null && lastScreenedMovieId.equals(selectedMovie.getMovie_id())) {
                // 다른 영화를 선택
                selectedMovie = chooseDifferentMovie(releasedMovies, selectedMovie, random);
                lastScreenedMovieId = lastScreeningMap.get(screeningDate);
            }

            // 상영 스케줄 생성
            Screening screeningEntry = new Screening();
            screeningEntry.setMovie(selectedMovie);
            screeningEntry.setTheater(theater);
            screeningEntry.setStartTime(showtime);
            screeningEntry.setScreeningDate(screeningDate);

            // 상영 스케줄 저장
            screeningRepository.save(screeningEntry);

            // 마지막 상영 영화 ID 업데이트
            lastScreeningMap.put(screeningDate, selectedMovie.getMovie_id());
        }

    }



    //영화 선택
    public Movie chooseMovie(List<Movie> releasedMovies, List<Movie> unreleasedMovies, LocalDate screeningDate, Random random){
        // 인기도 기반 영화 선택
        List<Movie> weightedMovies = new ArrayList<>();

        // 개봉한 영화에 인기도 기반으로 가중치를 부여
        for (Movie movie : releasedMovies) {
            int popularityScore = (int)movie.getPopularity(); // 가정: 영화 객체에 인기도 점수 필드가 있다고 가정
            for (int i = 0; i <= popularityScore; i++) {
                weightedMovies.add(movie); // 인기도에 따라 중복 추가
            }
        }

        // 개봉한 영화 또는 개봉하지 않은 영화 선택
        // 인기도가 높은 영화가 더 많이 선택될 수 있도록 조정
        if (!weightedMovies.isEmpty() && random.nextBoolean()) {
            return weightedMovies.get(random.nextInt(weightedMovies.size()));
        } else if (!unreleasedMovies.isEmpty()) {
            Movie selectedMovie = unreleasedMovies.get(random.nextInt(unreleasedMovies.size()));
            // 개봉일 이후로 조정
            if (screeningDate.isBefore(selectedMovie.getRelease_date())) {
                return null; // 개봉일 이전에는 상영하지 않음
            }
            return selectedMovie;
        }

        return null;
    }

    //다른 영화 선택
    private Movie chooseDifferentMovie(List<Movie> releasedMovies, Movie currentMovie, Random random) {
        Movie newMovie;
        do {
            newMovie = releasedMovies.get(random.nextInt(releasedMovies.size()));
        } while (newMovie.getMovie_id()==currentMovie.getMovie_id()); // 현재 영화와 다른 영화를 선택
        return newMovie;
    }


    //영화 예매 - 극장 지점까지 선택했을때
    public List<LocalDate> availableTheaterDate(Long movieId, Long branchId) {
        Movie movie = movieRepository.getMovie(movieId);
        Branch branch = branchRepository.findBranch(branchId);
        List<LocalDate> strList= screeningRepository.availableDate(movie, branch);
        Set<LocalDate> set = new HashSet<>(strList);
        List<LocalDate> newStrList = new ArrayList<>(set);
        Collections.sort(newStrList);
        return newStrList;
    }

    //영화 예매 - 극장 지점 -> 날짜 까지 선택 했을 때
    public List<ScreeningDTO.theaterSeats> availableTheaterSeats(Long movieId, Long branchId, LocalDate screeningDate) {
        Movie movie = movieRepository.getMovie(movieId);

        int runTime = movie.getRuntime();
        // 러닝 타임을 Duration으로 변환
        Duration duration = Duration.ofMinutes(runTime);

        Branch branch = branchRepository.findBranch(branchId);
        List<Object[]> theaterSeats =  screeningRepository.availableTheaterSeats(movie, branch, screeningDate);
        List<ScreeningDTO.theaterSeats> theaterSeatsList = new ArrayList<>();
        for (Object[] theaterSeat : theaterSeats) {

            Screening screening = (Screening) theaterSeat[0]; // Screening 객체
            LocalTime startTime = screening.getStartTime();
            // 시작 시간에 러닝 타임을 더함
            LocalTime endTime = startTime.plus(duration);
            String theaterNum = screening.getTheater().getName() + '관';
            long totalCount = seatRepository.totalSeatCounting(screening.getTheater());
            String totalSeats = '총'+ String.valueOf(totalCount)+'석';
            BookingDTO.ShowSeatsDTO showSeatsDTO = bookingService.showBookingSeats(movieId, branchId, screeningDate, screening.getTheater().getName(), startTime);
            long bookedCount = showSeatsDTO.getSeats().size();
            System.out.println(bookedCount);
            System.out.println(seatRepository.totalSeatCounting(screening.getTheater()));
            String remainSeats = String.valueOf(totalCount-bookedCount) + '석';
            theaterSeatsList.add(new ScreeningDTO.theaterSeats(theaterNum, startTime, endTime,branch.getName(), totalSeats, remainSeats));
        }
        return theaterSeatsList;

    }


}
