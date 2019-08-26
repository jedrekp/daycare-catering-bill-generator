package jedrekp.preschoolcateringbillcalculator.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.preschoolcateringbillcalculator.Utility.JsonViewFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "child_diet",
        uniqueConstraints = @UniqueConstraint(columnNames = {"effective_date", "child_id"}))
@Getter
@Setter
@NoArgsConstructor

public class ChildDiet implements Comparable<ChildDiet> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "effective_date")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private LocalDate effectiveDate;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "child_id")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "diet_id")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private Diet diet;

    public ChildDiet(LocalDate effectiveDate, Child child, Diet diet) {
        this.effectiveDate = effectiveDate;
        this.child = child;
        this.diet = diet;
    }

    @Override
    public int compareTo(ChildDiet o) {
        return this.effectiveDate.compareTo(o.effectiveDate);
    }

}
