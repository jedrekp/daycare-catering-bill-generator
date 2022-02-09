package jedrekp.daycarecateringbillgenerator.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.DTO.request.AssignOptionToChildRequest;
import jedrekp.daycarecateringbillgenerator.DTO.request.CreateCateringBillRequest;
import jedrekp.daycarecateringbillgenerator.DTO.response.CateringBillResponse;
import jedrekp.daycarecateringbillgenerator.entity.AssignedOption;
import jedrekp.daycarecateringbillgenerator.entity.CateringBill;
import jedrekp.daycarecateringbillgenerator.entity.Child;
import jedrekp.daycarecateringbillgenerator.service.CateringBillService;
import jedrekp.daycarecateringbillgenerator.service.ChildService;
import jedrekp.daycarecateringbillgenerator.utility.JsonViewFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
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
    @PreAuthorize("hasRole('HEADMASTER')")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Child> addNewChild(@RequestBody @Valid Child child) {
        return new ResponseEntity<>(childService.saveNewChild(child), HttpStatus.CREATED);
    }

    @PutMapping("/{childId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Child> editChild(@PathVariable long childId, @RequestBody @Valid Child child) {
        return new ResponseEntity<>(childService.editChild(childId, child), HttpStatus.OK);
    }

    @PostMapping("/{childId}/assignedOptions")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<AssignedOption> assignNewCateringOptionToChild(
            @PathVariable long childId, @RequestBody @Valid AssignOptionToChildRequest assignOptionToChildRequest) {
        return new ResponseEntity<>(childService.assignCateringOption(childId, assignOptionToChildRequest), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{childId}/assignedOptions", params = "effectiveDate")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity removeAssignedOptionFromChild(
            @PathVariable long childId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveDate) {
        childService.removeAssignedOption(childId, effectiveDate);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{childId}/cateringBills", params = {"month", "year"})
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<CateringBillResponse> getCateringBillForSpecificMonth(
            @PathVariable long childId, @RequestParam Month month, @RequestParam Year year) {
        return new ResponseEntity<>(cateringBillService.getCateringBillByChildIdAndMonth(childId, month, year), HttpStatus.OK);
    }

    @PostMapping("/{childId}/cateringBills")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<CateringBillResponse> addNewCateringBillToChild(
            @PathVariable long childId, @RequestBody @Valid CreateCateringBillRequest cateringBillRequest) {
        return new ResponseEntity<>(cateringBillService.mapExistingCateringBillToResponse(
                cateringBillService.addNewCateringBillToChild(childId, cateringBillRequest)), HttpStatus.CREATED);
    }
}
