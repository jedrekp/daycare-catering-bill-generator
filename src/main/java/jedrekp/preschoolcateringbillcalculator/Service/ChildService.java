package jedrekp.preschoolcateringbillcalculator.Service;

import jedrekp.preschoolcateringbillcalculator.Entity.Child;
import jedrekp.preschoolcateringbillcalculator.Entity.ChildDiet;
import jedrekp.preschoolcateringbillcalculator.Entity.Diet;
import jedrekp.preschoolcateringbillcalculator.Entity.PreschoolGroup;
import jedrekp.preschoolcateringbillcalculator.Repository.ChildDietRepository;
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
    ChildDietRepository childDietRepository;

    @Autowired
    DietService dietService;

    @Autowired
    PreschoolGroupService preschoolGroupService;


    public Child save(Child child) {
        return childRepository.save(child);
    }

    public Child findById(Long childId) {
        return childRepository.findById(childId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Child editChild(Long childId, Child child) {
        Child childToEdit = findById(childId);
        childToEdit.setFirstName(child.getFirstName());
        childToEdit.setLastName(child.getLastName());
        return childToEdit;
    }

    @Transactional(readOnly = true)
    public Child findByIdWithAllDetails(Long childId) {
        return childRepository.findByIdWithAllDetails(childId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Child findByIdWithChosenDiets(Long childId) {
        return childRepository.findByIdWithChosenDiets(childId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Collection<Child> findChildrenFromGroup(Long preschoolGroupId) {
        return childRepository.findByPreschoolGroup_Id(preschoolGroupId);
    }

    @Transactional
    public Child assignToPreschoolGroup(Long childId, Long preschoolGroupId) {
        Child child = childRepository.findByIdWithAllDetails(childId).orElseThrow(EntityNotFoundException::new);
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
    public Child assignDiet(Long childId, Long dietId, LocalDate effectiveDate) {
        Child child = findByIdWithAllDetails(childId);

        throwExceptionIfDietWithSameEffectiveDateAlreadyAssigned(child, effectiveDate);
        throwExceptionIfNewDietAlreadyInEffectForGivenDate(child, dietId, effectiveDate);
        deleteNextAssignedDietIfTheSameAsNew(child, dietId, effectiveDate);

        Diet diet = dietService.findById(dietId);
        ChildDiet childDiet = new ChildDiet(effectiveDate, child, diet);
        child.getAssignedDiets().add(childDiet);
        childDietRepository.save(childDiet);
        return child;
    }

    private void throwExceptionIfDietWithSameEffectiveDateAlreadyAssigned(Child child, LocalDate effectiveDate) {
        Optional<ChildDiet> assignedDietWithSameEffectiveDate = child.getAssignedDiets()
                .stream().filter(childDiet -> childDiet.getEffectiveDate().equals(effectiveDate))
                .findAny();
        assignedDietWithSameEffectiveDate.ifPresent(childDiet -> {
            throw new IllegalArgumentException("This child already has a diet with chosen effective date. Please remove it first.");
        });
    }

    private void throwExceptionIfNewDietAlreadyInEffectForGivenDate(Child child, Long newDietId, LocalDate effectiveDate) {
        Optional<Diet> dietInEffectIfSameAsNew = child.getAssignedDiets()
                .stream()
                .filter(childDiet -> childDiet.getEffectiveDate().compareTo(effectiveDate) < 0)
                .max(ChildDiet::compareTo)
                .map(ChildDiet::getDiet)
                .filter(diet -> diet.getId().equals(newDietId));

        dietInEffectIfSameAsNew.ifPresent(diet -> {
            throw new IllegalArgumentException("Diet: '" + diet.getDietName() + "' is already in effect for given date");
        });
    }


    private void deleteNextAssignedDietIfTheSameAsNew(Child child, Long newDietId, LocalDate effectiveDate) {
        Optional<ChildDiet> nextAssignedDietIfSameAsNew = child.getAssignedDiets()
                .stream()
                .filter(childDiet -> childDiet.getEffectiveDate().compareTo(effectiveDate) > 0)
                .min(ChildDiet::compareTo)
                .filter(childDiet -> childDiet.getDiet().getId().equals(newDietId));

        nextAssignedDietIfSameAsNew.ifPresent(childDiet -> {
            child.getAssignedDiets().remove(childDiet);
            childDietRepository.deleteById(childDiet.getId());
        });
    }

}
