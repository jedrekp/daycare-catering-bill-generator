package jedrekp.daycarecateringbillgenerator.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.Utility.JsonViewFilter;
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
    @JsonView(JsonViewFilter.BasicInfo.class)
    private Long id;

    @Column(name = "option_name", unique = true)
    @JsonView(JsonViewFilter.BasicInfo.class)
    private String optionName;

    @Column(name = "daily_cost")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private BigDecimal dailyCost;

    @Column(name = "disabled")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private boolean disabled;

}
