package jedrekp.daycarecateringbillgenerator.Service;

import jedrekp.daycarecateringbillgenerator.Entity.CateringOption;
import jedrekp.daycarecateringbillgenerator.DTO.DailyOrderDTO;
import jedrekp.daycarecateringbillgenerator.DTO.MonthlyCateringBillDTO;
import jedrekp.daycarecateringbillgenerator.Entity.Child;
import jedrekp.daycarecateringbillgenerator.Entity.DailyAttendance;
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
    public MonthlyCateringBillDTO generateCateringBill(Long childId, Month month, Integer year) {

        Child child = childService.findById(childId);

        MonthlyCateringBillDTO cateringBill = new MonthlyCateringBillDTO(month, year, child.getId(), child.getFirstName());

        List<DailyAttendance> dailyAttendances = dailyAttendanceRepository.findByChildIdForSpecificMonth(childId, month.getValue(), year);

        for (DailyAttendance dailyAttendance : dailyAttendances) {
            CateringOption currentlyAssignedCateringOption = cateringOptionService
                    .findOptionInEffectForChild(childId, dailyAttendance.getDate());
            cateringBill.getDailyOrders()
                    .add(new DailyOrderDTO(dailyAttendance.getDate(),
                            currentlyAssignedCateringOption.getOptionName(), currentlyAssignedCateringOption.getDailyCost()));
        }

        BigDecimal totalMonthlyCost = cateringBill.getDailyOrders()
                .stream()
                .map(DailyOrderDTO::getCateringOptionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cateringBill.setTotalCost(totalMonthlyCost);

        return cateringBill;

    }
}
