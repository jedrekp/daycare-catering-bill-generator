package jedrekp.daycarecateringbillgenerator.Repository;

import jedrekp.daycarecateringbillgenerator.Entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    Collection<Child> findByDaycareGroup_Id(Long daycareGroupId);

    @Query("SELECT c FROM Child c LEFT JOIN FETCH c.daycareGroup " +
            "LEFT JOIN FETCH c.assignedOptions ao  LEFT JOIN FETCH ao.cateringOption " +
            "WHERE c.id = :childId")
    Optional<Child> findByIdWithAllDetails(@Param("childId") Long childId);
}
