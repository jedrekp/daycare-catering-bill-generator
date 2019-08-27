package jedrekp.preschoolcateringbillcalculator.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "assigned_diet",
        uniqueConstraints = @UniqueConstraint(columnNames = {"effective_date", "child_id"}))
@Getter
@Setter
@NoArgsConstructor

public class AssignedDiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "child_id")
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "diet_id")
    private Diet diet;

    public AssignedDiet(LocalDate effectiveDate, Child child, Diet diet) {
        this.effectiveDate = effectiveDate;
        this.child = child;
        this.diet = diet;
    }

}
