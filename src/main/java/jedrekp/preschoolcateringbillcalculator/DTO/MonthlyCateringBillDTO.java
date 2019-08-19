package jedrekp.preschoolcateringbillcalculator.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MonthlyCateringBillDTO {

    private Month month;

    private Integer year;

    private Long childId;

    private String childName;

    private BigDecimal totalCost;

    private List<DailyOrderDTO> dailyOrders = new ArrayList<>();

    public MonthlyCateringBillDTO(Month month, Integer year, Long childId, String childName) {
        this.month = month;
        this.year = year;
        this.childId = childId;
        this.childName = childName;
    }

}
