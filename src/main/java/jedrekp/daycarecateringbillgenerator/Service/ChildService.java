package jedrekp.daycarecateringbillgenerator.Service;

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
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;

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
    public AssignedOption assignCateringOption(Long childId, Long cateringOptionId, LocalDate effectiveDate) {
        if (assignedOptionRepository.existsByEffectiveDateAndChild_Id(effectiveDate, childId)) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Child #{0} already has another catering option assigned " +
                            "with this effective date.\n It must be removed first", childId));
        }
        CateringOption cateringOption = cateringOptionService.findById(cateringOptionId);
        if (cateringOption.isDisabled()) {
            throw new IllegalArgumentException(MessageFormat.format("Catering option #{0} is disabled.\n" +
                    "It can no longer be assigned", cateringOptionId));
        }
        Child child = findSingleChildByIdAndArchived(childId, false);
        AssignedOption assignedOption = new AssignedOption(effectiveDate, child, cateringOption);
        child.getAssignedOptions().add(assignedOption);
        return assignedOption;
    }

    @Transactional
    public Child removeAssignedOption(Long childId, Long assignedOptionId) {
        Child child = findSingleChildByIdAndArchivedWithAllDetails(childId, false);
        AssignedOption assignedOption = assignedOptionRepository.findById(assignedOptionId)
                .orElseThrow(EntityNotFoundException::new);
        child.getAssignedOptions().remove(assignedOption);
        return child;
    }

    private Set<String> splitSearchPhrase(String searchPhrase) {
        Set<String> searchSubPhrases = new HashSet<>();
        searchPhrase = searchPhrase.toLowerCase().trim().replaceAll(" +", " ");
        Collections.addAll(searchSubPhrases, searchPhrase.split(" "));
        getAllAdjacentCombinationsViaRecursion(searchSubPhrases, searchPhrase);
        return searchSubPhrases;
    }

    private void getAllAdjacentCombinationsViaRecursion(Set<String> searchSubPhrases, String searchPhrase) {
        while (!searchSubPhrases.contains(searchPhrase) && searchPhrase.contains(" ")) {
            searchSubPhrases.add(searchPhrase);
            getAllAdjacentCombinationsViaRecursion(
                    searchSubPhrases, searchPhrase.substring(0, searchPhrase.lastIndexOf(" ")));
            getAllAdjacentCombinationsViaRecursion(
                    searchSubPhrases, searchPhrase.substring(searchPhrase.indexOf(" ") + 1));
        }
    }

}
