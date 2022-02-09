package jedrekp.daycarecateringbillgenerator.DTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class ChildMonthlyAttendanceResponse {

    private long childId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Set<LocalDate> datesWhenPresent = new HashSet<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Set<LocalDate> datesWhenAbsent = new HashSet<>();

    public ChildMonthlyAttendanceResponse(@NotNull long childId) {
        this.childId = childId;
    }
}
