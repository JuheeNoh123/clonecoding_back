package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Seat;
import CGVcloneCoding.cloneCoding.domain.Theater;

import java.util.List;

public interface SeatRepository {
    Seat getSeat(String seatRow, int seatCol, Theater theater);

    Long totalSeatCounting(Theater theater);

    List<Seat> getALLSeatsByTheater(Theater theater);
}
