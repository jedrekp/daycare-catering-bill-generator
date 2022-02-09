package jedrekp.daycarecateringbillgenerator.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "catering_order",
        uniqueConstraints = @UniqueConstraint(columnNames = {"order_date", "catering_bill_id"}))
@Getter
@Setter
@NoArgsConstructor
public class DailyCateringOrder {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @Column(name = "order_date")
    @NotNull
    private LocalDate orderDate;

    @Column(name = "catering_option_name")
    @NotNull
    private String cateringOptionName;

    @Column(name = "price")
    @NotNull
    private BigDecimal cateringOptionPrice;

    @ManyToOne
    @JoinColumn(name = "catering_bill_id")
    @JsonIgnoreProperties({"child", "dailyCateringOrders"})
    private CateringBill cateringBill;

    public DailyCateringOrder(LocalDate orderDate, String cateringOptionName, BigDecimal cateringOptionPrice) {
        this.orderDate = orderDate;
        this.cateringOptionName = cateringOptionName;
        this.cateringOptionPrice = cateringOptionPrice;
    }
}
