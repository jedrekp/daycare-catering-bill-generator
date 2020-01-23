package jedrekp.daycarecateringbillgenerator.controller;

import jedrekp.daycarecateringbillgenerator.entity.DaycareEmployee;
import jedrekp.daycarecateringbillgenerator.service.DaycareEmployeeService;
import jedrekp.daycarecateringbillgenerator.utility.DaycareRole;
import jedrekp.daycarecateringbillgenerator.utility.PreEncodedPasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/{daycareEmployeeId}")
    public ResponseEntity<DaycareEmployee> getSingleDaycareEmployee(@PathVariable Long daycareEmployeeId) {
        return new ResponseEntity<>(daycareEmployeeService.findSingleEmployeeByIdWithAllDetails(daycareEmployeeId), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_HEADMASTER')")
    public ResponseEntity<Collection<DaycareEmployee>> getAllDaycareEmployees() {
        return new ResponseEntity<>(daycareEmployeeService.findAllDaycareEmployees(), HttpStatus.OK);
    }

    @GetMapping(params = "daycareRole")
    @PreAuthorize("hasAuthority('ROLE_HEADMASTER')")
    public ResponseEntity<Collection<DaycareEmployee>> getDaycareEmployeesWithSpecificRole(@RequestParam DaycareRole daycareRole) {
        return new ResponseEntity<>(daycareEmployeeService.findDaycareEmployeesWithSpecificRole(daycareRole), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_HEADMASTER')")
    public ResponseEntity<DaycareEmployee> addNewGroupSupervisor(@RequestBody @Valid DaycareEmployee daycareEmployee) {
        return new ResponseEntity<>(daycareEmployeeService.addNewGroupSupervisor(daycareEmployee), HttpStatus.CREATED);
    }

}
