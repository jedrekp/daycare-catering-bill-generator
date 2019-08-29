package jedrekp.daycarecateringbillgenerator.Repository;

import jedrekp.daycarecateringbillgenerator.Entity.DaycareGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DaycareGroupRepository extends JpaRepository<DaycareGroup, Long> {
}
