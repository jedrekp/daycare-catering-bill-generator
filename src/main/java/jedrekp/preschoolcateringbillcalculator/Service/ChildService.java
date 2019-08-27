package jedrekp.preschoolcateringbillcalculator.Service;

import jedrekp.preschoolcateringbillcalculator.DTO.AssignedDietDTO;
import jedrekp.preschoolcateringbillcalculator.Entity.AssignedDiet;
import jedrekp.preschoolcateringbillcalculator.Entity.Child;
import jedrekp.preschoolcateringbillcalculator.Entity.Diet;
import jedrekp.preschoolcateringbillcalculator.Entity.PreschoolGroup;
import jedrekp.preschoolcateringbillcalculator.Repository.AssignedDietRepository;
import jedrekp.preschoolcateringbillcalculator.Repository.ChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Service
public class ChildService {

    @Autowired
    ChildRepository childRepository;

    @Autowired
    AssignedDietRepository assignedDietRepository;

    @Autowired
    DietService dietService;

    @Autowired
    PreschoolGroupService preschoolGroupService;


    public Child save(Child child) {
        return childRepository.save(child);
    }

    @Transactional
    public Child editChild(Long childId, Child child) {
        Child childToEdit = findById(childId);
        childToEdit.setFirstName(child.getFirstName());
        childToEdit.setLastName(child.getLastName());
        return childToEdit;
    }

    public Child findById(Long childId) {
        return childRepository.findById(childId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Child findByIdWithAllDetails(Long childId) {
        return childRepository.findByIdWithAllDetails(childId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Collection<Child> findChildrenFromGroup(Long preschoolGroupId) {
        return childRepository.findByPreschoolGroup_Id(preschoolGroupId);
    }

    @Transactional
    public Child assignToPreschoolGroup(Long childId, Long preschoolGroupId) {
        Child child = findByIdWithAllDetails(childId);
        PreschoolGroup oldGroup = child.getPreschoolGroup();
        if (oldGroup != null) {
            oldGroup.getChildren().remove(child);
        }
        PreschoolGroup newGroup = preschoolGroupService.findById(preschoolGroupId);
        child.setPreschoolGroup(newGroup);
        newGroup.getChildren().add(child);
        return child;
    }

    @Transactional
    public Child assignDiet(Long childId, AssignedDietDTO assignedDietDTO) {
        Child child = findByIdWithAllDetails(childId);
        Diet diet = dietService.findById(assignedDietDTO.getDietId());
        getAssignedDietByEffectiveDateIfExistsAlready(child, assignedDietDTO.getEffectiveDate())
                .ifPresentOrElse(assignedDiet -> assignedDiet.setDiet(diet),
                        () -> {
                            AssignedDiet assignedDiet = new AssignedDiet(
                                    assignedDietDTO.getEffectiveDate(), child, diet);
                            child.getAssignedDiets().add(assignedDiet);
                            assignedDietRepository.save(assignedDiet);
                        });
        return child;
    }

    private Optional<AssignedDiet> getAssignedDietByEffectiveDateIfExistsAlready(Child child, LocalDate effectiveDate) {
        return child.getAssignedDiets()
                .stream()
                .filter(assignedDiet -> assignedDiet.getEffectiveDate().equals(effectiveDate))
                .findAny();
    }
}
