package CGVcloneCoding.cloneCoding.controller;

import CGVcloneCoding.cloneCoding.DTO.BookingDTO;
import CGVcloneCoding.cloneCoding.DTO.ScreeningDTO;
import CGVcloneCoding.cloneCoding.domain.Booking;
import CGVcloneCoding.cloneCoding.domain.Branch;
import CGVcloneCoding.cloneCoding.domain.Seat;
import CGVcloneCoding.cloneCoding.service.BookingService;
import CGVcloneCoding.cloneCoding.service.MovieScheduleService;
import CGVcloneCoding.cloneCoding.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController {
    private final MovieService movieService;
    private final MovieScheduleService movieScheduleService;
    private final BookingService bookingService;


    //남은 좌석 리턴
    @PostMapping("/ticket/chooseTheater/{movieId}/{branchId}/{screeningDate}")
    public BookingDTO.ShowSeatsDTO showBookingSeats(@PathVariable("movieId") long movieId,
                                       @PathVariable("branchId") long branchId,
                                       @PathVariable("screeningDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate screeningDate,
                                       @RequestBody BookingDTO.chooseTheaterAndTime request) {

        return bookingService.showBookingSeats(movieId, branchId, screeningDate, request.getTheaterName(), request.getScreeningTime());

    }

    @PostMapping("/booking")
    public String booking(@RequestHeader("Authorization") String token ,@RequestBody BookingDTO.bookingSeats request) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        bookingService.booking(jwtToken, request.getMovieId(),
                request.getSeats() ,request.getTicketsCategory(),
                request.getTheaterNum(), request.getBranchId(),
                request.getScreeningDate(), request.getScreeningTime());
        return "예매 완료";
    }
}
