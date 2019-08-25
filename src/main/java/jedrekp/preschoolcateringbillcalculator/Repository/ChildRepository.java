package jedrekp.preschoolcateringbillcalculator.Repository;

import jedrekp.preschoolcateringbillcalculator.Entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    Collection<Child> findByPreschoolGroup_Id(Long preschoolGroupId);

    @Query("SELECT c FROM Child c LEFT JOIN FETCH c.preschoolGroup " +
            "WHERE c.id = :childId")
    Optional<Child> findByIdWithPreschoolGroup(@Param("childId") Long childId);

    @Query("SELECT c FROM Child c LEFT JOIN FETCH c.assignedDiets ad  LEFT JOIN FETCH ad.diet " +
            "WHERE c.id = :childId")
    Optional<Child> findByIdWithChosenDiets(@Param("childId") Long childId);

    @Query("SELECT c FROM Child c LEFT JOIN FETCH c.preschoolGroup " +
            "LEFT JOIN FETCH c.assignedDiets ad  LEFT JOIN FETCH ad.diet " +
            "WHERE c.id = :childId")
    Optional<Child> findByIdWithAllDetails(@Param("childId") Long childId);
}
