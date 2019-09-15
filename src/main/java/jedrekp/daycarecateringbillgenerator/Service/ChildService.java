package jedrekp.daycarecateringbillgenerator.Service;

import jedrekp.daycarecateringbillgenerator.DTO.AssignedOptionDTO;
import jedrekp.daycarecateringbillgenerator.Entity.AssignedOption;
import jedrekp.daycarecateringbillgenerator.Entity.CateringOption;
import jedrekp.daycarecateringbillgenerator.Entity.Child;
import jedrekp.daycarecateringbillgenerator.Repository.AssignedOptionRepository;
import jedrekp.daycarecateringbillgenerator.Repository.ChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;

@Service
public class ChildService {

    @Autowired
    ChildRepository childRepository;

    @Autowired
    AssignedOptionRepository assignedOptionRepository;

    @Autowired
    CateringOptionService cateringOptionService;

    @Transactional
    public Child save(Child child) {
        if (childRepository.existsByFirstNameAndLastName(child.getFirstName(), child.getLastName())) {
            throw new EntityExistsException("Another child with the same first name and last name already exists");
        }
        return childRepository.save(child);
    }

    @Transactional
    public Child editChild(Long childId, Child child) {
        if (childRepository.existsByFirstNameAndLastNameAndIdNot(child.getFirstName(), child.getLastName(), child.getId())) {
            throw new EntityExistsException("Another child with the same first name and last name already exists");
        }
        Child childToEdit = findById(childId);
        childToEdit.setFirstName(child.getFirstName());
        childToEdit.setLastName(child.getLastName());
        childToEdit.setParentEmail(child.getParentEmail());
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
    public Collection<Child> findChildrenFromGroup(Long daycareGroupId) {
        if (daycareGroupId == -1L) {
            daycareGroupId = null;
        }
        return childRepository.findByDaycareGroup_Id(daycareGroupId);
    }

    @Transactional
    public Child assignCateringOption(Long childId, AssignedOptionDTO assignedOptionDTO) {
        if (assignedOptionRepository.existsByEffectiveDateAndChild_Id(assignedOptionDTO.getEffectiveDate(), childId)) {
            throw new IllegalArgumentException(
                    "Child#" + childId + " already has another catering option assigned with this effective date.\n" +
                            "It must be removed first");
        }
        CateringOption cateringOption = cateringOptionService.findById(assignedOptionDTO.getCateringOptionId());
        if (cateringOption.isDisabled()) {
            throw new IllegalArgumentException("Catering option#" + cateringOption.getId() + " is disabled.\n" +
                    "It can no longer be assigned");
        }
        Child child = findByIdWithAllDetails(childId);
        AssignedOption assignedOption = new AssignedOption(assignedOptionDTO.getEffectiveDate(), child, cateringOption);
        child.getAssignedOptions().add(assignedOption);
        assignedOptionRepository.save(assignedOption);
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

}
