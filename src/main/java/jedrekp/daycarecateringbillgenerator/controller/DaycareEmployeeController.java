package jedrekp.daycarecateringbillgenerator.controller;

import jedrekp.daycarecateringbillgenerator.entity.DaycareEmployee;
import jedrekp.daycarecateringbillgenerator.service.DaycareEmployeeService;
import jedrekp.daycarecateringbillgenerator.utility.PreEncodedPasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/daycareEmployees")
@CrossOrigin
@RequiredArgsConstructor
public class DaycareEmployeeController {

    private final DaycareEmployeeService daycareEmployeeService;
    private final PreEncodedPasswordValidator preEncodedPasswordValidator;

    @InitBinder("daycareEmployee")
    public void dataBinding(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(preEncodedPasswordValidator);
    }

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

    @PostMapping
    public ResponseEntity<DaycareEmployee> addNewGroupSupervisor(@RequestBody @Valid DaycareEmployee daycareEmployee) {
        return new ResponseEntity<>(daycareEmployeeService.addNewGroupSupervisor(daycareEmployee), HttpStatus.OK);
    }

}
