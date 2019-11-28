package jedrekp.daycarecateringbillgenerator.Controller;

import jedrekp.daycarecateringbillgenerator.DTO.DailyGroupAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.DTO.SingleChildMonthlyAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.Entity.DailyAttendance;
import jedrekp.daycarecateringbillgenerator.Repository.DailyAttendanceRepository;
import jedrekp.daycarecateringbillgenerator.Service.DailyAttendanceService;
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
@RequestMapping
@CrossOrigin
public class DailyAttendanceController {

    @Autowired
    DailyAttendanceService dailyAttendanceService;

    @Autowired
    DailyAttendanceRepository dailyAttendanceRepository;

    @PostMapping()
    public ResponseEntity<DailyAttendance> submitAttendanceForGroup(
            @RequestParam Long daycareGroupId, @RequestBody @Valid DailyGroupAttendanceDTO dailyGroupAttendanceDTO) {
        return new ResponseEntity<>(dailyAttendanceService.submitAttendanceForGroup(dailyGroupAttendanceDTO), HttpStatus.OK);
    }

    @GetMapping(value = "/dailyAttendances", params = {"daycareGroupId", "date"})
    public ResponseEntity<DailyGroupAttendanceDTO> getDailyAttendanceForDaycareGroup(
            @RequestParam Long daycareGroupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(dailyAttendanceService.getDailyAttendanceForDaycareGroup(daycareGroupId, date), HttpStatus.OK);
    }

    @PostMapping(value = "/dailyAttendances", params = "childId")
    public ResponseEntity<Collection<DailyAttendance>> submitMonthlyAttendanceForSingleChild(
            @RequestParam Long childId, @RequestBody @Valid SingleChildMonthlyAttendanceDTO monthlyAttendanceDTO) {
        return new ResponseEntity<>(
                dailyAttendanceService.submitMonthlyAttendanceForChild(childId, monthlyAttendanceDTO), HttpStatus.OK);
    }

    @GetMapping(value = "/dailyAttendances", params = {"childId", "month", "year"})
    public ResponseEntity<SingleChildMonthlyAttendanceDTO> getMonthlyAttendanceForChild(
            @RequestParam Long childId, @RequestParam Month month, @RequestParam Integer year) {
        return new ResponseEntity<>(dailyAttendanceService
                .getMonthlyAttendanceForChild(childId, month, year), HttpStatus.OK);
    }
}
