package jedrekp.daycarecateringbillgenerator.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.DTO.response.CateringBillResponse;
import jedrekp.daycarecateringbillgenerator.entity.Child;
import jedrekp.daycarecateringbillgenerator.entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.service.CateringBillService;
import jedrekp.daycarecateringbillgenerator.service.ChildService;
import jedrekp.daycarecateringbillgenerator.service.DaycareGroupService;
import jedrekp.daycarecateringbillgenerator.utility.JsonViewFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Month;
import java.time.Year;
import java.util.Collection;

@RestController
@RequestMapping("/daycareGroups")
@CrossOrigin
@RequiredArgsConstructor
public class DaycareGroupController {

    private final DaycareGroupService daycareGroupService;

    private final ChildService childService;

    private final CateringBillService cateringBillService;

    @GetMapping("/{daycareGroupId}")
    public ResponseEntity<DaycareGroup> getSingleDaycareGroup(@PathVariable long daycareGroupId) {
        return new ResponseEntity<>(daycareGroupService.findSingleGroupByIdWithAllDetails(daycareGroupId), HttpStatus.OK);
    }

    @GetMapping
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<DaycareGroup>> getAllDaycareGroups() {
        return new ResponseEntity<>(daycareGroupService.findAll(), HttpStatus.OK);
    }

    @GetMapping(params = "groupSupervisorId")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<DaycareGroup>> getDaycareGroupsByGroupSupervisorId(@RequestParam long groupSupervisorId) {
        return new ResponseEntity<>(daycareGroupService.findByGroupSupervisorId(groupSupervisorId), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('HEADMASTER')")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<DaycareGroup> addNewDaycareGroup(@RequestBody @Valid DaycareGroup daycareGroup) {
        return new ResponseEntity<>(daycareGroupService.saveNewDaycareGroup(daycareGroup), HttpStatus.CREATED);
    }

    @PutMapping("/{daycareGroupId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<DaycareGroup> editDaycareGroup(
            @PathVariable long daycareGroupId, @RequestBody @Valid DaycareGroup daycareGroup) {
        return new ResponseEntity<>(daycareGroupService.editDaycareGroup(daycareGroupId, daycareGroup), HttpStatus.OK);
    }

    @DeleteMapping("/{daycareGroupId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity deleteDaycareGroup(@PathVariable long daycareGroupId) {
        daycareGroupService.deleteDaycareGroup(daycareGroupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{daycareGroupId}/children")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<Child>> getChildrenFromDaycareGroup(@PathVariable Long daycareGroupId) {
        return new ResponseEntity<>(childService.findChildrenByDaycareGroup(daycareGroupId), HttpStatus.OK);
    }

    @PutMapping("/{daycareGroupId}/children/{childId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<DaycareGroup> assignChildToDayCareGroup(
            @PathVariable long daycareGroupId, @PathVariable long childId) {
        return new ResponseEntity<>(daycareGroupService.addChildToDaycareGroup(daycareGroupId, childId), HttpStatus.OK);
    }

    @DeleteMapping("/{daycareGroupId}/children/{childId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity removeChildFromGroup(@PathVariable long daycareGroupId, @PathVariable long childId) {
        daycareGroupService.removeChildFromDaycareGroup(daycareGroupId, childId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{daycareGroupId}/cateringBills", params = {"month", "year"})
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<Collection<CateringBillResponse>> getBillsForSpecificMonthForAllChildrenInGroup(
            @PathVariable long daycareGroupId, @RequestParam Month month, @RequestParam Year year) {
        return new ResponseEntity<>(
                cateringBillService.getAllCateringBillsByDaycareGroupIdAndMonth(daycareGroupId, month, year), HttpStatus.OK);

    }
}
