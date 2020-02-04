package jedrekp.daycarecateringbillgenerator.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.entity.AppUser;
import jedrekp.daycarecateringbillgenerator.service.AppUserService;
import jedrekp.daycarecateringbillgenerator.utility.DaycareRole;
import jedrekp.daycarecateringbillgenerator.utility.JsonViewFilter;
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
@RequestMapping("/appUsers")
@CrossOrigin
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final PreEncodedPasswordValidator preEncodedPasswordValidator;

    @InitBinder("appUser")
    public void dataBinding(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(preEncodedPasswordValidator);
    }

    @GetMapping("/{appUserId}")
    public ResponseEntity<AppUser> getSingleAppUser(@PathVariable long appUserId) {
        return new ResponseEntity<>(appUserService.findSingleAppUserByIdWithAllDetails(appUserId), HttpStatus.OK);
    }

    @GetMapping(params = "username")
    public ResponseEntity<AppUser> getSingleAppUserByUsername(@RequestParam String username) {
        return new ResponseEntity<>(appUserService.findSingleAppUserByUsername(username), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('HEADMASTER')")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<AppUser>> getAllAppUsers() {
        return new ResponseEntity<>(appUserService.findAllAppUsers(), HttpStatus.OK);
    }

    @GetMapping(params = "daycareRole")
    @PreAuthorize("hasAuthority('HEADMASTER')")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<AppUser>> getAppUsersWithSpecificRole(@RequestParam DaycareRole daycareRole) {
        return new ResponseEntity<>(appUserService.findAllAppUsersByDaycareRole(daycareRole), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('HEADMASTER')")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<AppUser> addNewGroupSupervisor(@RequestBody @Valid AppUser appUser) {
        return new ResponseEntity<>(appUserService.createNewGroupSupervisorAccount(appUser), HttpStatus.CREATED);
    }

    @PutMapping("{appUserId}/daycareGroups/{daycareGroupId}")
    @PreAuthorize("hasAuthority('HEADMASTER')")
    public ResponseEntity<AppUser> assignDaycareGroupToGroupSupervisor(
            @PathVariable long appUserId, @PathVariable long daycareGroupId) {
        return new ResponseEntity<>(appUserService.assignDaycareGroupToGroupSupervisor(appUserId, daycareGroupId), HttpStatus.OK);
    }

    @DeleteMapping("{appUserId}/daycareGroups/{daycareGroupId}")
    @PreAuthorize("hasAuthority('HEADMASTER')")
    public ResponseEntity removeDaycareGroupFromGroupSupervisor(@PathVariable long appUserId, @PathVariable long daycareGroupId) {
        appUserService.removeAssignedGroupFromGroupSupervisor(appUserId, daycareGroupId);
        return ResponseEntity.noContent().build();
    }

}
