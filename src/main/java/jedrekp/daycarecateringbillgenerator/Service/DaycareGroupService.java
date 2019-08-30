package jedrekp.daycarecateringbillgenerator.Service;

import jedrekp.daycarecateringbillgenerator.Entity.Child;
import jedrekp.daycarecateringbillgenerator.Entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.Repository.DaycareGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

@Service
public class DaycareGroupService {

    @Autowired
    DaycareGroupRepository daycareGroupRepository;

    @Autowired
    ChildService childService;

    public DaycareGroup save(DaycareGroup daycareGroup) {
        return daycareGroupRepository.save(daycareGroup);
    }

    public Collection<DaycareGroup> findAll() {
        return daycareGroupRepository.findAll();
    }

    @Transactional
    public DaycareGroup addSingleChildToDaycareGroup(Long daycareGroupId, Long childId) {
        Child child = childService.findById(childId);
        DaycareGroup daycareGroup = daycareGroupRepository.findByIdWithChildren(daycareGroupId)
                .orElseThrow(EntityNotFoundException::new);
        child.setDaycareGroup(daycareGroup);
        daycareGroup.getChildren().add(child);
        return daycareGroup;
    }
}
