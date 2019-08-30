package jedrekp.daycarecateringbillgenerator.Repository;

import jedrekp.daycarecateringbillgenerator.Entity.DaycareGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DaycareGroupRepository extends JpaRepository<DaycareGroup, Long> {

    @Query("SELECT dg FROM DaycareGroup dg LEFT JOIN FETCH dg.children " +
            "WHERE dg.id = :daycareGroupId")
    Optional<DaycareGroup> findByIdWithChildren(@Param("daycareGroupId") Long daycareGroupId);
}
