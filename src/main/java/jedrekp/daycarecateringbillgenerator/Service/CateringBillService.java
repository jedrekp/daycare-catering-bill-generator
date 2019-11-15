package jedrekp.daycarecateringbillgenerator.Service;

import jedrekp.daycarecateringbillgenerator.Entity.*;
import jedrekp.daycarecateringbillgenerator.Repository.DailyAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

@Service
public class CateringBillService {

    @Autowired
    DailyAttendanceRepository dailyAttendanceRepository;

    @Autowired
    ChildService childService;

    @Autowired
    CateringOptionService cateringOptionService;

    @Transactional(readOnly = true)
    public CateringBill generateCateringBill(Long childId, Month month, Integer year) {

        Child child = childService.findSingleChildByIdAndArchived(childId, false);

        CateringBill cateringBill = new CateringBill(month, year, child);

        List<DailyAttendance> dailyAttendances = dailyAttendanceRepository
                .findByPresentChildIdForSpecificMonth(childId, month.getValue(), year);

        for (DailyAttendance dailyAttendance : dailyAttendances) {
            CateringOption optionInEffect = cateringOptionService
                    .findOptionInEffectForChild(childId, dailyAttendance.getDate());
            cateringBill.getDailyCateringOrders()
                    .add(new DailyCateringOrder(dailyAttendance.getDate(), optionInEffect.getOptionName(),
                            optionInEffect.getDailyCost(), cateringBill));
        }

        BigDecimal totalMonthlyCost = cateringBill.getDailyCateringOrders()
                .stream()
                .map(DailyCateringOrder::getCateringOptionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cateringBill.setTotalDue(totalMonthlyCost);

        return cateringBill;

    }
}
