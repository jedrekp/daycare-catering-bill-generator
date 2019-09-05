package jedrekp.daycarecateringbillgenerator.Service;

import jedrekp.daycarecateringbillgenerator.Entity.CateringOption;
import jedrekp.daycarecateringbillgenerator.Repository.CateringOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collection;

@Service
public class CateringOptionService {

    @Autowired
    CateringOptionRepository cateringOptionRepository;

    @Transactional
    public CateringOption save(CateringOption cateringOption) {
        if (cateringOptionRepository.existsByOptionName(cateringOption.getOptionName())) {
            throw new EntityExistsException(
                    "Catering option with this name already exists. Please choose a different name");
        }
        return cateringOptionRepository.save(cateringOption);
    }

    @Transactional
    public CateringOption editCateringOption(CateringOption cateringOption, Long cateringOptionId) {
        if (cateringOptionRepository.existsByOptionNameAndIdNot(cateringOption.getOptionName(), cateringOptionId)) {
            throw new EntityExistsException(
                    "Catering option with this name already exists. Please choose a different name");
        }
        CateringOption cateringOptionToEdit = findById(cateringOptionId);
        cateringOptionToEdit.setOptionName(cateringOption.getOptionName());
        cateringOptionToEdit.setDailyCost(cateringOption.getDailyCost());
        cateringOptionToEdit.setDisabled(cateringOption.isDisabled());
        return cateringOptionToEdit;
    }

    public CateringOption findById(Long cateringOptionId) {
        return cateringOptionRepository.findById(cateringOptionId).orElseThrow(EntityNotFoundException::new);
    }

    public Collection<CateringOption> findAll() {
        return cateringOptionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Collection<CateringOption> findAllByDisabled(boolean disabled) {
        return cateringOptionRepository.findAllByDisabled(disabled);
    }

    @Transactional(readOnly = true)
    public CateringOption findOptionInEffectForChild(Long childId, LocalDate date) {
        return cateringOptionRepository.findOptionInEffectByChildIdAndDate(childId, date)
                .orElseThrow(EntityNotFoundException::new);
    }
}
