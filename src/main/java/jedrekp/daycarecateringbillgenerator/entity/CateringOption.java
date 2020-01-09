package jedrekp.daycarecateringbillgenerator.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "catering_option")
@Getter
@Setter
public class CateringOption {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "option_name", unique = true)
    @NotNull
    private String optionName;

    @Column(name = "daily_cost")
    @NotNull
    private BigDecimal dailyCost;

    @NotNull
    private boolean disabled;

}
