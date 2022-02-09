package jedrekp.daycarecateringbillgenerator.repository;

import jedrekp.daycarecateringbillgenerator.entity.AssignedOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AssignedOptionRepository extends JpaRepository<AssignedOption, Long> {

    boolean existsByEffectiveDateAndChildId(LocalDate effectiveDate, Long childId);

    Optional<AssignedOption> findByEffectiveDateAndChildId(LocalDate effectiveDate, Long childId);

}
