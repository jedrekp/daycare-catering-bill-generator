package jedrekp.daycarecateringbillgenerator.repository;

import jedrekp.daycarecateringbillgenerator.entity.AttendanceSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceSheetRepository extends JpaRepository<AttendanceSheet, Long> {

    @Query("SELECT att FROM AttendanceSheet att " +
            "LEFT JOIN FETCH att.presentChildren pc LEFT JOIN FETCH att.absentChildren ac" +
            " WHERE att.date = :date ")
    Optional<AttendanceSheet> findByDateWithChildren(@Param("date") LocalDate date);

    @Query("SELECT att FROM AttendanceSheet att INNER JOIN FETCH att.presentChildren pc INNER JOIN pc.daycareGroup dg " +
            "WHERE att.date = :date AND dg.id = :daycareGroupId")
    Optional<AttendanceSheet> findByDateAndDaycareGroupIdWithPresentChildren(
            @Param("date") LocalDate date, @Param("daycareGroupId") Long daycareGroupId);

    @Query("SELECT att FROM AttendanceSheet att LEFT JOIN att.presentChildren pc " +
            "WHERE  pc.id = :childId AND MONTH(att.date) = :month AND YEAR(att.date) = :year " +
            "ORDER BY att.date ASC")
    List<AttendanceSheet> findByPresentChildIdForSpecificMonth(@Param("childId") Long childId,
                                                               @Param("month") int month, @Param("year") int year);

    @Query("SELECT att FROM AttendanceSheet att LEFT JOIN att.absentChildren pc " +
            "WHERE  pc.id = :childId AND MONTH(att.date) = :month AND YEAR(att.date) = :year " +
            "ORDER BY att.date ASC")
    List<AttendanceSheet> findByAbsentChildIdForSpecificMonth(@Param("childId") Long childId,
                                                              @Param("month") int month, @Param("year") int year);
}
