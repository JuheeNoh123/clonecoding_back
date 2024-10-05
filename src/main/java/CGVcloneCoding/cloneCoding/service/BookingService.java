package CGVcloneCoding.cloneCoding.service;

import CGVcloneCoding.cloneCoding.DTO.BookingDTO;
import CGVcloneCoding.cloneCoding.DTO.SeatDTO;
import CGVcloneCoding.cloneCoding.domain.*;
import CGVcloneCoding.cloneCoding.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final BranchRepository branchRepository;
    private final PaymentRepository paymentRepository;
    private final UserService userService;

    public BookingDTO.ShowSeatsDTO showBookingSeats(long movieId, long branchId,
                                       LocalDate screeningDate, String theaterName,
                                       LocalTime screeningTime){
        Movie movie = movieRepository.getMovie(movieId);
        Branch branch = branchRepository.findBranch(branchId);
        Theater theater = theaterRepository.findTheater(theaterName, branch);
        Screening screening = screeningRepository.getScreening(screeningDate, screeningTime, movie, theater);
        
        List<Seat> bookedSeats = bookingRepository.bookedSeats(screening, theater);


        return getShowSeatsDTO(theaterName, branch, bookedSeats);
    }

    private static BookingDTO.ShowSeatsDTO getShowSeatsDTO(String theaterName, Branch branch, List<Seat> bookedSeats) {
        BookingDTO.ShowSeatsDTO showSeatsDTO = new BookingDTO.ShowSeatsDTO();
        showSeatsDTO.setTheaterName(theaterName +"관");
        showSeatsDTO.setBranchName(branch.getName());
        List<SeatDTO.Seats> seatsDTOList = new ArrayList<>();
        for (Seat seat : bookedSeats) {
            SeatDTO.Seats seats = new SeatDTO.Seats();
            seats.setRow(seat.getRow());
            seats.setNum(seat.getNumber());
            seatsDTOList.add(seats);
        }
        showSeatsDTO.setSeats(seatsDTOList);
        return showSeatsDTO;
    }


    private String generateBookingNumber(Long movieId, LocalDate screeningDate, LocalTime screeningTime) {
        // 각 부분 생성
        String partA = String.format("%04d", movieId % 10000); // 영화 ID의 마지막 4자리
        String partB = screeningDate.format(DateTimeFormatter.ofPattern("MMdd")); // 상영 날짜
        String partC = screeningTime.format(DateTimeFormatter.ofPattern("HHmm")); // 상영 시간
        String partD = String.format("%04d", new Random().nextInt(10000)); // 랜덤한 4자리 숫자

        // 각 부분을 결합하여 최종 예매 번호 생성
        return String.join("-", partA, partB, partC, partD);
    }


    //row - A,B,C... column - 1,2,3...
    @Transactional
    public BookingDTO.checkBooking booking(String token, Long movieId, List<SeatDTO.Seats> seats,int paymentAmount, String paymentType, String easyPaymentType,
                        List<BookingDTO.TicketsCategory> ticketsCategories,
                        String theaterNum, Long branchId, LocalDate ScreeningDate, LocalTime screeningTime) {
        User user = userService.tokenToUser(token);
        Movie movie = movieRepository.getMovie(movieId);
        Branch branch = branchRepository.findBranch(branchId);
        Theater theater = theaterRepository.findTheater(theaterNum, branch);
        Screening screening = screeningRepository.getScreening(ScreeningDate, screeningTime, movie, theater);

        // 영화 ID, 상영 날짜, 상영 시간을 기반으로 예매 번호 생성
        String bookingNumber = generateBookingNumber(movieId, ScreeningDate, screeningTime);
        Payment payment = new Payment(bookingNumber, paymentAmount, user, paymentType, easyPaymentType);
        paymentRepository.save(payment);
        int seatIndex = 0;
        for (BookingDTO.TicketsCategory ticketsCategory : ticketsCategories) {
            for (int i=0;i<ticketsCategory.getTicketCount();i++){
                if (seatIndex >= seats.size()) {
                    throw new IllegalArgumentException("선택한 좌석 수와 티켓 수가 일치하지 않습니다.");
                }

                SeatDTO.Seats seat = seats.get(seatIndex);
                Seat findSeat = seatRepository.getSeat(seat.getRow(), seat.getNum(), theater);
                int ticketPrice = getTicketPriceByType(ticketsCategory.getTicketType());

                Booking booking = new Booking(screening, findSeat, user, ticketsCategory.getTicketType(), ticketPrice, payment);
//                booking.setBookingNumber(bookingNumber);
                bookingRepository.save(booking);

                seatIndex++;
            }
        }

        BookingDTO.checkBooking response= new BookingDTO.checkBooking();
        response.setBookingNum(bookingNumber);
        response.setScreeningDate(ScreeningDate);
        response.setScreeningTime(screeningTime);
        response.setUserId(user.getUserId());
        response.setMovieTitle(movie.getTitle());
        response.setTheaterName(theater.getName()+'관');
        response.setBranchName(branch.getName());
        response.setSeats(seats);
        response.setPaymentType(paymentType);
        response.setEasyPaymentType(easyPaymentType);
        response.setTicketsCategory(ticketsCategories);
        response.setBookingDate(payment.getPaymentTime());
        return response;
    }

    private int getTicketPriceByType(String ticketType) {
        return switch (ticketType) {
            case "일반" -> 15000;
            case "청소년" -> 12000;
            case "경로" -> 7000;
            case "우대" -> 5000;
            default -> throw new IllegalArgumentException("알 수 없는 티켓 종류입니다: " + ticketType);
        };
    }


}
