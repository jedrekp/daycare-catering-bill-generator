package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.DTO.request.AppUserNewPasswordRequest;
import jedrekp.daycarecateringbillgenerator.entity.AppUser;
import jedrekp.daycarecateringbillgenerator.entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.repository.AppUserRepository;
import jedrekp.daycarecateringbillgenerator.utility.DaycareRole;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@SuppressWarnings("SpellCheckingInspection")
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private DaycareGroupService daycareGroupService;
    @InjectMocks
    private AppUserService appUserService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    //findSingleAppUserByIdWithAllDetails

    @Test
    void shouldReturnAppUser_WhenAppUserFoundById() {
        //given
        AppUser testAppUser = new AppUser(
                "Jackie",
                "Chan",
                "jchan",
                passwordEncoder.encode("testPassword"),
                DaycareRole.GROUP_SUPERVISOR
        );

        when(appUserRepository.findByIdWithAllDetails(1L)).thenReturn(Optional.of(testAppUser));

        //when
        AppUser returnedAppUser = appUserService.findSingleAppUserByIdWithAllDetails(1L);

        //then
        assertEquals(testAppUser, returnedAppUser);

        verify(appUserRepository, times(1)).findByIdWithAllDetails(1L);
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }

    @Test
    void shouldThrowException_WhenAppUserNotFoundById() {
        //given
        when(appUserRepository.findByIdWithAllDetails(1L)).thenReturn(Optional.empty());

        //expect
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> appUserService.findSingleAppUserByIdWithAllDetails(1L));
        assertEquals("User #1 does not exist.", ex.getMessage());

        verify(appUserRepository, times(1)).findByIdWithAllDetails(1L);
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }


    //findSingleAppUserByUsername

    @Test
    void shouldReturnAppUser_WhenAppUserFoundByUsername() {
        //given
        AppUser testAppUser = new AppUser(
                "Jackie",
                "Chan",
                "jchan",
                passwordEncoder.encode("testPassword"),
                DaycareRole.GROUP_SUPERVISOR
        );

        when(appUserRepository.findByUsername("jchan")).thenReturn(Optional.of(testAppUser));

        //when
        AppUser returnedAppUser = appUserService.findSingleAppUserByUsername("jchan");

        //then
        assertEquals(testAppUser, returnedAppUser);

        verify(appUserRepository, times(1)).findByUsername("jchan");
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }

    @Test
    void shouldThrowException_WhenAppUserNotFoundByUsername() {
        //given
        when(appUserRepository.findByUsername("someUsername")).thenReturn(Optional.empty());

        //expect
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> appUserService.findSingleAppUserByUsername("someUsername"));
        assertEquals("User 'someUsername' does not exist.", ex.getMessage());

        verify(appUserRepository, times(1)).findByUsername("someUsername");
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }


    //findAllAppUsers

    @Test
    void shouldReturnCollectionOfAllAppUsers() {
        //given
        AppUser testAppUser1 = new AppUser(
                "Jackie",
                "Chan",
                "jchan",
                passwordEncoder.encode("testPassword"),
                DaycareRole.GROUP_SUPERVISOR
        );
        AppUser testAppUser2 = new AppUser(
                "John",
                "Locke",
                "jlocke",
                passwordEncoder.encode("testPassword"),
                DaycareRole.HEADMASTER
        );

        List<AppUser> testAppUserList = Arrays.asList(testAppUser1, testAppUser2);

        when(appUserRepository.findAll()).thenReturn(testAppUserList);

        //when
        Collection<AppUser> returnedAppUsers = appUserService.findAllAppUsers();

        //then
        assertEquals(testAppUserList, returnedAppUsers);

        verify(appUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }


    //findAllAppUsersByDaycareRole

    @Test
    void shouldReturnCollectionOfAppUsersWithSpecificDaycareRole() {
        //given
        AppUser testAppUser = new AppUser(
                "John",
                "Locke",
                "jlocke",
                passwordEncoder.encode("testPassword"),
                DaycareRole.HEADMASTER
        );

        List<AppUser> testHeadmasterList = Collections.singletonList(testAppUser);

        when(appUserRepository.findAllByDaycareRole(DaycareRole.HEADMASTER)).thenReturn(testHeadmasterList);

        //when
        Collection<AppUser> returnedAppUsers = appUserService.findAllAppUsersByDaycareRole(DaycareRole.HEADMASTER);

        //then
        assertEquals(testHeadmasterList, returnedAppUsers);

        verify(appUserRepository, times(1)).findAllByDaycareRole(DaycareRole.HEADMASTER);
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }


    //createNewGroupSupervisorAccount

    @Test
    void shouldCreateAndReturnNewAppUserWithGroupSupervisorRole() {
        //given
        AppUser appUserToSave = new AppUser(
                "Tom",
                "Brown",
                "tbrown",
                "testPassword",
                DaycareRole.GROUP_SUPERVISOR
        );

        when(appUserRepository.existsByUsernameIgnoreCase("tbrown")).thenReturn(false);
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(save -> save.getArguments()[0]);

        //when
        AppUser createdGroupSupervisor = appUserService.createNewGroupSupervisorAccount(appUserToSave);

        //then
        assertAll(
                "Created appUser should have the same properties as appUser passed as method argument, except for password, which should be encoded.",
                () -> assertEquals("Tom", createdGroupSupervisor.getFirstName()),
                () -> assertEquals("Brown", createdGroupSupervisor.getLastName()),
                () -> assertEquals("tbrown", createdGroupSupervisor.getUsername()),
                () -> assertTrue(passwordEncoder.matches(
                        "testPassword", createdGroupSupervisor.getPassword())),
                () -> assertEquals(DaycareRole.GROUP_SUPERVISOR, createdGroupSupervisor.getDaycareRole())
        );

        verify(appUserRepository, times(1)).existsByUsernameIgnoreCase("tbrown");
        verify(appUserRepository, times(1)).save(any(AppUser.class));
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }

    @Test
    void shouldThrowException_WhenAppUserNotGroupSupervisor() {
        //given
        AppUser appUserToSave = new AppUser(
                "Tom",
                "Brown",
                "tbrown",
                "testPassword",
                DaycareRole.HEADMASTER
        );

        //expect
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> appUserService.createNewGroupSupervisorAccount(appUserToSave));
        assertEquals("You may only create new accounts for group supervisors.", ex.getMessage());

        verifyZeroInteractions(appUserRepository, daycareGroupService);
    }

    @Test
    void shouldThrowException_WhenUsernameAlreadyTaken() {
        //given
        AppUser appUserToSave = new AppUser(
                "Tom",
                "Brown",
                "tbrown",
                "testPassword",
                DaycareRole.GROUP_SUPERVISOR
        );

        when(appUserRepository.existsByUsernameIgnoreCase("tbrown")).thenReturn(true);

        //expect
        EntityExistsException ex = assertThrows(EntityExistsException.class, () -> appUserService.createNewGroupSupervisorAccount(appUserToSave));
        assertEquals("This username is already taken.", ex.getMessage());

        verify(appUserRepository, times(1)).existsByUsernameIgnoreCase("tbrown");
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }


    //assignDaycareGroupToGroupSupervisor

    @Test
    void shouldAssignDaycareGroupToAppUserWithGroupSupervisorRole() {
        //given
        AppUser testAppUser = new AppUser(
                "Jackie",
                "Chan",
                "jchan",
                passwordEncoder.encode("testPassword"),
                DaycareRole.GROUP_SUPERVISOR
        );

        DaycareGroup testDaycareGroup = new DaycareGroup("Tigers");

        when(daycareGroupService.findSingleGroupByIdWithAllDetails(1L)).thenReturn(testDaycareGroup);
        when(appUserRepository.findByIdAndDaycareRole(1L, DaycareRole.GROUP_SUPERVISOR)).thenReturn(Optional.of(testAppUser));

        //when
        appUserService.assignDaycareGroupToGroupSupervisor(1L, 1L);

        //then
        assertAll("Daycare group has been assigned to user with group supervisor role.",
                () -> assertEquals(testDaycareGroup, testAppUser.getDaycareGroup()),
                () -> assertEquals(testAppUser, testDaycareGroup.getGroupSupervisor()),
                () -> assertEquals(DaycareRole.GROUP_SUPERVISOR, testAppUser.getDaycareRole())
        );

        verify(daycareGroupService, times(1)).findSingleGroupByIdWithAllDetails(1L);
        verify(appUserRepository, times(1)).findByIdAndDaycareRole(1L, DaycareRole.GROUP_SUPERVISOR);
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }


    @Test
    void shouldThrowException_WhenDaycareGroupNotFound() {
        //given
        when(daycareGroupService.findSingleGroupByIdWithAllDetails(1L)).thenThrow(new EntityNotFoundException("Daycare group #1 does not exist."));

        //expect
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> appUserService.assignDaycareGroupToGroupSupervisor(1L, 1L));
        assertEquals("Daycare group #1 does not exist.", ex.getMessage());

        verify(daycareGroupService, times(1)).findSingleGroupByIdWithAllDetails(1L);
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }

    @Test
    void shouldThrowException_WhenAppUserWithGroupSupervisorRoleNotFound() {
        //given
        DaycareGroup testDaycareGroup = new DaycareGroup("Tigers");

        when(daycareGroupService.findSingleGroupByIdWithAllDetails(1L)).thenReturn(testDaycareGroup);
        when(appUserRepository.findByIdAndDaycareRole(1L, DaycareRole.GROUP_SUPERVISOR)).thenReturn(Optional.empty());

        //expect
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> appUserService.assignDaycareGroupToGroupSupervisor(1L, 1L));
        assertEquals("User with id #1 does not exists or is not a group supervisor.", ex.getMessage());

        verify(daycareGroupService, times(1)).findSingleGroupByIdWithAllDetails(1L);
        verify(appUserRepository, times(1)).findByIdAndDaycareRole(1L, DaycareRole.GROUP_SUPERVISOR);
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }

    @Test
    void shouldThrowException_WhenSomeDaycareGroupAlreadyAssignedToGivenGroupSupervisor() {
        //given
        AppUser testAppUser = new AppUser(
                "Jackie",
                "Chan",
                "jchan",
                passwordEncoder.encode("testPassword"),
                DaycareRole.GROUP_SUPERVISOR
        );
        ReflectionTestUtils.setField(testAppUser, "id", 1L);

        DaycareGroup testDaycareGroup1 = new DaycareGroup("Tigers");
        DaycareGroup testDaycareGroup2 = new DaycareGroup("Hippos");

        testAppUser.setDaycareGroup(testDaycareGroup2);

        when(daycareGroupService.findSingleGroupByIdWithAllDetails(1L)).thenReturn(testDaycareGroup1);
        when(appUserRepository.findByIdAndDaycareRole(1L, DaycareRole.GROUP_SUPERVISOR)).thenReturn(Optional.of(testAppUser));

        //expect
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> appUserService.assignDaycareGroupToGroupSupervisor(1L, 1L));
        assertEquals("Another daycare group is already assigned to user #1. Assignment needs to be revoked first.", ex.getMessage());

        verify(daycareGroupService, times(1)).findSingleGroupByIdWithAllDetails(1L);
        verify(appUserRepository, times(1)).findByIdAndDaycareRole(1L, DaycareRole.GROUP_SUPERVISOR);
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }

    @Test
    void shouldThrowException_WhenGivenDaycareGroupreadyAssignedToSomeGroupSupervisor() {
        //given
        AppUser testAppUser1 = new AppUser(
                "Jackie",
                "Chan",
                "jchan",
                passwordEncoder.encode("testPassword"),
                DaycareRole.GROUP_SUPERVISOR
        );
        AppUser testAppUser2 = new AppUser(
                "John",
                "Locke",
                "jlocke",
                passwordEncoder.encode("testPassword"),
                DaycareRole.GROUP_SUPERVISOR
        );

        DaycareGroup testDaycareGroup = new DaycareGroup("Tigers");
        ReflectionTestUtils.setField(testDaycareGroup, "id", 1L);
        testDaycareGroup.setGroupSupervisor(testAppUser2);

        when(daycareGroupService.findSingleGroupByIdWithAllDetails(1L)).thenReturn(testDaycareGroup);
        when(appUserRepository.findByIdAndDaycareRole(1L, DaycareRole.GROUP_SUPERVISOR)).thenReturn(Optional.of(testAppUser1));

        //expect
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> appUserService.assignDaycareGroupToGroupSupervisor(1L, 1L));
        assertEquals("Daycare group #1 is already assigned to another group supervisor.", ex.getMessage());

        verify(daycareGroupService, times(1)).findSingleGroupByIdWithAllDetails(1L);
        verify(appUserRepository, times(1)).findByIdAndDaycareRole(1L, DaycareRole.GROUP_SUPERVISOR);
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }



    //revokeDaycareGroupAssignmentFromGroupSupervisor

    @Test
    void shouldRemoveRelationBetweenDaycareGroupAndGroupSupervisor() {
        //given
        AppUser testAppUser = new AppUser(
                "Jackie",
                "Chan",
                "jchan",
                passwordEncoder.encode("testPassword"),
                DaycareRole.GROUP_SUPERVISOR
        );

        DaycareGroup testDaycareGroup = new DaycareGroup("Tigers");

        testAppUser.setDaycareGroup(testDaycareGroup);
        testDaycareGroup.setGroupSupervisor(testAppUser);

        when(daycareGroupService.findSingleGroupByIdAndGroupSupervisorId(1L, 1L)).thenReturn(testDaycareGroup);

        //when
        appUserService.revokeDaycareGroupAssignmentFromGroupSupervisor(1L, 1L);

        //then
        assertAll(
                "Association between appUser and daycare group has been removed.",
                () -> assertNull(testAppUser.getDaycareGroup()),
                () -> assertNull(testDaycareGroup.getGroupSupervisor())
        );

        verify(daycareGroupService, times(1)).findSingleGroupByIdAndGroupSupervisorId(1L, 1L);
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }


    @Test
    void shouldThrowException_WhenDaycareGroupNotFoundByIdAndGroupSupervisorId() {
        //given
        when(daycareGroupService.findSingleGroupByIdAndGroupSupervisorId(1L, 1L)).thenThrow(
                new EntityNotFoundException("Daycare group #1 is not assigned to user #1."));

        //expect
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> appUserService.revokeDaycareGroupAssignmentFromGroupSupervisor(1L, 1L));
        assertEquals("Daycare group #1 is not assigned to user #1.", ex.getMessage());

        verify(daycareGroupService, times(1)).findSingleGroupByIdAndGroupSupervisorId(1L, 1L);
        verifyNoMoreInteractions(daycareGroupService, appUserRepository);
    }


    //deleteGroupSupervisorAccount

    @Test
    void shoulCallDeleteByIdMethodAndRemoveAppUserRelationFromDaycareGroup() {
        //given
        AppUser testAppUser = new AppUser(
                "Jackie",
                "Chan",
                "jchan",
                passwordEncoder.encode("testPassword"),
                DaycareRole.GROUP_SUPERVISOR
        );

        DaycareGroup testDaycareGroup = new DaycareGroup("Tigers");

        testAppUser.setDaycareGroup(testDaycareGroup);
        testDaycareGroup.setGroupSupervisor(testAppUser);

        when(appUserRepository.findByIdAndDaycareRole(1L, DaycareRole.GROUP_SUPERVISOR)).thenReturn(Optional.of(testAppUser));
        doNothing().when(appUserRepository).deleteById(1L);

        //when
        appUserService.deleteGroupSupervisorAccount(1L);

        //then
        assertNull(testDaycareGroup.getGroupSupervisor());
        verify(appUserRepository, times(1)).findByIdAndDaycareRole(1L, DaycareRole.GROUP_SUPERVISOR);

        verify(appUserRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }

    @Test
    void shouldThrowException_WhenAppUserWithGroupSupervisorRoleNotFoundById() {
        //given
        when(appUserRepository.findByIdAndDaycareRole(1L, DaycareRole.GROUP_SUPERVISOR)).thenReturn(Optional.empty());

        //expect
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> appUserService.deleteGroupSupervisorAccount(1L));
        assertEquals("User with id #1 does not exists or is not a group supervisor.", ex.getMessage());

        verify(appUserRepository, times(1)).findByIdAndDaycareRole(1L, DaycareRole.GROUP_SUPERVISOR);
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }



    //changeAppUserPassword

    @Test
    void shouldChangeAppUserPasswordToEncodedValueOfNewPassword() {
        //given
        AppUser testAppUser = new AppUser(
                "Jackie",
                "Chan",
                "jchan",
                passwordEncoder.encode("currentPassword"),
                DaycareRole.HEADMASTER
        );

        AppUserNewPasswordRequest newPasswordRequest = new AppUserNewPasswordRequest("currentPassword", "newPassword");

        when(appUserRepository.findByUsername("jchan")).thenReturn(Optional.of(testAppUser));

        //when
        appUserService.changeAppUserPassword("jchan", newPasswordRequest);

        //then
        assertTrue(passwordEncoder.matches("newPassword", testAppUser.getPassword()));

        verify(appUserRepository, times(1)).findByUsername("jchan");
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }

    @Test
    void shouldThrowException_WhenAppUserNotFound() {
        //given
        AppUserNewPasswordRequest newPasswordRequest = new AppUserNewPasswordRequest("currentPassword", "newPassword");

        when(appUserRepository.findByUsername("jchan")).thenReturn(Optional.empty());

        //expect
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> appUserService.changeAppUserPassword("jchan", newPasswordRequest));
        assertEquals("User 'jchan' does not exist.", ex.getMessage());

        verify(appUserRepository, times(1)).findByUsername("jchan");
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }

    @Test
    void shouldThrowException_WhenCurrentPasswordNotMatched() {
        //given
        AppUser testAppUser = new AppUser(
                "Jackie",
                "Chan",
                "jchan",
                passwordEncoder.encode("currentPassword"),
                DaycareRole.HEADMASTER
        );

        AppUserNewPasswordRequest newPasswordRequest = new AppUserNewPasswordRequest("incorrectCurrentPassword", "newPassword");

        when(appUserRepository.findByUsername("jchan")).thenReturn(Optional.of(testAppUser));

        //expect
        BadCredentialsException ex = assertThrows(BadCredentialsException.class, () -> appUserService.changeAppUserPassword("jchan", newPasswordRequest));
        assertEquals("Your current password is missing or incorrect.",ex.getMessage());

        verify(appUserRepository, times(1)).findByUsername("jchan");
        verifyNoMoreInteractions(appUserRepository, daycareGroupService);
    }


}