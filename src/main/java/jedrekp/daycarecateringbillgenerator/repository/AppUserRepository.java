package jedrekp.daycarecateringbillgenerator.repository;

import jedrekp.daycarecateringbillgenerator.entity.AppUser;
import jedrekp.daycarecateringbillgenerator.utility.DaycareRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByUsernameIgnoreCase(String username);

    @Query("SELECT au FROM AppUser au LEFT JOIN FETCH au.daycareGroup " +
            "WHERE au.id = :appUserId")
    Optional<AppUser> findByIdWithAllDetails(Long appUserId);

    @Query("SELECT au FROM AppUser au LEFT JOIN FETCH au.daycareGroup " +
            "WHERE au.username = :username")
    Optional<AppUser> findByUsername(String username);

    @Query("SELECT au FROM AppUser au LEFT JOIN FETCH au.daycareGroup " +
            "WHERE au.id = :appUserId AND au.daycareRole = :daycareRole")
    Optional<AppUser> findByIdAndDaycareRole(Long appUserId, DaycareRole daycareRole);

    List<AppUser> findAllByDaycareRoleOrderByLastNameAscFirstNameAsc(DaycareRole daycareRole);
}
