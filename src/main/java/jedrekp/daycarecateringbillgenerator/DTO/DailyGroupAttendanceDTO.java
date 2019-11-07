package jedrekp.daycarecateringbillgenerator.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class DailyGroupAttendanceDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Set<Long> presentChildrenIds = new HashSet<>();

    private Set<Long> absentChildrenIds = new HashSet<>();

    public DailyGroupAttendanceDTO(LocalDate date) {
        this.date = date;
    }
}
