package jedrekp.daycarecateringbillgenerator.repository;

import jedrekp.daycarecateringbillgenerator.entity.DaycareEmployee;
import jedrekp.daycarecateringbillgenerator.utility.DaycareRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DaycareEmployeeRepository extends JpaRepository<DaycareEmployee, Long> {

    boolean existsByAppUsernameIgnoreCase(String appUsername);

    boolean existsByAppUsernameAndDaycareGroup_Id(String appUsername, long daycareGroupId);

    Optional<DaycareEmployee> findByAppUsername(String appUsername);

    @Query("SELECT de FROM DaycareEmployee de LEFT JOIN FETCH DaycareGroup dg " +
            "WHERE de.id = :daycareEmployeeId")
    Optional<DaycareEmployee> findByIdWithAllDetails(@Param("daycareEmployeeId") Long daycareEmployeeId);

    List<DaycareEmployee> findAllByDaycareRoleOrderByLastNameAscFirstNameAsc(DaycareRole daycareRole);
}
