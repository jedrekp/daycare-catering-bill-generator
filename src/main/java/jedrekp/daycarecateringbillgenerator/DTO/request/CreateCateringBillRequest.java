package jedrekp.daycarecateringbillgenerator.DTO.request;

import jedrekp.daycarecateringbillgenerator.entity.DailyCateringOrder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Month;
import java.time.Year;
import java.util.Set;

@Data
public class CreateCateringBillRequest {

    @NotNull
    private Month month;

    @NotNull
    private Year year;

    @NotNull
    @Valid
    private Set<DailyCateringOrder> dailyCateringOrders;
}
