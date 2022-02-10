package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.entity.Child;
import jedrekp.daycarecateringbillgenerator.entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.repository.DaycareGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.text.MessageFormat;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class DaycareGroupService {

    private final DaycareGroupRepository daycareGroupRepository;

    private final ChildService childService;

    @Transactional(readOnly = true)
    public DaycareGroup findSingleGroupByIdWithAllDetails(long daycareGroupId) {
        return daycareGroupRepository.findByIdWithAllDetails(daycareGroupId).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("Daycare group #{0} does not exist.", daycareGroupId)));
    }

    @Transactional(readOnly = true)
    public Collection<DaycareGroup> findAll() {
        return daycareGroupRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Collection<DaycareGroup> findByGroupSupervisorId(long groupSupervisorId) {
        if (groupSupervisorId == 0L) {
            return daycareGroupRepository.findAllByGroupSupervisorId(null);
        }
        return daycareGroupRepository.findAllByGroupSupervisorId(groupSupervisorId);
    }

    @Transactional(readOnly = true)
    public boolean verifyIfDaycareGroupIsSupervisedByUser(long daycareGroupId, String groupSupervisorUsername) {
        return daycareGroupRepository.existsByIdAndGroupSupervisorUsername(daycareGroupId, groupSupervisorUsername);
    }

    @Transactional
    public DaycareGroup saveNewDaycareGroup(DaycareGroup daycareGroup) {
        checkGroupNameAvailability(daycareGroup.getGroupName(), null);
        return daycareGroupRepository.save(daycareGroup);
    }

    @Transactional
    public DaycareGroup editDaycareGroup(long daycareGroupId, DaycareGroup daycareGroup) {
        checkGroupNameAvailability(daycareGroup.getGroupName(), daycareGroupId);
        DaycareGroup daycareGroupToEdit = findSingleGroupById(daycareGroupId);
        daycareGroupToEdit.setGroupName(daycareGroup.getGroupName());
        return daycareGroupToEdit;
    }

    @Transactional
    public void deleteDaycareGroup(long daycareGroupId) {
        DaycareGroup daycareGroup = findSingleGroupByIdWithAllDetails(daycareGroupId);
        Collection<Child> children = daycareGroup.getChildren();
        children.forEach(child -> child.setDaycareGroup(null));
        daycareGroupRepository.deleteById(daycareGroupId);
    }

    @Transactional
    public DaycareGroup addChildToDaycareGroup(long daycareGroupId, long childId) {
        Child child = childService.findSingleNotArchivedChildById(childId);
        if (child.getDaycareGroup() != null) {
            throw new IllegalArgumentException(MessageFormat.format("Child #{0} is already assigned to a daycare group.", childId));
        }
        DaycareGroup daycareGroup = findSingleGroupByIdWithAllDetails(daycareGroupId);
        child.setDaycareGroup(daycareGroup);
        daycareGroup.getChildren().add(child);
        return daycareGroup;
    }

    @Transactional
    public void removeChildFromDaycareGroup(long daycareGroupId, long childId) {
        Child child = childService.findSingleChildByIdAndDaycareGroupId(childId, daycareGroupId);
        DaycareGroup daycareGroup = findSingleGroupByIdWithAllDetails(daycareGroupId);
        daycareGroup.getChildren().remove(child);
        child.setDaycareGroup(null);
    }

    DaycareGroup findSingleGroupByIdAndGroupSupervisorId(long daycareGroupId, long groupSupervisorId) {
        return daycareGroupRepository.findByIdAndGroupSupervisorId(daycareGroupId, groupSupervisorId).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("Daycare group #{0} is not assigned to user #{1}.", daycareGroupId, groupSupervisorId)));
    }

    private DaycareGroup findSingleGroupById(long daycareGroupId) {
        return daycareGroupRepository.findById(daycareGroupId).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("Daycare group #{0} does not exist.", daycareGroupId)));
    }

    private void checkGroupNameAvailability(String groupName, Long daycareGroupId) {
        boolean nameTaken;
        if (daycareGroupId == null) {
            nameTaken = daycareGroupRepository.existsByGroupNameIgnoreCase(groupName);
        } else {
            nameTaken = daycareGroupRepository.existsByGroupNameIgnoreCaseAndIdNot(groupName, daycareGroupId);
        }
        if (nameTaken) {
            throw new EntityExistsException("Another daycare group with the same name already exists");
        }
    }
}
