package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.DTO.AssignedOptionDTO;
import jedrekp.daycarecateringbillgenerator.entity.AssignedOption;
import jedrekp.daycarecateringbillgenerator.entity.CateringOption;
import jedrekp.daycarecateringbillgenerator.entity.Child;
import jedrekp.daycarecateringbillgenerator.repository.AssignedOptionRepository;
import jedrekp.daycarecateringbillgenerator.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;

    private final AssignedOptionRepository assignedOptionRepository;

    private final CateringOptionService cateringOptionService;

    @Transactional(readOnly = true)
    public Child findSingleChildByIdAndArchived(long childId, boolean archived) {
        return childRepository.findByIdAndArchived(childId, archived).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Child findSingleChildByIdWithAllDetails(long childId) {
        return childRepository.findByIdWithAllDetails(childId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Child findSingleChildByIdAndArchivedWithAllDetails(long childId, boolean archived) {
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
    public Collection<Child> findChildrenByDaycareGroup(long daycareGroupId) {
        if (daycareGroupId == 0L) {
            return childRepository.findAllByDaycareGroup_IdAndArchivedOrderByLastNameAscFirstNameAsc(
                    null, false);
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
        if (childRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(child.getFirstName(), child.getLastName())) {
            throw new EntityExistsException("Another child with the same first name and last name already exists");
        }
        return childRepository.save(child);
    }

    @Transactional
    public Child editChild(long childId, Child childFromRequest) {
        if (childRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndIdNot(childFromRequest.getFirstName(),
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
    public AssignedOption assignCateringOption(long childId, AssignedOptionDTO assignedOptionDTO) {
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
    public void removeAssignedOption(long childId, long assignedOptionId) {
        Child child = findSingleChildByIdAndArchivedWithAllDetails(childId, false);
        AssignedOption assignedOption = assignedOptionRepository.findById(assignedOptionId)
                .orElseThrow(EntityNotFoundException::new);
        child.getAssignedOptions().remove(assignedOption);
    }

    private Child findSingleChildById(long childId) {
        return childRepository.findById(childId).orElseThrow(EntityNotFoundException::new);
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
