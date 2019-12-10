package jedrekp.daycarecateringbillgenerator.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jedrekp.daycarecateringbillgenerator.entity.DailyCateringOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CateringBillDTO {

    @NotNull
    private long childId;
    @NotNull
    private Month month;
    @NotNull
    private Year year;
    @NotNull
    private boolean correction;

    private String childFullName;
    private BigDecimal totalDue;

    @JsonIgnoreProperties("cateringBill")
    private List<DailyCateringOrder> dailyCateringOrders = new ArrayList<>();

    public CateringBillDTO(long childId, Month month, Year year) {
        this.childId = childId;
        this.month = month;
        this.year = year;
    }
}
