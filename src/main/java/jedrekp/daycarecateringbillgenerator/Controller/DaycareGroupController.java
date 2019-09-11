package jedrekp.daycarecateringbillgenerator.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.Entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.Service.DaycareGroupService;
import jedrekp.daycarecateringbillgenerator.Utility.JsonViewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
public class DaycareGroupController {

    @Autowired
    DaycareGroupService daycareGroupService;

    @PostMapping("/daycareGroups")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<DaycareGroup> addNewDaycareGroup(@RequestBody DaycareGroup daycareGroup) {
        return new ResponseEntity<>(daycareGroupService.save(daycareGroup), HttpStatus.OK);
    }

    @PutMapping("/daycareGroups/{daycareGroupId}")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<DaycareGroup> editDaycareGroup(@PathVariable Long daycareGroupId, @RequestBody DaycareGroup daycareGroup) {
        return new ResponseEntity<>(daycareGroupService.editDaycareGroup(daycareGroupId, daycareGroup), HttpStatus.OK);
    }

    @DeleteMapping("/daycareGroups/{daycareGroupId}")
    public ResponseEntity deleteDaycareGroup(@PathVariable Long daycareGroupId) {
        daycareGroupService.deleteDaycareGroup(daycareGroupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/daycareGroups/{daycareGroupId}")
    @JsonView(JsonViewFilter.WithChildren.class)
    public ResponseEntity<DaycareGroup> getDaycareGroup(@PathVariable Long daycareGroupId) {
        return new ResponseEntity<>(daycareGroupService.findByIdWithChildren(daycareGroupId), HttpStatus.OK);
    }

    @GetMapping("/daycareGroups")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<DaycareGroup>> getAllDaycareGroups() {
        return new ResponseEntity<>(daycareGroupService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/daycareGroups/{daycareGroupId}/children/{childId}")
    @JsonView(JsonViewFilter.WithChildren.class)
    public ResponseEntity<DaycareGroup> addChildToDayCareGroup(@PathVariable Long daycareGroupId, @PathVariable Long childId) {
        return new ResponseEntity<>(
                daycareGroupService.addChildToDaycareGroup(daycareGroupId, childId), HttpStatus.OK);
    }

    @DeleteMapping("/daycareGroups/{daycareGroupId}/children/{childId}")
    @JsonView(JsonViewFilter.WithChildren.class)
    public ResponseEntity<DaycareGroup> removeChildFromGroup(@PathVariable Long daycareGroupId, @PathVariable Long childId) {
        return new ResponseEntity<>(daycareGroupService.removeChildFromDaycareGroup(daycareGroupId, childId), HttpStatus.OK);
    }
}
