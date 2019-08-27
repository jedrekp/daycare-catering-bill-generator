package jedrekp.preschoolcateringbillcalculator.Repository;

import jedrekp.preschoolcateringbillcalculator.Entity.AssignedDiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AssignedDietRepository extends JpaRepository<AssignedDiet, Long> {

    Optional<AssignedDiet> findByEffectiveDateAndChild_Id(LocalDate effectiveDate, Long childId);
}
