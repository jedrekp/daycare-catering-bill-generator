package jedrekp.daycarecateringbillgenerator.controller;

import jedrekp.daycarecateringbillgenerator.DTO.DailyGroupAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.DTO.SingleChildMonthlyAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.entity.DailyAttendance;
import jedrekp.daycarecateringbillgenerator.service.DailyAttendanceService;
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
@RequestMapping("/dailyAttendances")
@CrossOrigin
@RequiredArgsConstructor
public class DailyAttendanceController {

    private final DailyAttendanceService dailyAttendanceService;

    @PostMapping(params = "daycareGroupId")
    public ResponseEntity<DailyAttendance> submitAttendanceForGroup(
            @RequestParam long daycareGroupId, @RequestBody @Valid DailyGroupAttendanceDTO dailyGroupAttendanceDTO) {
        return new ResponseEntity<>(dailyAttendanceService.submitAttendanceForGroup(dailyGroupAttendanceDTO), HttpStatus.OK);
    }

    @GetMapping(params = {"daycareGroupId", "date"})
    public ResponseEntity<DailyGroupAttendanceDTO> getDailyAttendanceForDaycareGroup(
            @RequestParam long daycareGroupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(dailyAttendanceService.getDailyAttendanceForDaycareGroup(daycareGroupId, date), HttpStatus.OK);
    }

    @PostMapping(params = "childId")
    public ResponseEntity<Collection<DailyAttendance>> submitMonthlyAttendanceForSingleChild(
            @RequestParam long childId, @RequestBody @Valid SingleChildMonthlyAttendanceDTO monthlyAttendanceDTO) {
        return new ResponseEntity<>(
                dailyAttendanceService.submitMonthlyAttendanceForChild(childId, monthlyAttendanceDTO), HttpStatus.OK);
    }

    @GetMapping(params = {"childId", "month", "year"})
    public ResponseEntity<SingleChildMonthlyAttendanceDTO> getMonthlyAttendanceForChild(
            @RequestParam long childId, @RequestParam Month month, @RequestParam Year year) {
        return new ResponseEntity<>(dailyAttendanceService
                .getMonthlyAttendanceForChild(childId, month, year), HttpStatus.OK);
    }
}
