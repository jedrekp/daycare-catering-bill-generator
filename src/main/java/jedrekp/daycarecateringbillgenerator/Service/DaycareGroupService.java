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

    @Transactional
    public DaycareGroup save(DaycareGroup daycareGroup) {
        if (daycareGroupRepository.existsByGroupName(daycareGroup.getGroupName())) {
            throw new EntityExistsException("Daycare group with chosen group name already exists");
        }
        return daycareGroupRepository.save(daycareGroup);
    }

    @Transactional
    public DaycareGroup editDaycareGroup(Long daycareGroupId, DaycareGroup daycareGroup) {
        if (daycareGroupRepository.existsByGroupNameAndIdNot(daycareGroup.getGroupName(), daycareGroupId)) {
            throw new EntityExistsException("Daycare group with chosen group name already exists");
        }
        DaycareGroup daycareGroupToEdit = findById(daycareGroupId);
        daycareGroupToEdit.setGroupName(daycareGroup.getGroupName());
        return daycareGroupToEdit;
    }

    public DaycareGroup findById(Long id) {
        return daycareGroupRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public DaycareGroup findByIdWithChildren(Long daycareGroupId) {
        return daycareGroupRepository.findByIdWithChildren(daycareGroupId).orElseThrow(EntityNotFoundException::new);
    }

    public Collection<DaycareGroup> findAll() {
        return daycareGroupRepository.findAll();
    }

    @Transactional
    public DaycareGroup addSingleChildToDaycareGroup(Long daycareGroupId, Long childId) {
        Child child = childService.findById(childId);
        DaycareGroup daycareGroup = findByIdWithChildren(daycareGroupId);
        child.setDaycareGroup(daycareGroup);
        daycareGroup.getChildren().add(child);
        return daycareGroup;
    }
}
