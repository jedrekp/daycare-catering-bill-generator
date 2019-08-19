package jedrekp.preschoolcateringbillcalculator.Repository;

import jedrekp.preschoolcateringbillcalculator.Entity.DailyAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyAttendanceRepository extends JpaRepository<DailyAttendance, Long> {

    @Query("SELECT da FROM DailyAttendance da LEFT JOIN FETCH da.presentChildren pc WHERE da.date = :date ")
    Optional<DailyAttendance> findByDateWithPresentChildren(@Param("date") LocalDate date);

    @Query("SELECT da FROM DailyAttendance da LEFT JOIN da.presentChildren pc " +
            "WHERE  pc.id = :childId AND MONTH(da.date) = :month AND YEAR(da.date) = :year " +
            "ORDER BY da.date")
    List<DailyAttendance> findByChildIdForSpecificMonth(@Param("childId") Long childId, @Param("month") Integer month, @Param("year") Integer year);

}
