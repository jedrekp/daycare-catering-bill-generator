package jedrekp.daycarecateringbillgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jedrekp.daycarecateringbillgenerator.utility.YearAttributeConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Month;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "catering_bill",
        uniqueConstraints = @UniqueConstraint(columnNames = {"month", "year", "child_id"}))
@Getter
@Setter
@NoArgsConstructor
public class CateringBill {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Month month;

    @Column(columnDefinition = "int")
    @NotNull
    @Convert(converter = YearAttributeConverter.class)
    private Year year;

    @Column(name = "correction_flag")
    @NotNull
    private boolean correction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    @JsonIgnoreProperties({"daycareGroup", "assignedOptions", "cateringBills",})
    @NotNull
    private Child child;

    @OneToMany(mappedBy = "cateringBill", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JsonIgnoreProperties("cateringBill")
    @OrderBy(value = "orderDate ASC")
    private Set<DailyCateringOrder> dailyCateringOrders = new HashSet<>();

    public CateringBill(@NotNull Month month, @NotNull Year year) {
        this.month = month;
        this.year = year;
    }
}
