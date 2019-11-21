package jedrekp.daycarecateringbillgenerator.Controller;

import jedrekp.daycarecateringbillgenerator.Entity.AssignedOption;
import jedrekp.daycarecateringbillgenerator.Entity.Child;
import jedrekp.daycarecateringbillgenerator.Service.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin
public class AssignedOptionController {

    @Autowired
    private ChildService childService;

    @PostMapping(value = "/assignedOptions", params = {"childId", "cateringOptionId", "effectiveDate"})
    public ResponseEntity
    assignCateringOptionToChild(@RequestParam Long childId, @RequestParam Long cateringOptionId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveDate) {
                childService.assignCateringOption(childId, cateringOptionId, effectiveDate);
                        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/children/{childId}/assignedOptions/{assignedOptionId}")
    public ResponseEntity<Child> removeAssignedOptionFromChild(@PathVariable Long childId, @PathVariable
            Long assignedOptionId) {
        return new ResponseEntity<>(childService.removeAssignedOption(childId, assignedOptionId), HttpStatus.OK);
    }

}
