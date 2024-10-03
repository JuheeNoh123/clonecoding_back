package CGVcloneCoding.cloneCoding.service;

import CGVcloneCoding.cloneCoding.DTO.SeatDTO;
import CGVcloneCoding.cloneCoding.domain.*;
import CGVcloneCoding.cloneCoding.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public void showBookingSeats(){}

    //row - A,B,C... column - 1,2,3...
    @Transactional
    public void booking(String token, Long movieId, List<SeatDTO.Seats> seats,
                        Long theaterNum, Long branchId, LocalDate ScreeningDate, LocalTime screeningTime) {
        User user = userService.tokenToUser(token);
        Movie movie = movieRepository.getMovie(movieId);
        Branch branch = branchRepository.findBranch(branchId);
        Theater theater = theaterRepository.findTheater(theaterNum, branch);
        Screening screening = screeningRepository.getScreening(ScreeningDate, screeningTime, movie, theater);
        for(SeatDTO.Seats seat : seats) {
            Seat findSeat = seatRepository.getSeat(seat.getRow(), seat.getNum(), theater);
            Booking booking = new Booking(screening,findSeat,user);
            bookingRepository.save(booking);
        }




    }


}
