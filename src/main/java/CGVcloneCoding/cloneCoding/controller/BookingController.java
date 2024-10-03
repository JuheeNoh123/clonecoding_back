package CGVcloneCoding.cloneCoding.controller;

import CGVcloneCoding.cloneCoding.DTO.BookingDTO;
import CGVcloneCoding.cloneCoding.domain.Booking;
import CGVcloneCoding.cloneCoding.service.BookingService;
import CGVcloneCoding.cloneCoding.service.MovieScheduleService;
import CGVcloneCoding.cloneCoding.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookingController {
    private final MovieService movieService;
    private final MovieScheduleService movieScheduleService;
    private final BookingService bookingService;

    @PostMapping("/showBookingSeats")
    public void showBookingSeats(@RequestBody BookingDTO.showBookingSeats request ) {

    }

    @PostMapping("/booking")
    public String booking(@RequestHeader("Authorization") String token ,@RequestBody BookingDTO.bookingSeats request) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        bookingService.booking(jwtToken, request.getMovieId(),
                request.getSeats() ,request.getTheaterNum(), request.getBranchId(),
                request.getScreeningDate(), request.getScreeningTime());
        return "예매 완료";
    }
}
