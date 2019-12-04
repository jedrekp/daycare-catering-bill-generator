package jedrekp.daycarecateringbillgenerator.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jedrekp.daycarecateringbillgenerator.entity.DailyCateringOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CateringBillDTO {

    @NotNull
    private Month month;
    @NotNull
    private Year year;

    @JsonIgnoreProperties("cateringBill")
    private List<DailyCateringOrder> dailyCateringOrders = new ArrayList<>();

    public CateringBillDTO(Month month, Year year) {
        this.month = month;
        this.year = year;
    }
}
