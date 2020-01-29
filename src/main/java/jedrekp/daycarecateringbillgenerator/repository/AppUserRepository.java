package jedrekp.daycarecateringbillgenerator.repository;

import jedrekp.daycarecateringbillgenerator.entity.AppUser;
import jedrekp.daycarecateringbillgenerator.utility.DaycareRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByUsernameIgnoreCase(String username);

    Optional<AppUser> findByUsername(String username);

    @Query("SELECT au FROM AppUser au LEFT JOIN FETCH au.daycareGroup " +
            "WHERE au.id = :appUserId")
    Optional<AppUser> findByIdWithAllDetails(Long appUserId);

    @Query("SELECT au FROM AppUser au LEFT JOIN FETCH au.daycareGroup dg " +
            "WHERE au.id = :appUserId AND dg.id = :daycareGroupId")
    Optional<AppUser> findByIdAndDaycareGroupIdWithAllDetails(Long appUserId, Long daycareGroupId);

    List<AppUser> findAllByDaycareRoleOrderByLastNameAscFirstNameAsc(DaycareRole daycareRole);
}
