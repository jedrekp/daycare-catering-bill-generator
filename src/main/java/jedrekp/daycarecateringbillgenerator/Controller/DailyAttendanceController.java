package jedrekp.daycarecateringbillgenerator.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.DTO.DailyGroupAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.DTO.SingleChildMonthlyAttendanceDTO;
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
import java.util.Collection;

@RestController
@CrossOrigin
public class DailyAttendanceController {

    @Autowired
    DailyAttendanceService dailyAttendanceService;

    @Autowired
    DailyAttendanceRepository dailyAttendanceRepository;

    @PostMapping("/dailyAttendances")
    @JsonView(JsonViewFilter.WithChildren.class)
    public ResponseEntity<DailyAttendance> submitAttendanceForGroup(
            @RequestBody @Valid DailyGroupAttendanceDTO dailyGroupAttendanceDTO) {
        return new ResponseEntity<>(dailyAttendanceService
                .submitAttendanceForGroup(dailyGroupAttendanceDTO), HttpStatus.OK);
    }

    @GetMapping(value = "/dailyAttendances", params = {"daycareGroupId", "date"})
    public ResponseEntity<DailyGroupAttendanceDTO> getDailyAttendanceForDaycareGroup(
            @RequestParam Long daycareGroupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(
                dailyAttendanceService.getDailyAttendanceForDaycareGroup(daycareGroupId, date), HttpStatus.OK);
    }

    @PostMapping(value = "/dailyAttendances/children/{childId}")
    @JsonView(JsonViewFilter.WithChildren.class)
    public ResponseEntity<Collection<DailyAttendance>> submitMonthlyAttendanceForSingleChild(
            @PathVariable Long childId, @RequestBody @Valid SingleChildMonthlyAttendanceDTO monthlyAttendanceDTO) {
        return new ResponseEntity<>(
                dailyAttendanceService.submitMonthlyAttendanceForChild(childId, monthlyAttendanceDTO), HttpStatus.OK);
    }

    @GetMapping(value = "/dailyAttendances/children/{childId}", params = {"month", "year"})
    public ResponseEntity<SingleChildMonthlyAttendanceDTO> getMonthlyAttendanceForChild(
            @PathVariable Long childId, @RequestParam Month month, @RequestParam Integer year) {
        return new ResponseEntity<>(dailyAttendanceService
                .getMonthlyAttendanceForChild(childId, month, year), HttpStatus.OK);
    }
}
