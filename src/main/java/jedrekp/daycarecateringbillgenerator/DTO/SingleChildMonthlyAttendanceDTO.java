package jedrekp.daycarecateringbillgenerator.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SingleChildMonthlyAttendanceDTO {

    private Month month;

    private int year;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Set<LocalDate> daysWhenPresent = new HashSet<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Set<LocalDate> daysWhenAbsent = new HashSet<>();

    public SingleChildMonthlyAttendanceDTO(Month month, int year) {
        this.month = month;
        this.year = year;
    }
}
