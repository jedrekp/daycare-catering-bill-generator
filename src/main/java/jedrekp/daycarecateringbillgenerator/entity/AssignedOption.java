package jedrekp.daycarecateringbillgenerator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "assigned_option",
        uniqueConstraints = @UniqueConstraint(columnNames = {"effective_date", "child_id"}))
@Getter
@Setter
@NoArgsConstructor
public class AssignedOption {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "effective_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catering_option_id")
    private CateringOption cateringOption;

    public AssignedOption(LocalDate effectiveDate, Child child, CateringOption cateringOption) {
        this.effectiveDate = effectiveDate;
        this.child = child;
        this.cateringOption = cateringOption;
    }
}
