package jedrekp.daycarecateringbillgenerator.Service;

import freemarker.template.TemplateException;
import jedrekp.daycarecateringbillgenerator.DTO.CateringBillDTO;
import jedrekp.daycarecateringbillgenerator.Entity.*;
import jedrekp.daycarecateringbillgenerator.Repository.CateringBillRepository;
import jedrekp.daycarecateringbillgenerator.Repository.DailyAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CateringBillService {

    @Autowired
    DailyAttendanceRepository dailyAttendanceRepository;

    @Autowired
    CateringBillRepository cateringBillRepository;

    @Autowired
    ChildService childService;

    @Autowired
    CateringOptionService cateringOptionService;

    @Autowired
    EmailService emailService;

    @Transactional(readOnly = true)
    public CateringBill findByIdWithAllDetails(Long cateringBillId) {
        return cateringBillRepository.findByIdWithAllDetails(cateringBillId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public CateringBillDTO generateCateringBillPreview(Long childId, Month month, Year year) {

        if (cateringBillRepository.existsByMonthAndYearAndChild_Id(month, year, childId)) {
            throw new EntityExistsException(MessageFormat
                    .format("Catering bill for child #{0} for this month already exists.", childId));
        }

        CateringBillDTO cateringBillDTO = new CateringBillDTO(month, year);

        List<DailyAttendance> dailyAttendances = dailyAttendanceRepository
                .findByPresentChildIdForSpecificMonth(childId, month.getValue(), year.getValue());

        for (DailyAttendance dailyAttendance : dailyAttendances) {
            CateringOption optionInEffect = cateringOptionService.findOptionInEffectForChild(childId, dailyAttendance.getDate());
            cateringBillDTO.getDailyCateringOrders()
                    .add(new DailyCateringOrder(dailyAttendance.getDate(), optionInEffect.getOptionName(),
                            optionInEffect.getDailyCost()));
        }

        return cateringBillDTO;
    }

    @Transactional
    public CateringBill saveNewCateringBill(Long childId, CateringBillDTO cateringBillDTO) {

        if (cateringBillDTO.getDailyCateringOrders().isEmpty()) {
            throw new IllegalArgumentException("Cannot save catering bill with no daily orders");
        }
        if (cateringBillRepository.existsByMonthAndYearAndChild_Id(cateringBillDTO.getMonth(), cateringBillDTO.getYear(), childId)) {
            throw new EntityExistsException(MessageFormat
                    .format("Catering bill for child #{0} for this month already exists.", childId));
        }

        Child child = childService.findSingleChildByIdAndArchived(childId, false);
        CateringBill cateringBill = new CateringBill(cateringBillDTO.getMonth(), cateringBillDTO.getYear());

        cateringBillDTO.getDailyCateringOrders().forEach(dailyCateringOrder -> dailyCateringOrder.setCateringBill(cateringBill));
        cateringBill.setDailyCateringOrders(cateringBillDTO.getDailyCateringOrders());

        cateringBill.setChild(child);
        child.getCateringBills().add(cateringBill);

        return cateringBill;
    }


    @Transactional(readOnly = true)
    public void sendBillToParentViaEmail(Long cateringBillId) {
        CateringBill cateringBill = findByIdWithAllDetails(cateringBillId);

        BigDecimal totalDue = cateringBill.getDailyCateringOrders()
                .stream()
                .map(DailyCateringOrder::getCateringOptionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        try {
            emailService.sendEmailWithCateringBill(cateringBill, totalDue);
        } catch (IOException | TemplateException | MessagingException ex) {
            ex.printStackTrace();
        }
    }
}
