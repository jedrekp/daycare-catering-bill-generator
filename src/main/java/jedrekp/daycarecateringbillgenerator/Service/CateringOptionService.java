package jedrekp.daycarecateringbillgenerator.Service;

import jedrekp.daycarecateringbillgenerator.Entity.CateringOption;
import jedrekp.daycarecateringbillgenerator.Repository.CateringOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collection;

@Service
public class CateringOptionService {

    @Autowired
    CateringOptionRepository cateringOptionRepository;

    public CateringOption save(CateringOption cateringOption) {
        return cateringOptionRepository.save(cateringOption);
    }

    public CateringOption findById(Long cateringOptionId) {
        return cateringOptionRepository.findById(cateringOptionId).orElseThrow(EntityNotFoundException::new);
    }

    public Collection<CateringOption> findAll() {
        return cateringOptionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CateringOption findOptionInEffectForChild(Long childId, LocalDate date) {
        return cateringOptionRepository.findOptionInEffectByChildIdAndDate(childId, date)
                .orElseThrow(EntityNotFoundException::new);
    }
}
