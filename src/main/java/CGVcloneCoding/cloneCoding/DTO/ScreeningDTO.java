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
        public theaterSeats(String theaterNum, LocalTime startTime, LocalTime endTime, String branch) {
            this.theaterNum = theaterNum;
            this.startTime = startTime;
            this.endTime = endTime;
            this.branch = branch;
        }
    }
}
