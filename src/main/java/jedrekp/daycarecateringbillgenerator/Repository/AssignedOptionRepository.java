package jedrekp.daycarecateringbillgenerator.Repository;

import jedrekp.daycarecateringbillgenerator.Entity.AssignedOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AssignedOptionRepository extends JpaRepository<AssignedOption, Long> {

    Optional<AssignedOption> findByEffectiveDateAndChild_Id(LocalDate effectiveDate, Long childId);

}
