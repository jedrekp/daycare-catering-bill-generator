package jedrekp.preschoolcateringbillcalculator.Service;

import jedrekp.preschoolcateringbillcalculator.Entity.Diet;
import jedrekp.preschoolcateringbillcalculator.Repository.DietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collection;

@Service
public class DietService {

    @Autowired
    DietRepository dietRepository;

    public Diet save(Diet diet) {
        return dietRepository.save(diet);
    }

    public Diet findById(Long dietId) {
        return dietRepository.findById(dietId).orElseThrow(EntityNotFoundException::new);
    }

    public Collection<Diet> findAll() {
        return dietRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Diet findDietCurrentlyAssignedToChild(Long childId, LocalDate date) {
        return dietRepository.findCurrentlyAssignedDietByChildIdAndDate(childId, date)
                .orElseThrow(EntityNotFoundException::new);
    }
}
