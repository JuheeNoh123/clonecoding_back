package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Seat;
import CGVcloneCoding.cloneCoding.domain.Theater;

public interface SeatRepository {
    Seat getSeat(String seatRow, int seatCol, Theater theater);
}
