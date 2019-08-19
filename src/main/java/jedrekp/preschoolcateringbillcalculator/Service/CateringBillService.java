package jedrekp.preschoolcateringbillcalculator.Service;

import jedrekp.preschoolcateringbillcalculator.DTO.DailyOrderDTO;
import jedrekp.preschoolcateringbillcalculator.DTO.MonthlyCateringBillDTO;
import jedrekp.preschoolcateringbillcalculator.Entity.Child;
import jedrekp.preschoolcateringbillcalculator.Entity.DailyAttendance;
import jedrekp.preschoolcateringbillcalculator.Entity.Diet;
import jedrekp.preschoolcateringbillcalculator.Repository.DailyAttendanceRepository;
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
    DietService dietService;

    @Transactional(readOnly = true)
    public MonthlyCateringBillDTO generateCateringBill(Long childId, Month month, Integer year) {

        Child child = childService.findById(childId);

        MonthlyCateringBillDTO cateringBill = new MonthlyCateringBillDTO(month, year, child.getId(), child.getFirstName());

        List<DailyAttendance> dailyAttendances = dailyAttendanceRepository.findByChildIdForSpecificMonth(childId, month.getValue(), year);

        for (DailyAttendance dailyAttendance : dailyAttendances) {
            Diet currentlyAssignedDiet = dietService.findDietCurrentlyAssignedToChild(childId, dailyAttendance.getDate());
            cateringBill.getDailyOrders()
                    .add(new DailyOrderDTO(dailyAttendance.getDate(), currentlyAssignedDiet.getDietName(), currentlyAssignedDiet.getDailyCost()));
        }

        BigDecimal totalMonthlyCost = cateringBill.getDailyOrders()
                .stream()
                .map(DailyOrderDTO::getDietPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cateringBill.setTotalCost(totalMonthlyCost);

        return cateringBill;

    }
}
