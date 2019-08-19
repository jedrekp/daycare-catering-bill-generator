package jedrekp.preschoolcateringbillcalculator.Service;

import jedrekp.preschoolcateringbillcalculator.Entity.PreschoolGroup;
import jedrekp.preschoolcateringbillcalculator.Repository.PreschoolGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

@Service
public class PreschoolGroupService {

    @Autowired
    PreschoolGroupRepository preschoolGroupRepository;

    public PreschoolGroup save(PreschoolGroup preschoolGroup) {
        return preschoolGroupRepository.save(preschoolGroup);
    }

    public PreschoolGroup findById(Long preschoolGroupId) {
        return preschoolGroupRepository.findById(preschoolGroupId).orElseThrow(EntityNotFoundException::new);
    }

    public Collection<PreschoolGroup> findAll() {
        return preschoolGroupRepository.findAll();
    }

}
