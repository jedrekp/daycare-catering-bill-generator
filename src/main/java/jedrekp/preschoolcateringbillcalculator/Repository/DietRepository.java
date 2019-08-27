package jedrekp.preschoolcateringbillcalculator.Repository;

import jedrekp.preschoolcateringbillcalculator.Entity.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

    @Query(value = "SELECT d.id, d.diet_name, d.daily_cost FROM assigned_diet ad " +
            "INNER JOIN diet d ON d.id = ad.diet_id " +
            "WHERE ad.effective_date <= :date AND ad.child_id = :childId " +
            "ORDER BY ad.effective_date DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Diet> findCurrentlyAssignedDietByChildIdAndDate(@Param("childId") Long childId,
                                                             @Param("date") LocalDate date);
}
