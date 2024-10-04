package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Seat;
import CGVcloneCoding.cloneCoding.domain.Theater;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JPASeatRepository implements SeatRepository {
    private final EntityManager em;

    @Override
    public Seat getSeat(String seatRow, int seatCol, Theater theater){
        String query = "Select s from Seat s " +
                "where row = :seatRow " +
                "AND number = :seatCol " +
                "AND theater = :theater";
        return em.createQuery(query, Seat.class)
                .setParameter("seatRow", seatRow)
                .setParameter("seatCol", seatCol)
                .setParameter("theater", theater)
                .getSingleResult();
    }

    @Override
    public Long totalSeatCounting(Theater theater){
        String query = "Select count(*) from Seat where theater = :theater";
        return em.createQuery(query, Long.class)
                .setParameter("theater", theater)
                .getSingleResult();
    }

    @Override
    public List<Seat> getALLSeatsByTheater(Theater theater){
        String query = "Select s from Seat s where theater = :theater";
        return em.createQuery(query, Seat.class)
                .setParameter("theater", theater)
                .getResultList();
    }

}
