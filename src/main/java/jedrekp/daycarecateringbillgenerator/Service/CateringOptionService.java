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

    @Transactional(readOnly = true)
    public CateringOption findById(Long cateringOptionId) {
        return cateringOptionRepository.findById(cateringOptionId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Collection<CateringOption> findAll() {
        return cateringOptionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Collection<CateringOption> findAllByDisabled(boolean disabled) {
        return cateringOptionRepository.findAllByDisabledOrderByOptionNameAsc(disabled);
    }

    @Transactional(readOnly = true)
    public CateringOption findOptionInEffectForChild(Long childId, LocalDate date) {
        return cateringOptionRepository.findOptionInEffectByChildIdAndDate(childId, date)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public CateringOption saveNewCateringOption(CateringOption cateringOption) {
        if (cateringOptionRepository.existsByOptionName(cateringOption.getOptionName())) {
            throw new EntityExistsException(
                    "Another catering option with the same name already exists. Please choose a different name");
        }
        if (cateringOption.isDisabled()) {
            throw new IllegalArgumentException("New catering option cannot be disabled");
        }
        return cateringOptionRepository.save(cateringOption);
    }

    @Transactional
    public CateringOption editCateringOption(CateringOption cateringOption, Long cateringOptionId) {
        if (cateringOptionRepository.existsByOptionNameAndIdNot(cateringOption.getOptionName(), cateringOptionId)) {
            throw new EntityExistsException(
                    "Another catering option with the name already exists. Please choose a different name");
        }
        CateringOption cateringOptionToEdit = findById(cateringOptionId);
        cateringOptionToEdit.setOptionName(cateringOption.getOptionName());
        cateringOptionToEdit.setDailyCost(cateringOption.getDailyCost());
        cateringOptionToEdit.setDisabled(cateringOption.isDisabled());
        return cateringOptionToEdit;
    }
}
