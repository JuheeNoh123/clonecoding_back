package CGVcloneCoding.cloneCoding.DTO;

import CGVcloneCoding.cloneCoding.domain.*;
import lombok.Data;

import java.time.LocalDate;
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
        public Long movieId;
        public LocalDate screeningDate;
        public LocalTime screeningTime;
        public Long branchId;
        public String theaterNum;
        public List<TicketsCategory> ticketsCategory;
        public List<SeatDTO.Seats> seats;

    }

    @Data
    public static class TicketsCategory{
        public String ticketType;
        public int ticketCount;
    }

    public static class checkBooking{
        public String userId;
        public String movieTitle;
        public LocalDate screeningDate;
        public LocalTime screeningTime;
        public String branchName;
        public String theaterName;
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
}
