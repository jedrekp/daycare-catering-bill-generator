package jedrekp.daycarecateringbillgenerator.repository;

import jedrekp.daycarecateringbillgenerator.entity.AssignedOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AssignedOptionRepository extends JpaRepository<AssignedOption, Long> {

    boolean existsByEffectiveDateAndChild_Id(LocalDate effectiveDate, Long childId);

}
