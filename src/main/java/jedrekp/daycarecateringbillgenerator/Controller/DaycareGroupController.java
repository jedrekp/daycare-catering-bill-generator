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
@RequestMapping("/daycareGroups")
@CrossOrigin
public class DaycareGroupController {

    @Autowired
    DaycareGroupService daycareGroupService;

    @GetMapping("/{daycareGroupId}")
    public ResponseEntity<DaycareGroup> getSingleDaycareGroup(@PathVariable Long daycareGroupId) {
        return new ResponseEntity<>(daycareGroupService.findSingleGroupByIdWithChildren(daycareGroupId), HttpStatus.OK);
    }

    @GetMapping
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<DaycareGroup>> getAllDaycareGroups() {
        return new ResponseEntity<>(daycareGroupService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<DaycareGroup> addNewDaycareGroup(@RequestBody DaycareGroup daycareGroup) {
        return new ResponseEntity<>(daycareGroupService.saveNewDaycareGroup(daycareGroup), HttpStatus.CREATED);
    }

    @PutMapping("/{daycareGroupId}")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<DaycareGroup> editDaycareGroup(
            @PathVariable Long daycareGroupId, @RequestBody DaycareGroup daycareGroup) {
        return new ResponseEntity<>(daycareGroupService.editDaycareGroup(daycareGroupId, daycareGroup), HttpStatus.OK);
    }

    @DeleteMapping("/{daycareGroupId}")
    public ResponseEntity deleteDaycareGroup(@PathVariable Long daycareGroupId) {
        daycareGroupService.deleteDaycareGroup(daycareGroupId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{daycareGroupId}/children/{childId}")
    public ResponseEntity<DaycareGroup> addChildToDayCareGroup(
            @PathVariable Long daycareGroupId, @PathVariable Long childId) {
        return new ResponseEntity<>(daycareGroupService.addChildToDaycareGroup(daycareGroupId, childId), HttpStatus.OK);
    }

    @DeleteMapping("/{daycareGroupId}/children/{childId}")
    public ResponseEntity removeChildFromGroup(@PathVariable Long daycareGroupId, @PathVariable Long childId) {
        daycareGroupService.removeChildFromDaycareGroup(daycareGroupId, childId);
        return ResponseEntity.noContent().build();
    }
}
