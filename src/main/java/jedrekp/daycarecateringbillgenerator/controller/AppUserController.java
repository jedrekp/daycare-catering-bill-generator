package jedrekp.daycarecateringbillgenerator.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.DTO.request.AppUserNewPasswordRequest;
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

    @GetMapping(params = "username")
    @PreAuthorize("hasRole('HEADMASTER') or authentication.name == #username")
    public ResponseEntity<AppUser> getSingleAppUserByUsername(@RequestParam String username) {
        return new ResponseEntity<>(appUserService.findSingleAppUserByUsername(username), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('HEADMASTER')")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<AppUser>> getAllAppUsers() {
        return new ResponseEntity<>(appUserService.findAllAppUsers(), HttpStatus.OK);
    }

    @GetMapping(params = "daycareRole")
    @PreAuthorize("hasRole('HEADMASTER')")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<AppUser>> getAppUsersWithSpecificRole(@RequestParam DaycareRole daycareRole) {
        return new ResponseEntity<>(appUserService.findAllAppUsersByDaycareRole(daycareRole), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('HEADMASTER')")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<AppUser> addNewGroupSupervisor(@RequestBody @Valid AppUser appUser) {
        return new ResponseEntity<>(appUserService.createNewGroupSupervisorAccount(appUser), HttpStatus.CREATED);
    }

    @DeleteMapping("{appUserId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity deleteGroupSupervisorAccount(@PathVariable long appUserId) {
        appUserService.deleteGroupSupervisorAccount(appUserId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{appUserId}/daycareGroups/{daycareGroupId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<AppUser> assignDaycareGroupToGroupSupervisor(
            @PathVariable long appUserId, @PathVariable long daycareGroupId) {
        return new ResponseEntity<>(appUserService.assignDaycareGroupToGroupSupervisor(daycareGroupId, appUserId), HttpStatus.OK);
    }

    @DeleteMapping("{appUserId}/daycareGroups/{daycareGroupId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity revokeDaycareGroupAssignmentFromGroupSupervisor(@PathVariable long appUserId, @PathVariable long daycareGroupId) {
        appUserService.revokeDaycareGroupAssignmentFromGroupSupervisor(daycareGroupId, appUserId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(params = "username")
    @PreAuthorize("authentication.name == #username")
    public ResponseEntity changeUserPassword(@RequestParam String username, @RequestBody @Valid AppUserNewPasswordRequest newPasswordRequest) {
        appUserService.changeAppUserPassword(username, newPasswordRequest);
        return ResponseEntity.noContent().build();
    }
}
