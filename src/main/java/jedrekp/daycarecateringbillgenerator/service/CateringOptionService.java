package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.entity.CateringOption;
import jedrekp.daycarecateringbillgenerator.repository.CateringOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Collection;

@Service
@RequiredArgsConstructor

public class CateringOptionService {

    private final CateringOptionRepository cateringOptionRepository;

    @Transactional(readOnly = true)
    public CateringOption findById(long cateringOptionId) {
        return cateringOptionRepository.findById(cateringOptionId).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("Catering option #{0} does not exist.", cateringOptionId)
        ));
    }

    @Transactional(readOnly = true)
    public Collection<CateringOption> findAll() {
        return cateringOptionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Collection<CateringOption> findAllByDisabled(boolean disabled) {
        return cateringOptionRepository.findAllByDisabledOrderByOptionNameAsc(disabled);
    }

    @Transactional
    public CateringOption saveNewCateringOption(CateringOption cateringOption) {
        checkOptionNameAvailability(cateringOption.getOptionName(), null);
        if (cateringOption.isDisabled()) {
            throw new IllegalArgumentException("New catering option cannot be disabled");
        }
        return cateringOptionRepository.save(cateringOption);
    }

    @Transactional
    public CateringOption editCateringOption(CateringOption cateringOption, long cateringOptionId) {
        checkOptionNameAvailability(cateringOption.getOptionName(), cateringOptionId);
        CateringOption cateringOptionToEdit = findById(cateringOptionId);
        cateringOptionToEdit.setOptionName(cateringOption.getOptionName());
        cateringOptionToEdit.setDailyCost(cateringOption.getDailyCost());
        cateringOptionToEdit.setDisabled(cateringOption.isDisabled());
        return cateringOptionToEdit;
    }

    CateringOption findOptionInEffectForChild(long childId, LocalDate date) {
        return cateringOptionRepository.findOptionInEffectByChildIdAndDate(childId, date)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("There is no catering option " +
                        "in effect for {0} for child #{1}", date, childId)));
    }

    private void checkOptionNameAvailability(String cateringOptionName, Long cateringOptionId) {
        boolean nameTaken;
        if (cateringOptionId == null) {
            nameTaken = cateringOptionRepository.existsByOptionNameIgnoreCase(cateringOptionName);
        } else {
            nameTaken = cateringOptionRepository.existsByOptionNameIgnoreCaseAndIdNot(cateringOptionName, cateringOptionId);
        }
        if (nameTaken) {
            throw new EntityExistsException(
                    "Another catering option with this name already exists. Please choose a different name");
        }
    }
}
