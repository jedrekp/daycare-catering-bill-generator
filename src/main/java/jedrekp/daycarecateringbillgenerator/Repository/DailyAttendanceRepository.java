package jedrekp.daycarecateringbillgenerator.Repository;

import jedrekp.daycarecateringbillgenerator.Entity.DailyAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyAttendanceRepository extends JpaRepository<DailyAttendance, Long> {

    @Query("SELECT da FROM DailyAttendance da " +
            "LEFT JOIN FETCH da.presentChildren pc LEFT JOIN FETCH da.absentChildren ac" +
            " WHERE da.date = :date ")
    Optional<DailyAttendance> findByDateWithChildren(@Param("date") LocalDate date);

    @Query("SELECT da FROM DailyAttendance da INNER JOIN FETCH da.presentChildren pc INNER JOIN pc.daycareGroup dg " +
            "WHERE da.date = :date AND dg.id = :daycareGroupId")
    Optional<DailyAttendance> findByDateAndDaycareGroupIdWithPresentChildren(
            @Param("date") LocalDate date, @Param("daycareGroupId") Long daycareGroupId);

    @Query("SELECT da FROM DailyAttendance da LEFT JOIN da.presentChildren pc " +
            "WHERE  pc.id = :childId AND MONTH(da.date) = :month AND YEAR(da.date) = :year " +
            "ORDER BY da.date ASC")
    List<DailyAttendance> findByPresentChildIdForSpecificMonth(@Param("childId") Long childId,
                                                        @Param("month") int month, @Param("year") int year);

    @Query("SELECT da FROM DailyAttendance da LEFT JOIN da.absentChildren pc " +
            "WHERE  pc.id = :childId AND MONTH(da.date) = :month AND YEAR(da.date) = :year " +
            "ORDER BY da.date ASC")
    List<DailyAttendance> findByAbsentChildIdForSpecificMonth(@Param("childId") Long childId,
                                                               @Param("month") int month, @Param("year") int year);
}
