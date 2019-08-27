package jedrekp.preschoolcateringbillcalculator.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "diet")
@Getter
@Setter
@NoArgsConstructor
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "diet_name", unique = true)
    private String dietName;

    @Column(name = "daily_cost")
    private BigDecimal dailyCost;

    @Column(name = "disabled")
    private boolean disabled = false;

}
