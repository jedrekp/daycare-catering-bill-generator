package jedrekp.daycarecateringbillgenerator.Service;

import jedrekp.daycarecateringbillgenerator.Entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.Repository.DaycareGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

@Service
public class DaycareGroupService {

    @Autowired
    DaycareGroupRepository daycareGroupRepository;

    public DaycareGroup save(DaycareGroup daycareGroup) {
        return daycareGroupRepository.save(daycareGroup);
    }

    public DaycareGroup findById(Long daycareGroupId) {
        return daycareGroupRepository.findById(daycareGroupId).orElseThrow(EntityNotFoundException::new);
    }

    public Collection<DaycareGroup> findAll() {
        return daycareGroupRepository.findAll();
    }

}
