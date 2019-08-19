package jedrekp.preschoolcateringbillcalculator.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;

@Getter
@Setter
@NoArgsConstructor
public class MonthInput {

    private Month month;

    private Integer year;
}
