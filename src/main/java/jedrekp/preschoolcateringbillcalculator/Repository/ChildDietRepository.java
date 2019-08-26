package jedrekp.preschoolcateringbillcalculator.Repository;

import jedrekp.preschoolcateringbillcalculator.Entity.ChildDiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ChildDietRepository extends JpaRepository<ChildDiet, Long> {

    Optional<ChildDiet> findByEffectiveDateAndChild_Id(LocalDate effectiveDate, Long childId);
}
