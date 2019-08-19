package jedrekp.preschoolcateringbillcalculator.Repository;

import jedrekp.preschoolcateringbillcalculator.Entity.PreschoolGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreschoolGroupRepository extends JpaRepository<PreschoolGroup, Long> {
}
