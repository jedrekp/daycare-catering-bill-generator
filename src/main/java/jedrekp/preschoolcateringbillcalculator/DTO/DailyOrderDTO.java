package jedrekp.preschoolcateringbillcalculator.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyOrderDTO {

    private LocalDate date;
    private String dietName;
    private BigDecimal dietPrice;
}
