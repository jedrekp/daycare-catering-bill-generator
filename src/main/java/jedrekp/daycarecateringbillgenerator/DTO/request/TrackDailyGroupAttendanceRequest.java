package jedrekp.daycarecateringbillgenerator.DTO.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class TrackDailyGroupAttendanceRequest {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    private Set<Long> idsOfChildrenToMarkAsPresent;

    @NotNull
    private Set<Long> idsOfChildrenToMarkAsAbsent;
}
