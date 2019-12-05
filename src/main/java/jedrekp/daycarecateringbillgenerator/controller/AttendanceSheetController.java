package jedrekp.daycarecateringbillgenerator.controller;

import jedrekp.daycarecateringbillgenerator.DTO.DailyGroupAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.DTO.SingleChildMonthlyAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.entity.AttendanceSheet;
import jedrekp.daycarecateringbillgenerator.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Collection;

@RestController
@RequestMapping("/attendanceSheets")
@CrossOrigin
@RequiredArgsConstructor
public class AttendanceSheetController {

    private final AttendanceService attendanceService;

    @PostMapping(params = "daycareGroupId")
    public ResponseEntity<AttendanceSheet> submitAttendanceForGroup(
            @RequestParam long daycareGroupId, @RequestBody @Valid DailyGroupAttendanceDTO dailyGroupAttendanceDTO) {
        return new ResponseEntity<>(attendanceService.submitDailyAttendanceForGroup(dailyGroupAttendanceDTO), HttpStatus.OK);
    }

    @GetMapping(params = {"daycareGroupId", "date"})
    public ResponseEntity<DailyGroupAttendanceDTO> getDailyAttendanceForDaycareGroup(
            @RequestParam long daycareGroupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(attendanceService.getDailyAttendanceForDaycareGroup(daycareGroupId, date), HttpStatus.OK);
    }

    @PutMapping(params = {"childId", "month", "year"})
    public ResponseEntity<Collection<AttendanceSheet>> submitMonthlyAttendanceChangesForSingleChild(
            @RequestParam long childId, @RequestParam Month month, @RequestParam Year year,
            @RequestBody @Valid SingleChildMonthlyAttendanceDTO monthlyAttendanceDTO) {
        return new ResponseEntity<>(
                attendanceService.submitMonthlyAttendanceChangesForChild(childId, month, year, monthlyAttendanceDTO), HttpStatus.OK);
    }

    @GetMapping(params = {"childId", "month", "year"})
    public ResponseEntity<SingleChildMonthlyAttendanceDTO> getMonthlyAttendanceForChild(
            @RequestParam long childId, @RequestParam Month month, @RequestParam Year year) {
        return new ResponseEntity<>(attendanceService
                .getMonthlyAttendanceForChild(childId, month, year), HttpStatus.OK);
    }
}
