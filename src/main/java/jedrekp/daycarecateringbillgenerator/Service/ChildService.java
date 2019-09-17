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
    public Child saveNewChild(Child child) {
        if (child.isArchived()) {
            throw new IllegalArgumentException("New child cannot be saved directly to archive");
        }
        if (childRepository.existsByFirstNameAndLastName(child.getFirstName(), child.getLastName())) {
            throw new EntityExistsException("Another child with the same first name and last name already exists");
        }
        return childRepository.save(child);
    }

    @Transactional
    public Child editChild(Long childId, Child childFromRequest) {
        if (childRepository.existsByFirstNameAndLastNameAndIdNot(childFromRequest.getFirstName(),
                childFromRequest.getLastName(), childId)) {
            throw new EntityExistsException("Another child with the same first name and last name already exists");
        }
        Child childToEdit = findSingleChildById(childId);
        childToEdit.setFirstName(childFromRequest.getFirstName());
        childToEdit.setLastName(childFromRequest.getLastName());
        childToEdit.setParentEmail(childFromRequest.getParentEmail());

        // Remove child from daycare group before moving it to archive
        if (childFromRequest.isArchived() && childToEdit.getDaycareGroup() != null) {
            childToEdit.getDaycareGroup().getChildren().remove(childToEdit);
            childToEdit.setDaycareGroup(null);
        }
        childToEdit.setArchived(childFromRequest.isArchived());
        return childToEdit;
    }

    public Child findSingleChildById(Long childId) {
        return childRepository.findById(childId).orElseThrow(EntityNotFoundException::new);
    }

    public Child findSingleChildByIdAndArchived(Long childId, boolean archived) {
        return childRepository.findByIdAndArchived(childId, archived).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Child findByIdWithAssignedOptions(Long childId) {
        return childRepository.findByIdWithAssignedOptions(childId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Child findSingleChildByIdWithAllDetails(Long childId) {
        return childRepository.findByIdWithAllDetails(childId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Collection<Child> findChildrenByArchived(boolean archived) {
        return childRepository.findAllByArchived(archived);
    }

    @Transactional(readOnly = true)
    public Collection<Child> findChildrenByDaycareGroup(Long daycareGroupId) {
        if (daycareGroupId == -1L) {
            daycareGroupId = null;
        }
        return childRepository.findAllByDaycareGroup_IdAndArchived(daycareGroupId,false);
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
        Child child = findByIdWithAssignedOptions(childId);
        AssignedOption assignedOption = new AssignedOption(assignedOptionDTO.getEffectiveDate(), child, cateringOption);
        child.getAssignedOptions().add(assignedOption);
        assignedOptionRepository.save(assignedOption);
        return child;
    }

    @Transactional
    public Child removeAssignedOption(Long childId, Long assignedOptionId) {
        Child child = findByIdWithAssignedOptions(childId);
        AssignedOption assignedOption = assignedOptionRepository.findById(assignedOptionId)
                .orElseThrow(EntityNotFoundException::new);
        child.getAssignedOptions().remove(assignedOption);
        assignedOptionRepository.delete(assignedOption);
        return child;
    }

}
