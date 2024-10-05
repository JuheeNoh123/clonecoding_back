package CGVcloneCoding.cloneCoding.DTO;

import CGVcloneCoding.cloneCoding.domain.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class BookingDTO {
    @Data
    public static class showBookingSeats {
        public Long movieId;
        public LocalDate screeningDate;
        public LocalTime screeningTime;
        public int theaterNum;

        public showBookingSeats(Long movieId, LocalDate screeningDate, LocalTime screeningTime, int theaterNum) {
            this.movieId = movieId;
            this.screeningDate = screeningDate;
            this.screeningTime = screeningTime;
            this.theaterNum = theaterNum;
        }
    }

    @Data
    public static class bookingSeats {
        @Schema(description = "영화 아이디", example = "889737")
        public Long movieId;
        @Schema(description = "상영 날짜", example = "2024-10-04")
        public LocalDate screeningDate;
        @Schema(description = "상영 시간", example = "11:45:00")
        public LocalTime screeningTime;
        @Schema(description = "지점 아이디 (강남, 강변 ID)", example = "1")
        public Long branchId;
        @Schema(description = "상영관 몇관인지", example = "2")
        public String theaterNum;
        @Schema(description = "티켓 종류 리스트", example = "일반 2매, 청소년 1매, 경로 1매")
        public List<TicketsCategory> ticketsCategory;
        @Schema(description = "선택한 좌석 리스트", example = "[선택한 좌석들 ~]")
        public List<SeatDTO.Seats> seats;
        @Schema(description = "총 결제 액", example = "49000")
        public int paymentAmount;
        @Schema(description = "결제 페이지에서 간편결제, 신용카드 등 적혀있는 부분 중 내가 선택한 결제방법", example = "간편결제")
        public String paymentType;
        @Schema(description = "간편결제 중 네이버페이, 카카오페이 같이 한번 더 선택되는 부분 중 내가 선택한 결제 방법 ", example = "카카오페이")
        public String easyPaymentType;

    }

    @Data
    public static class TicketsCategory{
        @Schema(description = "일반/청소년/경로/우대", example = "일반")
        public String ticketType;
        @Schema(description = "몇매인지", example = "2")
        public int ticketCount;
    }
    @Data
    public static class checkBooking{
        @Schema(description = "예매 번호", example = "9737-1004-1145-8810")
        public String bookingNum;
        @Schema(description = "결제 시각", example = "2024-10-05T23:08:04.2974591")
        public LocalDateTime bookingDate;
        @Schema(description = "유저 아이디", example = "juhee")
        public String userId;
        @Schema(description = "결제 페이지에서 간편결제, 신용카드 등 적혀있는 부분 중 내가 선택한 결제방법", example = "간편 결제")
        public String paymentType;
        @Schema(description = "간편결제 중 네이버페이, 카카오페이 같이 한번 더 선택되는 부분 중 내가 선택한 결제 방법 ", example = "카카오페이")
        public String easyPaymentType;
        @Schema(description = "영화 포스터", example = "/dA1TGJPTVjlqPc8PiEE2PfvFBUp.jpg")
        public String moviePosterPath;
        @Schema(description = "영화 제목", example = "조커: 폴리 아 되")
        public String movieTitle;
        @Schema(description = "상영 날짜", example = "2024-10-04")
        public LocalDate screeningDate;
        @Schema(description = "상영 시간", example = "11:45:00")
        public LocalTime screeningTime;
        @Schema(description = "지점 이름", example = "강남")
        public String branchName;
        @Schema(description = "상영관('관'까지 붙어서 나옴)", example = "2관")
        public String theaterName;
        @Schema(description = "티켓 종류 리스트", example = "일반 2매, 청소년 1매, 경로 1매")
        public List<TicketsCategory> ticketsCategory;
        @Schema(description = "선택한 좌석 리스트", example = "[선택한 좌석들 ~]")
        public List<SeatDTO.Seats> seats;
    }

    @Data
    public static class chooseTheaterAndTime{
        public String theaterName;
        public LocalTime screeningTime;
    }

    @Data
    public static class ShowSeatsDTO{
        public String branchName;
        public String theaterName;
        public List<SeatDTO.Seats> seats;
    }

    @Data
    public static class selectSeatsDTO{
        @Schema(description = "영화 아이디", example = "889737")
        public Long movieId;
        @Schema(description = "지점 아이디 (강남, 강변 ID)", example = "1")
        public Long branchId;
        @Schema(description = "상영관 몇관인지", example = "2")
        public String theaterNum;
        @Schema(description = "상영 날짜", example = "2024-10-04")
        public LocalDate screeningDate;
        @Schema(description = "상영 시간", example = "11:45:00")
        public LocalTime screeningTime;
        @Schema(description = "선택한 좌석 리스트", example = "[선택한 좌석들 ~]")
        public List<SeatDTO.Seats> seats;
    }
}
