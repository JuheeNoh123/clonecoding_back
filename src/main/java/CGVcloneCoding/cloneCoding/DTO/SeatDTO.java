package CGVcloneCoding.cloneCoding.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

public class SeatDTO {
    @Data
    public static class Seats {
        @Schema(description = "<h3>좌석 열</h3>강남 점 " +
                "<li>1관 :  A~J</li><li>2관 : A~H</li>" +
                "<br>강변 점<li>1관 : A~J</li><li>2관 : A~J</li><li>3관 : A~H</li><li>4관 : A~I</li>", example = "A")
        public String row;
        @Schema(description = "<h3>좌석 열</h3>강남 점 " +
                "<ul><li>1관 :  1~10번</li><li>2관 : 1~10번</li></ul>" +
                "<br>강변 점<li>1관 : 1~10번</li><li>2관 : 1~10번</li><li>3관 : 1~10번/li><li>4관 : 1~12번</li>", example = "1")
        public int num;
    }
}
