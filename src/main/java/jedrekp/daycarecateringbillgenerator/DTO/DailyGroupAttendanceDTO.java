package jedrekp.daycarecateringbillgenerator.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class DailyGroupAttendanceDTO {

    @NotNull
    private long daycareGroupId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Set<Long> presentChildrenIds = new HashSet<>();

    private Set<Long> absentChildrenIds = new HashSet<>();

    public DailyGroupAttendanceDTO(long daycareGroupId, LocalDate date) {
        this.date = date;
    }
}
