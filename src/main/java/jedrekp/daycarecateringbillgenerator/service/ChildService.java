package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.DTO.request.AssignOptionToChildRequest;
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
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;

    private final AssignedOptionRepository assignedOptionRepository;

    private final CateringOptionService cateringOptionService;

    @Transactional(readOnly = true)
    public boolean existsById(long childId) {
        return childRepository.existsById(childId);
    }

    @Transactional(readOnly = true)
    public Child findSingleChildByIdWithAllDetails(long childId) {
        return childRepository.findByIdWithAllDetails(childId)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Child #{0} does not exist.", childId)));
    }

    @Transactional(readOnly = true)
    public Collection<Child> findAll() {
        return childRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Collection<Child> findChildrenByArchived(boolean archived) {
        return childRepository.findAllByArchived(archived);
    }

    @Transactional(readOnly = true)
    public Collection<Child> findChildrenByDaycareGroup(long daycareGroupId) {
        if (daycareGroupId == 0L) {
            return childRepository.findAllByDaycareGroupIdAndArchived(
                    null, false);
        }
        return childRepository.findAllByDaycareGroupIdAndArchived(
                daycareGroupId, false);
    }

    @Transactional(readOnly = true)
    public boolean verifyIfChildIsAssignedToDaycareGroupSupervisedByUser(long childId, String groupSupervisorUsername) {
        return childRepository.existsByIdAndDaycareGroupGroupSupervisorUsername(childId, groupSupervisorUsername);
    }

    @Transactional
    public Child saveNewChild(Child child) {
        checkNameAvailability(child.getFirstName(), child.getLastName(), null);
        return childRepository.save(child);
    }

    @Transactional
    public Child editChild(long childId, Child childFromRequest) {
        checkNameAvailability(childFromRequest.getFirstName(), childFromRequest.getLastName(), childId);

        Child childToEdit = findSingleChildById(childId);
        childToEdit.setFirstName(childFromRequest.getFirstName());
        childToEdit.setLastName(childFromRequest.getLastName());
        childToEdit.setParentEmail(childFromRequest.getParentEmail());

        if (childFromRequest.isArchived() && childToEdit.getDaycareGroup() != null) {
            removeChildFromDaycareGroup(childToEdit);
        }

        childToEdit.setArchived(childFromRequest.isArchived());
        return childToEdit;
    }

    @Transactional
    public AssignedOption assignCateringOption(long childId, AssignOptionToChildRequest assignOptionRequest) {
        checkIfOptionCanBeAssignedForDate(assignOptionRequest.getEffectiveDate(), childId);

        CateringOption cateringOption = cateringOptionService.findById(assignOptionRequest.getCateringOptionId());
        if (cateringOption.isDisabled()) {
            throw new IllegalArgumentException(MessageFormat.format("Catering option #{0} is disabled.\n" +
                    "It can no longer be assigned", cateringOption.getId()));
        }

        Child child = findSingleChildByIdWithAllDetails(childId);
        AssignedOption assignedOption = new AssignedOption(assignOptionRequest.getEffectiveDate(), child, cateringOption);
        child.getAssignedOptions().add(assignedOption);
        return assignedOption;
    }

    @Transactional
    public void removeAssignedOption(long childId, LocalDate effectiveDate) {
        Child child = findSingleNotArchivedChildByIdWithAssignedOptions(childId);
        AssignedOption assignedOption = assignedOptionRepository.findByEffectiveDateAndChildId(effectiveDate, childId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("There is no catering option assigned " +
                        "to child #{0} with an effective date - {1}", childId, effectiveDate)));
        child.getAssignedOptions().remove(assignedOption);
        assignedOption.setChild(null);
    }

    Child findSingleNotArchivedChildById(long childId) {
        return childRepository.findByIdAndArchived(childId, false).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("Child #{0} does not exists or is archived.", childId)));
    }

    Child findSingleChildByIdAndDaycareGroupId(long childId, long daycareGroupId) {
        return childRepository.findByIdAndDaycareGroupId(childId, daycareGroupId).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format(
                        "Child #{0} does not exist or is not assigned to daycare group #{1}.", childId, daycareGroupId)));
    }

    List<Child> findPresentChildrenByDateAndDaycareGroupId(LocalDate date, long daycareGroupId) {
        return childRepository.findPresentChildrenByDateAndDaycareGroupId(date, daycareGroupId);
    }

    List<Child> findAbsentChildrenByDateAndDaycareGroupId(LocalDate date, long daycareGroupId) {
        return childRepository.findAbsentChildrenByDateAndDaycareGroupId(date, daycareGroupId);
    }

    String getFullNameOfChild(Child child) {
        return MessageFormat.format("{0} {1}", child.getFirstName(), child.getLastName());
    }

    private Child findSingleChildById(long childId) {
        return childRepository.findById(childId).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("Child #{0} does not exist.", childId)));
    }

    private Child findSingleNotArchivedChildByIdWithAssignedOptions(long childId) {
        return childRepository.findByIdAndArchivedWithAssignedOptions(childId, false).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("Child #{0} does not exist.", childId)));
    }

    private void checkNameAvailability(String firstName, String lastName, Long childId) {
        boolean nameTaken;
        if (childId == null) {
            nameTaken = childRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
        } else {
            nameTaken = childRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndIdNot(firstName, lastName, childId);
        }
        if (nameTaken) {
            throw new EntityExistsException("Another child with the same first name and last name already exists.");
        }
    }

    private void checkIfOptionCanBeAssignedForDate(LocalDate effectiveDate, long childId) {
        if (assignedOptionRepository.existsByEffectiveDateAndChildId(effectiveDate, childId)) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Child #{0} already has another catering option assigned " +
                            "with this effective date.\n It must be removed first.", childId));
        }
    }

    private void removeChildFromDaycareGroup(Child child) {
        child.getDaycareGroup().getChildren().remove(child);
        child.setDaycareGroup(null);
    }
}
