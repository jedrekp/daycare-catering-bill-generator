package jedrekp.daycarecateringbillgenerator.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.DTO.AssignedOptionDTO;
import jedrekp.daycarecateringbillgenerator.Entity.Child;
import jedrekp.daycarecateringbillgenerator.Service.ChildService;
import jedrekp.daycarecateringbillgenerator.Utility.JsonViewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
public class ChildController {

    @Autowired
    ChildService childService;

    @PostMapping("/children")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Child> addNewChild(@RequestBody Child child) {
        return new ResponseEntity<>(childService.save(child), HttpStatus.OK);
    }

    @PutMapping("/children/{childId}")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Child> EditChild(@PathVariable Long childId, @RequestBody Child child) {
        return new ResponseEntity<>(childService.editChild(childId, child), HttpStatus.OK);
    }

    @GetMapping("/children/{childId}")
    public ResponseEntity<Child> getChild(@PathVariable Long childId) {
        return new ResponseEntity<>(childService.findByIdWithAllDetails(childId), HttpStatus.OK);
    }

    @GetMapping("/children")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<Child>> getAllChildrenWithNoGroup() {
        return new ResponseEntity<>(childService.findChildrenFromGroup(null), HttpStatus.OK);
    }

    @GetMapping("/children/daycareGroups/{daycareGroupId}")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<Child>> getAllChildrenFromDaycareGroup(@PathVariable Long daycareGroupId) {
        return new ResponseEntity<>(childService.findChildrenFromGroup(daycareGroupId), HttpStatus.OK);
    }

    @PutMapping("/children/{childId}/daycareGroups/{daycareGroupId}")
    public ResponseEntity<Child> assignToGroup(@PathVariable Long childId, @PathVariable long daycareGroupId) {
        return new ResponseEntity<>(childService.assignToDaycareGroup(childId, daycareGroupId), HttpStatus.OK);
    }

    @PostMapping("/children/{childId}/assignedOptions")
    public ResponseEntity<Child> assignNewCateringOption(@PathVariable Long childId,
                                                         @RequestBody AssignedOptionDTO assignedOptionDTO) {
        return new ResponseEntity<>(childService.assignCateringOption(childId, assignedOptionDTO), HttpStatus.OK);
    }

    @DeleteMapping("/children/{childId}/assignedOptions/{assignedOptionId}")
    public ResponseEntity<Child> removeAssignedOptionFromChild(@PathVariable Long childId, @PathVariable
            Long assignedOptionId) {
        childService.removeAssignedOption(childId, assignedOptionId);
        return ResponseEntity.noContent().build();
    }
}
