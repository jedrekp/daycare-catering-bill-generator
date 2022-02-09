package jedrekp.daycarecateringbillgenerator.controller;

import jedrekp.daycarecateringbillgenerator.DTO.request.TrackDailyGroupAttendanceRequest;
import jedrekp.daycarecateringbillgenerator.DTO.request.UpdateMonthlyAttendanceForChildRequest;
import jedrekp.daycarecateringbillgenerator.DTO.response.ChildMonthlyAttendanceResponse;
import jedrekp.daycarecateringbillgenerator.DTO.response.DailyGroupAttendanceResponse;
import jedrekp.daycarecateringbillgenerator.entity.AttendanceSheet;
import jedrekp.daycarecateringbillgenerator.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping(params = {"daycareGroupId", "date"})
    public ResponseEntity<DailyGroupAttendanceResponse> getDailyAttendanceForDaycareGroup(
            @RequestParam long daycareGroupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(attendanceService.getDailyAttendanceForDaycareGroup(date,daycareGroupId), HttpStatus.OK);
    }

    @PutMapping(params = "daycareGroupId")
    @PreAuthorize("hasRole('HEADMASTER') or @attendanceTrackingPermissionEvaluator.canMarkAttendanceForDaycareGroup(#daycareGroupId,authentication)")
    public ResponseEntity<AttendanceSheet> submitAttendanceForGroup(
            @RequestParam long daycareGroupId, @RequestBody @Valid TrackDailyGroupAttendanceRequest attendanceRequest) {
        return new ResponseEntity<>(attendanceService.submitDailyAttendanceForGroup(attendanceRequest, daycareGroupId), HttpStatus.OK);
    }

    @GetMapping(params = {"childId", "month", "year"})
    public ResponseEntity<ChildMonthlyAttendanceResponse> getMonthlyAttendanceForChild(
            @RequestParam long childId, @RequestParam Month month, @RequestParam Year year) {
        return new ResponseEntity<>(attendanceService.getMonthlyAttendanceForChild(childId, month, year), HttpStatus.OK);
    }

    @PutMapping(params = {"childId", "month", "year"})
    @PreAuthorize("hasRole('HEADMASTER') or @attendanceTrackingPermissionEvaluator.canMarkAttendanceForChild(#childId,authentication)")
    public ResponseEntity<Collection<AttendanceSheet>> submitMonthlyAttendanceChangesForSingleChild(
            @RequestParam long childId, @RequestParam Month month, @RequestParam Year year,
            @RequestBody @Valid UpdateMonthlyAttendanceForChildRequest attendanceRequest) {
        return new ResponseEntity<>(
                attendanceService.submitMonthlyAttendanceChangesForChild(childId, month, year, attendanceRequest), HttpStatus.OK);
    }
}
