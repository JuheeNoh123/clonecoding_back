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

    @Operation(summary="좌석 선택시 결제중인 좌석인지 확인하는 API",
            description = "사용자가 좌석을 선택했을때, " +
                    "<br>해당 좌석을 다른 사용자가 이미 결제중인 상황일 때 ,False 리턴" +
                    "<br>아무도 해당 좌석을 결제중이 아니라면 True 리턴" +
                    "<br> 막아두는 시간 3분으로 정해둠 (3분 이후에 다시 좌석 선택 가능)" +
                    "<br> 요청 형태는 아래 스키마(selectSeatsDTO) 확인해보면 됩니당 ~",
            tags = {"예매페이지"})
    @PostMapping("/selectSeats")
    public Boolean selectSeats(@RequestBody BookingDTO.selectSeatsDTO request){
        return bookingService.checkRedis(request.getMovieId(), request.getBranchId(), request.getTheaterNum(), request.getScreeningDate(), request.getScreeningTime(), request.getSeats(), 180);
    }

    @Operation(summary="결제완료 후 결제 정보 및 예매 정보 저장",
            description = "결제완료 후 결제 정보 및 예매 정보 저장 <br>결제 완료 되면 보내주기 " +
                    "<br>보내줄때 형식은 아래 스키마(bookingSeat) 확인해서 보내주면 될거같아여" +
                    "<br>예매 후 어떻게 반환 되는지는 스키마(checkBooking) 확인 하기 ~",
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
