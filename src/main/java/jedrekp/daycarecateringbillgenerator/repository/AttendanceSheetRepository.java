package jedrekp.daycarecateringbillgenerator.repository;

import jedrekp.daycarecateringbillgenerator.entity.AttendanceSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceSheetRepository extends JpaRepository<AttendanceSheet, Long> {

    @Query("SELECT att FROM AttendanceSheet att " +
            "LEFT JOIN FETCH att.presentChildren pc LEFT JOIN FETCH att.absentChildren ac" +
            " WHERE att.date = :date ")
    Optional<AttendanceSheet> findByDateWithChildren(LocalDate date);

    @Query("SELECT att FROM AttendanceSheet att LEFT JOIN att.presentChildren pc " +
            "WHERE  pc.id = :childId AND MONTH(att.date) = :month AND YEAR(att.date) = :year " +
            "ORDER BY att.date ASC")
    List<AttendanceSheet> findByPresentChildIdForSpecificMonth(Long childId, int month, int year);

    @Query("SELECT att FROM AttendanceSheet att LEFT JOIN att.absentChildren pc " +
            "WHERE  pc.id = :childId AND MONTH(att.date) = :month AND YEAR(att.date) = :year " +
            "ORDER BY att.date ASC")
    List<AttendanceSheet> findByAbsentChildIdForSpecificMonth(Long childId, int month, int year);
}
