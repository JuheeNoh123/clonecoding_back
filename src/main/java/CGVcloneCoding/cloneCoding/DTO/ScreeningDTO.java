package CGVcloneCoding.cloneCoding.DTO;

import lombok.Data;

import java.time.LocalTime;

public class ScreeningDTO {

    @Data
    public static class theaterSeats {
        public String branch;
        public String theaterNum;
        public LocalTime startTime;
        public LocalTime endTime;
        public String totalSeats;
        public String remainSeats;
        public theaterSeats(String theaterNum, LocalTime startTime, LocalTime endTime, String branch, String totalSeats, String remainSeats) {
            this.theaterNum = theaterNum;
            this.startTime = startTime;
            this.endTime = endTime;
            this.totalSeats = totalSeats;
            this.remainSeats = remainSeats;
            this.branch = branch;
        }
    }
}
