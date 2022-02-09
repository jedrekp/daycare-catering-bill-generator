package jedrekp.daycarecateringbillgenerator.repository;

import jedrekp.daycarecateringbillgenerator.entity.CateringOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CateringOptionRepository extends JpaRepository<CateringOption, Long> {

    boolean existsByOptionNameIgnoreCase(String optionName);

    boolean existsByOptionNameIgnoreCaseAndIdNot(String optionName, Long cateringOptionId);

    @Query(value = "SELECT co.* FROM assigned_option ao " +
            "INNER JOIN catering_option co ON co.id = ao.catering_option_id " +
            "WHERE ao.effective_date <= :date AND ao.child_id = :childId " +
            "ORDER BY ao.effective_date DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<CateringOption> findOptionInEffectByChildIdAndDate(Long childId, LocalDate date);

    List<CateringOption> findAllByDisabledOrderByOptionNameAsc(boolean disabled);
}
