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
    public Child assignDiet(Long childId, Long dietId, LocalDate date) {
        Child child = findByIdWithChosenDiets(childId);
        Diet diet = dietService.findById(dietId);
        ChildDiet childDiet = new ChildDiet(date, child, diet);
        child.getChosenDiets().add(childDiet);
        diet.getChildren().add(childDiet);
        childDietRepository.save(childDiet);
        return child;
    }
}
