package jedrekp.daycarecateringbillgenerator.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "catering_option")
@Getter
@Setter
@NoArgsConstructor
public class CateringOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "option_name", unique = true)
    private String optionName;

    @Column(name = "daily_cost")
    private BigDecimal dailyCost;

    private boolean disabled;

}
