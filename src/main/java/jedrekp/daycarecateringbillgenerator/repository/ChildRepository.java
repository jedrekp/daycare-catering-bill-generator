package jedrekp.daycarecateringbillgenerator.repository;

import jedrekp.daycarecateringbillgenerator.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndIdNot(String firstName, String lastName, Long childId);

    boolean existsByIdAndDaycareGroupGroupSupervisorUsername(Long childId, String username);

    Optional<Child> findByIdAndArchived(Long childId, boolean archived);

    Optional<Child> findByIdAndDaycareGroupId(Long childId, Long daycareGroupId);

    @Query("SELECT c FROM Child c LEFT JOIN FETCH c.daycareGroup dg " +
            "LEFT JOIN FETCH dg.groupSupervisor " +
            "LEFT JOIN FETCH c.assignedOptions ao  LEFT JOIN FETCH ao.cateringOption " +
            "WHERE c.id = :childId")
    Optional<Child> findByIdWithAllDetails(Long childId);

    @Query("SELECT c FROM Child c " +
            "LEFT JOIN FETCH c.assignedOptions ao  LEFT JOIN FETCH ao.cateringOption " +
            "WHERE c.id = :childId AND c.archived = :archived")
    Optional<Child> findByIdAndArchivedWithAssignedOptions(Long childId, boolean archived);

    List<Child> findAllByArchived(boolean archived);

    List<Child> findAllByDaycareGroupIdAndArchived(
            Long daycareGroupId, boolean archived);

    @Query("SELECT pc FROM AttendanceSheet att INNER JOIN att.presentChildren pc INNER JOIN pc.daycareGroup dg " +
            "WHERE att.date = :date AND dg.id = :daycareGroupId")
    List<Child> findPresentChildrenByDateAndDaycareGroupId(LocalDate date, Long daycareGroupId);

    @Query("SELECT ac FROM AttendanceSheet att INNER JOIN att.absentChildren ac INNER JOIN ac.daycareGroup dg " +
            "WHERE att.date = :date AND dg.id = :daycareGroupId")
    List<Child> findAbsentChildrenByDateAndDaycareGroupId(LocalDate date, Long daycareGroupId);

}
