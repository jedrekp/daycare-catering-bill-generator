package jedrekp.daycarecateringbillgenerator.DTO.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jedrekp.daycarecateringbillgenerator.entity.DailyCateringOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
public class CateringBillResponse {

    private long billId;
    private long childId;
    private String month;
    private Year year;
    private boolean correction;
    private String childFullName;
    private BigDecimal totalDue;

    @JsonIgnoreProperties("cateringBill")
    private List<DailyCateringOrder> dailyCateringOrders = new ArrayList<>();

    public CateringBillResponse(long billId, long childId, String month, Year year) {
        this.billId = billId;
        this.childId = childId;
        this.month = month;
        this.year = year;
    }
}
