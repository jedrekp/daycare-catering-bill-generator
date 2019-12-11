package jedrekp.daycarecateringbillgenerator.DTO.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jedrekp.daycarecateringbillgenerator.entity.DailyCateringOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Data
public class CateringBillResponse {

    private long childId;
    private Month month;
    private Year year;
    private boolean correction;
    private String childFullName;
    private BigDecimal totalDue;

    @JsonIgnoreProperties("cateringBill")
    private Set<DailyCateringOrder> dailyCateringOrders = new HashSet<>();

    public CateringBillResponse(long childId, Month month, Year year) {
        this.childId = childId;
        this.month = month;
        this.year = year;
    }
}
