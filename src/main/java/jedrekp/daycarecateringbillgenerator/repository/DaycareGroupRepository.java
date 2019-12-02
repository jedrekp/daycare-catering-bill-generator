package jedrekp.daycarecateringbillgenerator.repository;

import jedrekp.daycarecateringbillgenerator.entity.DaycareGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DaycareGroupRepository extends JpaRepository<DaycareGroup, Long> {

    boolean existsByGroupName(String groupName);

    boolean existsByGroupNameAndIdNot(String groupName, Long id);

    @Query("SELECT dg FROM DaycareGroup dg LEFT JOIN FETCH dg.children " +
            "WHERE dg.id = :daycareGroupId")
    Optional<DaycareGroup> findByIdWithChildren(@Param("daycareGroupId") Long daycareGroupId);

    List<DaycareGroup> findAllByOrderByGroupNameAsc();
}
