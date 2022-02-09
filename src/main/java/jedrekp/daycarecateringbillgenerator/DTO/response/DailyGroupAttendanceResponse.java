package jedrekp.daycarecateringbillgenerator.DTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class DailyGroupAttendanceResponse {


    private long daycareGroupId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Set<Long> presentChildrenIds = new HashSet<>();

    private Set<Long> absentChildrenIds = new HashSet<>();

    public DailyGroupAttendanceResponse(long daycareGroupId, LocalDate date) {
        this.daycareGroupId = daycareGroupId;
        this.date = date;
    }
}
