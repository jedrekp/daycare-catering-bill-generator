package jedrekp.daycarecateringbillgenerator.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.DTO.AssignedOptionDTO;
import jedrekp.daycarecateringbillgenerator.DTO.CateringBillDTO;
import jedrekp.daycarecateringbillgenerator.Entity.AssignedOption;
import jedrekp.daycarecateringbillgenerator.Entity.CateringBill;
import jedrekp.daycarecateringbillgenerator.Entity.Child;
import jedrekp.daycarecateringbillgenerator.Service.ChildService;
import jedrekp.daycarecateringbillgenerator.Utility.JsonViewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Month;
import java.util.Collection;

@RestController
@RequestMapping("/children")
@CrossOrigin
public class ChildController {

    @Autowired
    ChildService childService;

    @GetMapping("/{childId}")
    public ResponseEntity<Child> getSingleChild(@PathVariable Long childId) {
        return new ResponseEntity<>(childService.findSingleChildByIdWithAllDetails(childId), HttpStatus.OK);
    }

    @GetMapping
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<Child>> getAllChildren() {
        return new ResponseEntity<>(childService.findAll(), HttpStatus.OK);
    }

    @GetMapping(params = "archived")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<Child>> getChildrenByArchived(@RequestParam boolean archived) {
        return new ResponseEntity<>(childService.findChildrenByArchived(archived), HttpStatus.OK);
    }

    @GetMapping(params = "searchPhrase")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<Child>> getChildrenBySearchPhrase(@RequestParam String searchPhrase) {
        return new ResponseEntity<>(childService.findChildrenBySearchPhrase(searchPhrase), HttpStatus.OK);
    }

    @PostMapping
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Child> addNewChild(@RequestBody Child child) {
        return new ResponseEntity<>(childService.saveNewChild(child), HttpStatus.CREATED);
    }

    @PutMapping("/{childId}")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Child> EditChild(@PathVariable Long childId, @RequestBody Child child) {
        return new ResponseEntity<>(childService.editChild(childId, child), HttpStatus.OK);
    }

    @PostMapping("/children/{childId}/assignedOptions")
    public ResponseEntity<AssignedOption> assignNewCateringOptionToChild(
            @PathVariable Long childId, @RequestBody @Valid AssignedOptionDTO assignedOptionDTO) {
        return new ResponseEntity<>(childService.assignCateringOption(childId, assignedOptionDTO), HttpStatus.OK);
    }

    @DeleteMapping("/children/{childId}/assignedOptions/{assignedOptionId}")
    public ResponseEntity removeAssignedOptionFromChild(
            @PathVariable Long childId, @PathVariable Long assignedOptionId) {
        childService.removeAssignedOption(childId, assignedOptionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{childId}/cateringBills", params = {"month", "year"})
    public ResponseEntity<CateringBill> getCateringBillForSpecificMonth(
            @PathVariable Long childId, @RequestParam Month month, @RequestParam int year) {
        return null;
    }

    @PostMapping("/{childId}}/cateringBills")
    public ResponseEntity addNewCateringBillToChild(
            @PathVariable Long childId, @RequestBody CateringBillDTO cateringBillDTO) {
        return null;
    }
}
