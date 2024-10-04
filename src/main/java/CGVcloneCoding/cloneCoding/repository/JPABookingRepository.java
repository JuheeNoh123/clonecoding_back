package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Booking;
import CGVcloneCoding.cloneCoding.domain.Screening;
import CGVcloneCoding.cloneCoding.domain.Seat;
import CGVcloneCoding.cloneCoding.domain.Theater;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JPABookingRepository implements BookingRepository {
    private final EntityManager em;
    @Override
    public void save(Booking booking) {
        em.persist(booking);
    }

    @Override
    public List<Seat> bookedSeats(Screening screening, Theater theater){
        //Select * from Seat s join Booking b where b.screening_id = 6356 and s.theater_id = 1 and s.id=b.seat_id;
        String query = "SELECT s FROM Seat s " +
                "JOIN Booking b ON s.id = b.seat.id " +
                "WHERE b.screening = :screening " +
                "AND s.theater = :theater";
        return em.createQuery(query, Seat.class)
                .setParameter("screening", screening)
                .setParameter("theater", theater)
                .getResultList();
    }
}
