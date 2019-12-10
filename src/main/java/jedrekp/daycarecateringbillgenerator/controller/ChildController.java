package jedrekp.daycarecateringbillgenerator.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.DTO.AssignedOptionDTO;
import jedrekp.daycarecateringbillgenerator.DTO.CateringBillDTO;
import jedrekp.daycarecateringbillgenerator.entity.AssignedOption;
import jedrekp.daycarecateringbillgenerator.entity.CateringBill;
import jedrekp.daycarecateringbillgenerator.entity.Child;
import jedrekp.daycarecateringbillgenerator.service.CateringBillService;
import jedrekp.daycarecateringbillgenerator.service.ChildService;
import jedrekp.daycarecateringbillgenerator.utility.JsonViewFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Month;
import java.time.Year;
import java.util.Collection;

@RestController
@RequestMapping("/children")
@CrossOrigin
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;

    private final CateringBillService cateringBillService;

    @GetMapping("/{childId}")
    public ResponseEntity<Child> getSingleChild(@PathVariable long childId) {
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
    public ResponseEntity<Child> EditChild(@PathVariable long childId, @RequestBody Child child) {
        return new ResponseEntity<>(childService.editChild(childId, child), HttpStatus.OK);
    }

    @PostMapping("/{childId}/assignedOptions")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<AssignedOption> assignNewCateringOptionToChild(
            @PathVariable long childId, @RequestBody @Valid AssignedOptionDTO assignedOptionDTO) {
        return new ResponseEntity<>(childService.assignCateringOption(childId, assignedOptionDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{childId}/assignedOptions/{assignedOptionId}")
    public ResponseEntity removeAssignedOptionFromChild(
            @PathVariable long childId, @PathVariable long assignedOptionId) {
        childService.removeAssignedOption(childId, assignedOptionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{childId}/cateringBills", params = {"month", "year"})
    public ResponseEntity<CateringBill> getCateringBillForSpecificMonth(
            @PathVariable long childId, @RequestParam Month month, @RequestParam Year year) {
        return null;
    }

    @PostMapping("/{childId}/cateringBills")
    public ResponseEntity<CateringBill> addNewCateringBillOrBillCorrectionToChild(
            @PathVariable long childId, @RequestBody @Valid CateringBillDTO cateringBillDTO) {
        return new ResponseEntity<>(cateringBillService.saveOrEditCateringBill(childId, cateringBillDTO), HttpStatus.CREATED);
    }
}
