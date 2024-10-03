package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Branch;
import CGVcloneCoding.cloneCoding.domain.Movie;
import CGVcloneCoding.cloneCoding.domain.Screening;
import CGVcloneCoding.cloneCoding.domain.Theater;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScreeningRepository {
    void save(Screening screeningEntry);


    List<LocalDate> availableDate(Movie movie, Branch branch);

    List<Object[]> availableTheaterSeats(Movie movie, Branch branch, LocalDate screeningDate);

    Screening getScreening(LocalDate screeningDate, LocalTime startTime, Movie movie, Theater theater);
}
