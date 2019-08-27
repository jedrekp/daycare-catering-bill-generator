package jedrekp.preschoolcateringbillcalculator.Service;

import jedrekp.preschoolcateringbillcalculator.DTO.AssignedOptionDTO;
import jedrekp.preschoolcateringbillcalculator.Entity.AssignedOption;
import jedrekp.preschoolcateringbillcalculator.Entity.CateringOption;
import jedrekp.preschoolcateringbillcalculator.Entity.Child;
import jedrekp.preschoolcateringbillcalculator.Entity.PreschoolGroup;
import jedrekp.preschoolcateringbillcalculator.Repository.AssignedOptionRepository;
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
    AssignedOptionRepository assignedOptionRepository;

    @Autowired
    CateringOptionService cateringOptionService;

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
    public Child assignCateringOption(Long childId, AssignedOptionDTO assignedOptionDTO) {
        Child child = findByIdWithAllDetails(childId);
        CateringOption cateringOption = cateringOptionService.findById(assignedOptionDTO.getCateringOptionId());
        if (cateringOption.isDisabled()) {
            throw new IllegalArgumentException("Catering Option:  " + cateringOption.getOptionName() + " is disabled." +
                    " It can no longer be assigned");
        }
        getAssignedOptionByEffectiveDateIfExistsAlready(child, assignedOptionDTO.getEffectiveDate())
                .ifPresentOrElse(assignedOption -> assignedOption.setCateringOption(cateringOption),
                        () -> {
                            AssignedOption assignedOption = new AssignedOption(
                                    assignedOptionDTO.getEffectiveDate(), child, cateringOption);
                            child.getAssignedOptions().add(assignedOption);
                            assignedOptionRepository.save(assignedOption);
                        });
        return child;
    }

    @Transactional
    public void removeAssignedOption(Long childId, Long assignedOptionId) {
        Child child = findById(childId);
        AssignedOption assignedOption = assignedOptionRepository.findById(assignedOptionId)
                .orElseThrow(EntityNotFoundException::new);
        child.getAssignedOptions().remove(assignedOption);
        assignedOptionRepository.delete(assignedOption);
    }

    private Optional<AssignedOption> getAssignedOptionByEffectiveDateIfExistsAlready(Child child, LocalDate effectiveDate) {
        return child.getAssignedOptions()
                .stream()
                .filter(assignedOption -> assignedOption.getEffectiveDate().equals(effectiveDate))
                .findAny();
    }

}
