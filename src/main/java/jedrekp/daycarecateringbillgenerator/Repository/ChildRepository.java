package jedrekp.daycarecateringbillgenerator.Repository;

import jedrekp.daycarecateringbillgenerator.Entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByFirstNameAndLastNameAndIdNot(String firstName, String lastName, Long id);

    Optional<Child> findByIdAndArchived(Long childId, boolean archived);

    @Query("SELECT c FROM Child c " +
            "LEFT JOIN FETCH c.assignedOptions ao  LEFT JOIN FETCH ao.cateringOption " +
            "WHERE c.id = :childId AND c.archived = false")
    Optional<Child> findByIdWithAssignedOptions(@Param("childId") Long childId);

    @Query("SELECT c FROM Child c LEFT JOIN FETCH c.daycareGroup " +
            "LEFT JOIN FETCH c.assignedOptions ao  LEFT JOIN FETCH ao.cateringOption " +
            "WHERE c.id = :childId")
    Optional<Child> findByIdWithAllDetails(@Param("childId") Long childId);

    List<Child> findAllByArchivedOrderByLastNameAscFirstNameAsc(boolean archived);

    List<Child> findAllByDaycareGroup_IdAndArchivedOrderByLastNameAscFirstNameAsc(
            Long daycareGroupId, boolean archived);

    @Query("SELECT c FROM Child c WHERE LOWER(c.firstName) IN :searchSubPhrases " +
            "AND LOWER(c.lastName) IN :searchSubPhrases ORDER BY c.lastName ASC, c.firstName ASC")
    List<Child> findAllByFirstNameAndLastName(@Param("searchSubPhrases") Collection<String> searchSubPhrases);

    @Query("SELECT c FROM Child c WHERE LOWER(c.lastName) IN :searchSubPhrases " +
            "ORDER BY c.lastName ASC, c.firstName ASC")
    List<Child> findAllByLastName(@Param("searchSubPhrases") Collection<String> searchSubPhrases);

    @Query("SELECT c FROM Child c WHERE LOWER(c.firstName) IN :searchSubPhrases " +
            "ORDER BY c.lastName ASC, c.firstName ASC")
    List<Child> findAllByFirstName(@Param("searchSubPhrases") Collection<String> searchSubPhrases);

}
