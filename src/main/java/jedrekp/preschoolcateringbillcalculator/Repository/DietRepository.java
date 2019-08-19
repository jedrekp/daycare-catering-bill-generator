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

    @Query(value = "SELECT d.id, d.diet_name, d.daily_cost FROM child_diet cd " +
            "INNER JOIN diet d ON d.id = cd.diet_id " +
            "WHERE cd.effective_date <= :date AND cd.child_id = :childId " +
            "ORDER BY cd.effective_date DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Diet> findCurrentlyAssignedDietByChildIdAndDate(@Param("childId") Long childId, @Param("date") LocalDate date);
}
