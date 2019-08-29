package jedrekp.daycarecateringbillgenerator.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.Utility.JsonViewFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "daily_attendance")
public class DailyAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "date")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private LocalDate date;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "daily_attendance_child",
            joinColumns = @JoinColumn(name = "daily_attendance_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id"))
    @JsonView(JsonViewFilter.WithChildren.class)
    private Set<Child> presentChildren = new HashSet<>();

    public DailyAttendance(LocalDate date) {
        this.date = date;
    }
}
