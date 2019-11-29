package jedrekp.daycarecateringbillgenerator.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jedrekp.daycarecateringbillgenerator.Entity.DailyCateringOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CateringBillDTO {

    @NotNull
    private Month month;
    @NotNull
    private Year year;

    @JsonIgnoreProperties("cateringBill")
    private List<DailyCateringOrder> dailyCateringOrders = new ArrayList<>();

    public CateringBillDTO(Month month,Year year) {
        this.month = month;
        this.year = year;
    }
}
