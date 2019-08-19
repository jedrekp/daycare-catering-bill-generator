package jedrekp.preschoolcateringbillcalculator.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.preschoolcateringbillcalculator.DTO.DailyAttendanceDTO;
import jedrekp.preschoolcateringbillcalculator.DTO.MonthInput;
import jedrekp.preschoolcateringbillcalculator.Entity.DailyAttendance;
import jedrekp.preschoolcateringbillcalculator.Repository.DailyAttendanceRepository;
import jedrekp.preschoolcateringbillcalculator.Service.DailyAttendanceService;
import jedrekp.preschoolcateringbillcalculator.Utility.JsonViewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/dailyAttendances/child/{childId}")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<List<DailyAttendance>> getAttendanceForSpecificChildAndMonth(
            @PathVariable Long childId, @RequestBody MonthInput monthInput) {
        return new ResponseEntity<>(dailyAttendanceRepository
                .findByChildIdForSpecificMonth(childId, monthInput.getMonth().getValue(), monthInput.getYear()), HttpStatus.OK);
    }
}
