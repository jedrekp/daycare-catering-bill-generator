package jedrekp.daycarecateringbillgenerator.service;

import freemarker.template.TemplateException;
import jedrekp.daycarecateringbillgenerator.DTO.CateringBillDTO;
import jedrekp.daycarecateringbillgenerator.entity.*;
import jedrekp.daycarecateringbillgenerator.repository.CateringBillRepository;
import jedrekp.daycarecateringbillgenerator.repository.DailyAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Month;
import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CateringBillService {

    private final DailyAttendanceRepository dailyAttendanceRepository;

    private final CateringBillRepository cateringBillRepository;

    private final ChildService childService;

    private final CateringOptionService cateringOptionService;

    private final EmailService emailService;

    @Transactional(readOnly = true)
    public CateringBill findByIdWithAllDetails(long cateringBillId) {
        return cateringBillRepository.findByIdWithAllDetails(cateringBillId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public CateringBillDTO generateCateringBillPreview(long childId, Month month, Year year) {

        checkAvailabilityToGenerateCateringBill(month, year, childId);

        CateringBillDTO cateringBillDTO = new CateringBillDTO(month, year);

        List<DailyAttendance> dailyAttendances = dailyAttendanceRepository
                .findByPresentChildIdForSpecificMonth(childId, month.getValue(), year.getValue());

        insertDailyOrdersIntoCateringBill(dailyAttendances, cateringBillDTO, childId);

        return cateringBillDTO;
    }

    @Transactional
    public CateringBill saveNewCateringBill(long childId, CateringBillDTO cateringBillDTO) {

        checkIfCateringBillContainsAnyOrders(cateringBillDTO);

        checkAvailabilityToGenerateCateringBill(cateringBillDTO.getMonth(), cateringBillDTO.getYear(), childId);

        Child child = childService.findSingleChildByIdAndArchived(childId, false);
        CateringBill cateringBill = new CateringBill(cateringBillDTO.getMonth(), cateringBillDTO.getYear());

        cateringBillDTO.getDailyCateringOrders().forEach(dailyCateringOrder -> dailyCateringOrder.setCateringBill(cateringBill));
        cateringBill.setDailyCateringOrders(cateringBillDTO.getDailyCateringOrders());

        cateringBill.setChild(child);
        child.getCateringBills().add(cateringBill);

        return cateringBill;
    }


    @Transactional(readOnly = true)
    public void sendBillToParentViaEmail(long cateringBillId) {

        CateringBill cateringBill = findByIdWithAllDetails(cateringBillId);

        BigDecimal totalDue = calculateTotalDueForCateringBill(cateringBill);

        try {
            emailService.sendEmailWithCateringBill(cateringBill, totalDue);
        } catch (IOException | TemplateException | MessagingException ex) {
            ex.printStackTrace();
        }
    }

    private void checkAvailabilityToGenerateCateringBill(Month month, Year year, long childId) {
        if (cateringBillRepository.existsByMonthAndYearAndChild_Id(month, year, childId)) {
            throw new EntityExistsException(MessageFormat
                    .format("Catering bill for child #{0} for this month already exists.", childId));
        }
    }

    private void checkIfCateringBillContainsAnyOrders(CateringBillDTO cateringBillDTO) {
        if (cateringBillDTO.getDailyCateringOrders().isEmpty()) {
            throw new IllegalArgumentException("Cannot save catering bill with no daily orders");
        }
    }

    private void insertDailyOrdersIntoCateringBill(List<DailyAttendance> dailyAttendances, CateringBillDTO cateringBillDTO, long childId) {
        for (DailyAttendance dailyAttendance : dailyAttendances) {
            CateringOption optionInEffect = cateringOptionService.findOptionInEffectForChild(childId, dailyAttendance.getDate());
            cateringBillDTO.getDailyCateringOrders()
                    .add(new DailyCateringOrder(dailyAttendance.getDate(), optionInEffect.getOptionName(), optionInEffect.getDailyCost()));
        }
    }

    private BigDecimal calculateTotalDueForCateringBill(CateringBill cateringBill) {
        return cateringBill.getDailyCateringOrders()
                .stream()
                .map(DailyCateringOrder::getCateringOptionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
