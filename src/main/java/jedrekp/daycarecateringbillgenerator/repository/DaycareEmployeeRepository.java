package jedrekp.daycarecateringbillgenerator.repository;

import jedrekp.daycarecateringbillgenerator.entity.DaycareEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DaycareEmployeeRepository extends JpaRepository<DaycareEmployee, Long> {

    boolean existsByAppUsernameIgnoreCase(String appUsername);

    Optional<DaycareEmployee> findByAppUsername(String appUsername);
}
