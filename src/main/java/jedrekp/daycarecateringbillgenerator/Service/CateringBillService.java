package jedrekp.daycarecateringbillgenerator.Service;

import freemarker.template.TemplateException;
import jedrekp.daycarecateringbillgenerator.Entity.CateringBill;
import jedrekp.daycarecateringbillgenerator.Entity.CateringOption;
import jedrekp.daycarecateringbillgenerator.Entity.DailyAttendance;
import jedrekp.daycarecateringbillgenerator.Entity.DailyCateringOrder;
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
    public CateringBill generateCateringBillPreview(Long childId, Month month, Integer year) {

        if (cateringBillRepository.existsByMonthAndYearAndChild_Id(month, year, childId)) {
            throw new EntityExistsException(MessageFormat
                    .format("Catering bill for child #{0} for this month already exists.", childId));
        }

        CateringBill cateringBill = new CateringBill(month, year);

        List<DailyAttendance> dailyAttendances = dailyAttendanceRepository
                .findByPresentChildIdForSpecificMonth(childId, month.getValue(), year);

        for (DailyAttendance dailyAttendance : dailyAttendances) {
            CateringOption optionInEffect = cateringOptionService.findOptionInEffectForChild(childId, dailyAttendance.getDate());
            cateringBill.getDailyCateringOrders()
                    .add(new DailyCateringOrder(dailyAttendance.getDate(), optionInEffect.getOptionName(),
                            optionInEffect.getDailyCost()));
        }

        BigDecimal totalMonthlyCost = cateringBill.getDailyCateringOrders()
                .stream()
                .map(DailyCateringOrder::getCateringOptionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cateringBill.setTotalDue(totalMonthlyCost);

        return cateringBill;
    }

    @Transactional(readOnly = true)
    public void sendBillToParentViaEmail(Long cateringBillId) {
        CateringBill cateringBill = findByIdWithAllDetails(cateringBillId);
        try {
            emailService.sendEmailWithCateringBill(cateringBill);
        } catch (IOException | TemplateException | MessagingException ex) {
            ex.printStackTrace();
        }
    }
}
