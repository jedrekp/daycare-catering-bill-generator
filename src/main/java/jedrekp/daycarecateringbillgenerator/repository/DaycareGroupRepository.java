package jedrekp.daycarecateringbillgenerator.repository;

import jedrekp.daycarecateringbillgenerator.entity.DaycareGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DaycareGroupRepository extends JpaRepository<DaycareGroup, Long> {

    boolean existsByGroupNameIgnoreCase(String groupName);

    boolean existsByGroupNameIgnoreCaseAndIdNot(String groupName, Long childId);

    boolean existsByIdAndGroupSupervisorUsername(Long daycareGroupId, String groupSupervisorUsername);

    Optional<DaycareGroup> findByIdAndGroupSupervisorId(Long daycareGroupId, Long groupSupervisorId);

    @Query("SELECT dg FROM DaycareGroup dg LEFT JOIN FETCH dg.children " +
            "LEFT JOIN FETCH dg.groupSupervisor " +
            "WHERE dg.id = :daycareGroupId")
    Optional<DaycareGroup> findByIdWithAllDetails(Long daycareGroupId);

    List<DaycareGroup> findAllByGroupSupervisorId(Long groupSupervisorId);

}
