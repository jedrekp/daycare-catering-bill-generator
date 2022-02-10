package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.DTO.request.AppUserNewPasswordRequest;
import jedrekp.daycarecateringbillgenerator.entity.AppUser;
import jedrekp.daycarecateringbillgenerator.entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.repository.AppUserRepository;
import jedrekp.daycarecateringbillgenerator.utility.DaycareRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.text.MessageFormat;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final DaycareGroupService daycareGroupService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional(readOnly = true)
    public AppUser findSingleAppUserByIdWithAllDetails(long appUserId) {
        return appUserRepository.findByIdWithAllDetails(appUserId).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("User #{0} does not exist.", appUserId)));
    }

    @Transactional(readOnly = true)
    public AppUser findSingleAppUserByUsername(String username) {
        return appUserRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("User ''{0}'' does not exist.", username)));
    }

    @Transactional(readOnly = true)
    public Collection<AppUser> findAllAppUsers() {
        return appUserRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Collection<AppUser> findAllAppUsersByDaycareRole(DaycareRole daycareRole) {
        return appUserRepository.findAllByDaycareRole(daycareRole);
    }

    @Transactional
    public AppUser createNewGroupSupervisorAccount(AppUser appUser) {
        if (appUser.getDaycareRole() != DaycareRole.GROUP_SUPERVISOR) {
            throw new IllegalArgumentException("You may only create new accounts for group supervisors.");
        }
        checkUsernameAvailability(appUser.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    @Transactional
    public AppUser assignDaycareGroupToGroupSupervisor(long daycareGroupId, long appUserId) {
        DaycareGroup daycareGroup = daycareGroupService.findSingleGroupByIdWithAllDetails(daycareGroupId);
        AppUser appUser = findSingleGroupSupervisorById(appUserId);
        verifyIfDaycareGroupCanBeAssignedToUser(appUser, daycareGroup);
        daycareGroup.setGroupSupervisor(appUser);
        appUser.setDaycareGroup(daycareGroup);
        return appUser;
    }

    @Transactional
    public void revokeDaycareGroupAssignmentFromGroupSupervisor(long daycareGroupId, long appUserId) {
        DaycareGroup daycareGroup = daycareGroupService.findSingleGroupByIdAndGroupSupervisorId(daycareGroupId, appUserId);
        AppUser appUser = daycareGroup.getGroupSupervisor();
        daycareGroup.setGroupSupervisor(null);
        appUser.setDaycareGroup(null);
    }

    @Transactional
    public void deleteGroupSupervisorAccount(long appUserId) {
        AppUser appUser = findSingleGroupSupervisorById(appUserId);
        if (appUser.getDaycareGroup() != null) {
            appUser.getDaycareGroup().setGroupSupervisor(null);
        }
        appUserRepository.deleteById(appUserId);
    }

    @Transactional
    public void changeAppUserPassword(String username, AppUserNewPasswordRequest newPasswordRequest) {
        AppUser appUser = findSingleAppUserByUsername(username);
        if (passwordEncoder.matches(newPasswordRequest.getCurrentPassword(), appUser.getPassword())) {
            appUser.setPassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));
        } else {
            throw new BadCredentialsException("Your current password is missing or incorrect.");
        }
    }

    private AppUser findSingleGroupSupervisorById(long appUserId) {
        return appUserRepository.findByIdAndDaycareRole(appUserId, DaycareRole.GROUP_SUPERVISOR).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format("User with id #{0} does not exists or is not a group supervisor.", appUserId)));
    }

    private void checkUsernameAvailability(String username) {
        if (appUserRepository.existsByUsernameIgnoreCase(username)) {
            throw new EntityExistsException("This username is already taken.");
        }
    }

    private void verifyIfDaycareGroupCanBeAssignedToUser(AppUser appUser, DaycareGroup daycareGroup) {
        if (appUser.getDaycareGroup() != null) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "Another daycare group is already assigned to user #{0}. Assignment needs to be revoked first.", appUser.getId()));
        }
        if (daycareGroup.getGroupSupervisor() != null) {
            throw new IllegalArgumentException(MessageFormat.format("Daycare group #{0} is already assigned to another group supervisor.",
                    daycareGroup.getId()));
        }
    }

}
