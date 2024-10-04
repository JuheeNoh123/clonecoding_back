package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Branch;
import CGVcloneCoding.cloneCoding.domain.Theater;

import java.util.List;

public interface TheaterRepository {
    List<Theater> findAll();

    Theater findTheater(String TheaterNum, Branch branch);
}
