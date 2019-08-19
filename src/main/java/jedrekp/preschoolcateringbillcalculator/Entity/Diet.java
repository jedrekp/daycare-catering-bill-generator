package jedrekp.preschoolcateringbillcalculator.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.preschoolcateringbillcalculator.Utility.JsonViewFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "diet")
@Getter
@Setter
@NoArgsConstructor
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViewFilter.BasicInfo.class)
    private Long id;

    @Column(name = "diet_name", unique = true)
    @JsonView(JsonViewFilter.BasicInfo.class)
    private String dietName;

    @Column(name = "daily_cost")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private BigDecimal dailyCost;

    @OneToMany(mappedBy = "diet", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JsonIgnore
    private Set<ChildDiet> children;


}
