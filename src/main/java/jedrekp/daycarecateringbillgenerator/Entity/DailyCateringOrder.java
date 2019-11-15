package jedrekp.daycarecateringbillgenerator.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "catering_option_name")
    private String cateringOptionName;

    @Column(name = "price")
    private BigDecimal cateringOptionPrice;

    @ManyToOne
    @JoinColumn(name = "catering_bill_id")
    @JsonIgnoreProperties({"child", "dailyCateringOrders"})
    private CateringBill cateringBill;

    public DailyCateringOrder(LocalDate orderDate, String cateringOptionName, BigDecimal cateringOptionPrice,
                              CateringBill cateringBill) {
        this.orderDate = orderDate;
        this.cateringOptionName = cateringOptionName;
        this.cateringOptionPrice = cateringOptionPrice;
        this.cateringBill = cateringBill;
    }
}
