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
        return new ResponseEntity<>(childService.saveNewChild(child), HttpStatus.OK);
    }

    @PutMapping("/children/{childId}")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Child> EditChild(@PathVariable Long childId, @RequestBody Child child) {
        return new ResponseEntity<>(childService.editChild(childId, child), HttpStatus.OK);
    }

    @GetMapping("/children/{childId}")
    public ResponseEntity<Child> getSingleChild(@PathVariable Long childId) {
        return new ResponseEntity<>(childService.findSingleChildByIdWithAllDetails(childId), HttpStatus.OK);
    }

    @GetMapping(value = "/children", params = "daycareGroupId")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<Child>> getChildrenByDaycareGroup(@RequestParam Long daycareGroupId) {
        return new ResponseEntity<>(childService.findChildrenByDaycareGroup(daycareGroupId), HttpStatus.OK);
    }

    @GetMapping(value = "/children", params = "archived")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<Child>> getChildrenByArchived(@RequestParam boolean archived) {
        return new ResponseEntity<>(childService.findChildrenByArchived(archived), HttpStatus.OK);
    }

    @GetMapping(value = "/children", params = "searchPhrase")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<Child>> getChildrenBySearchPhrase(@RequestParam String searchPhrase) {
        return new ResponseEntity<>(childService.findChildrenBySearchPhrase(searchPhrase), HttpStatus.OK);
    }
}
