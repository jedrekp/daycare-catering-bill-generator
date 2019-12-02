package jedrekp.daycarecateringbillgenerator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "daily_attendance")
@Getter
@Setter
@NoArgsConstructor
public class DailyAttendance {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @Column(name = "attendance_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "daily_attendance_present_child",
            joinColumns = @JoinColumn(name = "daily_attendance_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id"))
    @JsonIgnoreProperties({"daycareGroup", "assignedOptions"})
    private Set<Child> presentChildren = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "daily_attendance_absent_child",
            joinColumns = @JoinColumn(name = "daily_attendance_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id"))
    @JsonIgnoreProperties({"daycareGroup", "assignedOptions"})
    private Set<Child> absentChildren = new HashSet<>();

    public DailyAttendance(LocalDate date) {
        this.date = date;
    }
}
