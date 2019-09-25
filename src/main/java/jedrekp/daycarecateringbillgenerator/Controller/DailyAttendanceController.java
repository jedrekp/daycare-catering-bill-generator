package jedrekp.daycarecateringbillgenerator.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.DTO.DailyAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.Entity.DailyAttendance;
import jedrekp.daycarecateringbillgenerator.Repository.DailyAttendanceRepository;
import jedrekp.daycarecateringbillgenerator.Service.DailyAttendanceService;
import jedrekp.daycarecateringbillgenerator.Utility.JsonViewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@CrossOrigin
public class DailyAttendanceController {

    @Autowired
    DailyAttendanceService dailyAttendanceService;

    @Autowired
    DailyAttendanceRepository dailyAttendanceRepository;

    @PostMapping("/dailyAttendances")
    @JsonView(JsonViewFilter.WithChildren.class)
    public ResponseEntity<DailyAttendance> markAttendance(@RequestBody @Valid DailyAttendanceDTO dailyAttendanceDTO) {
        return new ResponseEntity<>(dailyAttendanceService.markAttendance(dailyAttendanceDTO), HttpStatus.OK);
    }

    @GetMapping(value = "/dailyAttendances", params = {"daycareGroupId", "date"})
    public ResponseEntity<DailyAttendanceDTO> getDailyAttendanceForDaycareGroup(
            @RequestParam Long daycareGroupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(
                dailyAttendanceService.getDailyAttendanceForDaycareGroup(daycareGroupId, date), HttpStatus.OK);
    }

    @GetMapping(value = "/dailyAttendances/children/{childId}", params = {"month", "year"})
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<List<DailyAttendance>> getMonthlyAttendanceForChild(
            @PathVariable Long childId, @RequestParam Month month, @RequestParam Integer year) {
        return new ResponseEntity<>(dailyAttendanceRepository
                .findByChildIdForSpecificMonth(childId, month.getValue(), year),
                HttpStatus.OK);
    }
}
