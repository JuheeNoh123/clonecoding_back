package CGVcloneCoding.cloneCoding.controller;

import CGVcloneCoding.cloneCoding.DTO.BookingDTO;
import CGVcloneCoding.cloneCoding.service.BookingService;
import CGVcloneCoding.cloneCoding.service.MovieScheduleService;
import CGVcloneCoding.cloneCoding.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary="결제완료 후 결제 정보 및 예매 정보 저장",
            description = "결제완료 후 결제 정보 및 예매 정보 저장 <br>결제 완료 되면 보내주기 " +
                    "<br>보내줄때 형식은 아래 스키마에 bookingSeat 부분 확인해서 보내주면 될거같아여" +
                    "<br>예매 후 어떻게 반환 되는지는 스키마에서 checkBooking 부분에 이써여!",
            tags = {"예매페이지"})
    @PostMapping("/booking")
    public BookingDTO.checkBooking booking(@RequestHeader("Authorization") String token ,@RequestBody BookingDTO.bookingSeats request) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return bookingService.booking(jwtToken, request.getMovieId(),
                request.getSeats() ,request.getPaymentAmount(), request.getPaymentType(), request.getEasyPaymentType(),
                request.getTicketsCategory(),
                request.getTheaterNum(), request.getBranchId(),
                request.getScreeningDate(), request.getScreeningTime());

    }
}
