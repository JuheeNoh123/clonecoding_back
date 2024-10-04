package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Booking;
import CGVcloneCoding.cloneCoding.domain.Screening;
import CGVcloneCoding.cloneCoding.domain.Seat;
import CGVcloneCoding.cloneCoding.domain.Theater;

import java.util.List;

public interface BookingRepository {
    void save(Booking booking);

    List<Seat> bookedSeats(Screening screening, Theater theater);
}
