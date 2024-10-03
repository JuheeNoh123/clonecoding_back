package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Branch;
import CGVcloneCoding.cloneCoding.domain.Movie;
import CGVcloneCoding.cloneCoding.domain.Screening;
import CGVcloneCoding.cloneCoding.domain.Theater;

import java.time.LocalDate;
import java.util.List;

public interface ScreeningRepository {
    void save(Screening screeningEntry);


    List<LocalDate> availableDate(Movie movie, Branch branch);

    List<Object[]> availableTheaterSeats(Movie movie, Theater theater, LocalDate screeningDate);
}
