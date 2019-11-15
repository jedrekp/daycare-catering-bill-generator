package jedrekp.daycarecateringbillgenerator.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "monthly_catering_bill",
        uniqueConstraints = @UniqueConstraint(columnNames = {"month", "year", "child_id"}))
@Getter
@Setter
@NoArgsConstructor
public class CateringBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "month")
    private Month month;

    @Column(name = "year")
    private int year;

    @Column(name = "total_due")
    private BigDecimal totalDue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    @JsonIgnoreProperties({"daycareGroup", "assignedOptions"})
    private Child child;

    @OneToMany(mappedBy = "cateringBill", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JsonIgnoreProperties("cateringBill")
    @OrderBy(value = "orderDate ASC")
    private Set<DailyCateringOrder> dailyCateringOrders = new HashSet<>();

    public CateringBill(Month month, int year, Child child) {
        this.month = month;
        this.year = year;
        this.child = child;
    }
}
