package jedrekp.daycarecateringbillgenerator.controller;

import jedrekp.daycarecateringbillgenerator.entity.DaycareEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/daycareEmployees")
@CrossOrigin
@RequiredArgsConstructor
public class DaycareEmployeeController {

    @GetMapping
    public ResponseEntity<Collection<DaycareEmployee>> getAllDaycareEmployees() {
        return null;
    }

    @GetMapping(params = "daycareRole")
    public ResponseEntity<Collection<DaycareEmployee>> getAllDaycareEmployeesWithSpecificRole() {
        return null;
    }

    @GetMapping("/{daycareEmployeeId}")

    public ResponseEntity<DaycareEmployee> getSingleDaycareEmployee(@PathVariable Long daycareEmployeeId) {
        return null;
    }

}
