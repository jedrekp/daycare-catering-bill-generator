package jedrekp.daycarecateringbillgenerator.Service;

import jedrekp.daycarecateringbillgenerator.Entity.Child;
import jedrekp.daycarecateringbillgenerator.Entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.Repository.DaycareGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;

@Service
public class DaycareGroupService {

    @Autowired
    DaycareGroupRepository daycareGroupRepository;

    @Autowired
    ChildService childService;

    @Transactional(readOnly = true)
    public DaycareGroup findSingleGroupById(Long id) {
        return daycareGroupRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public DaycareGroup findSingleGroupByIdWithChildren(Long daycareGroupId) {
        return daycareGroupRepository.findByIdWithChildren(daycareGroupId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Collection<DaycareGroup> findAll() {
        return daycareGroupRepository.findAllByOrderByGroupNameAsc();
    }

    @Transactional
    public DaycareGroup saveNewDaycareGroup(DaycareGroup daycareGroup) {
        if (daycareGroupRepository.existsByGroupName(daycareGroup.getGroupName())) {
            throw new EntityExistsException("Another daycare group with the same group name already exists");
        }
        return daycareGroupRepository.save(daycareGroup);
    }

    @Transactional
    public DaycareGroup editDaycareGroup(Long daycareGroupId, DaycareGroup daycareGroup) {
        if (daycareGroupRepository.existsByGroupNameAndIdNot(daycareGroup.getGroupName(), daycareGroupId)) {
            throw new EntityExistsException("Another daycare group with the same group name already exists");
        }
        DaycareGroup daycareGroupToEdit = findSingleGroupById(daycareGroupId);
        daycareGroupToEdit.setGroupName(daycareGroup.getGroupName());
        return daycareGroupToEdit;
    }

    @Transactional
    public void deleteDaycareGroup(Long daycareGroupId) {
        DaycareGroup daycareGroup = findSingleGroupByIdWithChildren(daycareGroupId);
        Collection<Child> children = daycareGroup.getChildren();
        children.forEach(child -> child.setDaycareGroup(null));
        daycareGroupRepository.deleteById(daycareGroupId);
    }

    @Transactional
    public DaycareGroup addChildToDaycareGroup(Long daycareGroupId, Long childId) {
        Child child = childService.findSingleChildByIdAndArchived(childId, false);
        if (child.getDaycareGroup() != null) {
            throw new IllegalArgumentException("Child #" + child.getId() + " is already assigned to a daycare group");
        }
        DaycareGroup daycareGroup = findSingleGroupByIdWithChildren(daycareGroupId);
        child.setDaycareGroup(daycareGroup);
        daycareGroup.getChildren().add(child);
        return daycareGroup;
    }

    @Transactional
    public void removeChildFromDaycareGroup(Long daycareGroupId, Long childId) {
        Child child = childService.findSingleChildByIdWithAllDetails(childId);
        DaycareGroup daycareGroup = findSingleGroupByIdWithChildren(daycareGroupId);
        if (daycareGroup.getChildren().contains(child)) {
            daycareGroup.getChildren().remove(child);
            child.setDaycareGroup(null);
        } else {
            throw new IllegalArgumentException("Child #" + child.getId() + " is not in this daycare group");
        }
    }
}
