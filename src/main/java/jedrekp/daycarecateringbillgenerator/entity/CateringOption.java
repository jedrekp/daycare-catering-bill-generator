package jedrekp.daycarecateringbillgenerator.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "catering_option")
@Getter
@Setter
@NoArgsConstructor
public class CateringOption {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "option_name", unique = true)
    @NotNull
    @Length(min = 3, max = 20)
    private String optionName;

    @Column(name = "daily_cost")
    @NotNull
    @Min(0)
    @Max(30)
    private BigDecimal dailyCost;

    @NotNull
    private boolean disabled;

}
