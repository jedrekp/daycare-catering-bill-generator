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
import java.util.*;

@Service
public class ChildService {

    @Autowired
    ChildRepository childRepository;

    @Autowired
    AssignedOptionRepository assignedOptionRepository;

    @Autowired
    CateringOptionService cateringOptionService;

    @Transactional(readOnly = true)
    public Child findSingleChildById(Long childId) {
        return childRepository.findById(childId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Child findSingleChildByIdAndArchived(Long childId, boolean archived) {
        return childRepository.findByIdAndArchived(childId, archived).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Child findSingleChildByIdWithAllDetails(Long childId) {
        return childRepository.findByIdWithAllDetails(childId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Child findSingleChildByIdAndArchivedWithAllDetails(Long childId, boolean archived) {
        return childRepository.findByIdAndArchivedWithAllDetails(childId, archived)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Collection<Child> findAll() {
        return childRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Collection<Child> findChildrenByArchived(boolean archived) {
        return childRepository.findAllByArchivedOrderByLastNameAscFirstNameAsc(archived);
    }

    @Transactional(readOnly = true)
    public Collection<Child> findChildrenByDaycareGroup(Long daycareGroupId) {
        if (daycareGroupId == -1L) {
            daycareGroupId = null;
        }
        return childRepository.findAllByDaycareGroup_IdAndArchivedOrderByLastNameAscFirstNameAsc(
                daycareGroupId, false);
    }

    @Transactional(readOnly = true)
    public Collection<Child> findChildrenBySearchPhrase(String searchPhrase) {
        Set<Child> children = new LinkedHashSet<>();

        //splits searchPhrase to get all adjacent words combinations to cover cases where firstName or lastName consists of multiple words
        Set<String> searchSubPhrases = splitSearchPhrase(searchPhrase);

        //attempts to find children by both firstName and LastName, to put at the start of the list (only when multiple words have been entered)
        if (searchSubPhrases.size() > 1) {
            children.addAll(childRepository.findAllByFirstNameAndLastName(searchSubPhrases));
        }

        //attempts to find all children whose lastName matches one of the entered words
        children.addAll(childRepository.findAllByLastName(searchSubPhrases));

        //attempts to find all children whose firstName matches one of the entered words, to put at the end of the list
        children.addAll(childRepository.findAllByFirstName(searchSubPhrases));

        return children;
    }

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

    @Transactional
    public AssignedOption assignCateringOption(Long childId, AssignedOptionDTO assignedOptionDTO) {
        if (assignedOptionRepository.existsByEffectiveDateAndChild_Id(assignedOptionDTO.getEffectiveDate(), childId)) {
            throw new IllegalArgumentException(
                    "Child #" + childId + " already has another catering option assigned with this effective date.\n" +
                            "It must be removed first");
        }
        CateringOption cateringOption = cateringOptionService.findById(assignedOptionDTO.getCateringOptionId());
        if (cateringOption.isDisabled()) {
            throw new IllegalArgumentException("Catering option#" + cateringOption.getId() + " is disabled.\n" +
                    "It can no longer be assigned");
        }
        Child child = findSingleChildByIdAndArchivedWithAllDetails(childId, false);
        AssignedOption assignedOption = new AssignedOption(assignedOptionDTO.getEffectiveDate(), child, cateringOption);
        child.getAssignedOptions().add(assignedOption);
        return assignedOption;
    }

    @Transactional
    public void removeAssignedOption(Long childId, Long assignedOptionId) {
        Child child = findSingleChildByIdAndArchivedWithAllDetails(childId, false);
        AssignedOption assignedOption = assignedOptionRepository.findById(assignedOptionId)
                .orElseThrow(EntityNotFoundException::new);
        child.getAssignedOptions().remove(assignedOption);
    }

    private Set<String> splitSearchPhrase(String searchPhrase) {
        Set<String> searchSubPhrases = new HashSet<>();
        searchPhrase = searchPhrase.toLowerCase().trim().replaceAll(" +", " ");
        Collections.addAll(searchSubPhrases, searchPhrase.split(" "));
        getAllAdjacentCombinations(searchSubPhrases, searchPhrase);
        return searchSubPhrases;
    }

    private void getAllAdjacentCombinations(Set<String> searchSubPhrases, String searchPhrase) {
        while (!searchSubPhrases.contains(searchPhrase) && searchPhrase.contains(" ")) {
            searchSubPhrases.add(searchPhrase);
            getAllAdjacentCombinations(
                    searchSubPhrases, searchPhrase.substring(0, searchPhrase.lastIndexOf(" ")));
            getAllAdjacentCombinations(
                    searchSubPhrases, searchPhrase.substring(searchPhrase.indexOf(" ") + 1));
        }
    }

}
