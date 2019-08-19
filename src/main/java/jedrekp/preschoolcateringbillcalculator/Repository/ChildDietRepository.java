package jedrekp.preschoolcateringbillcalculator.Repository;

import jedrekp.preschoolcateringbillcalculator.Entity.ChildDiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildDietRepository extends JpaRepository<ChildDiet, Long> {
}
