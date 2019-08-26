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
        Diet diet = dietService.findById(dietId);
        childDietRepository.findByEffectiveDateAndChild_Id(effectiveDate, childId)
                .ifPresentOrElse(childDiet -> childDiet.setDiet(diet),
                        () -> {
                            ChildDiet childDiet = new ChildDiet(effectiveDate, child, diet);
                            child.getAssignedDiets().add(childDiet);
                            childDietRepository.save(childDiet);
                        });
        return child;
    }


}
