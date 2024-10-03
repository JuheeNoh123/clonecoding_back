package CGVcloneCoding.cloneCoding.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

public class SeatDTO {
    @Data
    public static class Seats {
        public String row;
        public int num;
    }
}
