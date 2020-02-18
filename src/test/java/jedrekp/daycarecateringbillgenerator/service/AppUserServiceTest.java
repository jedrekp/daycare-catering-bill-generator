package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.entity.AppUser;
import jedrekp.daycarecateringbillgenerator.entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.repository.AppUserRepository;
import jedrekp.daycarecateringbillgenerator.utility.DaycareRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

    private AppUser testGroupSupervisor1;
    private AppUser testGroupSupervisor2;
    private AppUser testHeadmaster;
    private DaycareGroup testDaycareGroup1;
    private DaycareGroup testDaycareGroup2;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        buildAppUsersForTesting();
        buildDaycareGroupsForTesting();
        setBehaviorOfMockMethodsUsedAcrossMultipleTestCases();
    }

    private void buildAppUsersForTesting() {
        testGroupSupervisor1 = new AppUser(
                "Marcin",
                "Farcik",
                "mfarcik",
                passwordEncoder.encode("supervisor1"),
                DaycareRole.GROUP_SUPERVISOR);

        testGroupSupervisor2 = new AppUser(
                "Jackie",
                "Chan",
                "jchan",
                passwordEncoder.encode("supervisor2"),
                DaycareRole.GROUP_SUPERVISOR);

        testHeadmaster = new AppUser(
                "John",
                "Locke",
                "jlocke",
                passwordEncoder.encode("headmaster1"),
                DaycareRole.HEADMASTER);

    }

    private void buildDaycareGroupsForTesting() {
        testDaycareGroup1 = new DaycareGroup("Hippos");
        testDaycareGroup2 = new DaycareGroup("Ducks");
    }

    private void setBehaviorOfMockMethodsUsedAcrossMultipleTestCases() {
        when(appUserRepository.findByIdWithAllDetails(anyLong())).thenReturn(Optional.empty());
        when(appUserRepository.findByIdWithAllDetails(1L)).thenReturn(Optional.of(testGroupSupervisor1));
        when(appUserRepository.findByIdWithAllDetails(2L)).thenReturn(Optional.of(testGroupSupervisor2));
        when(appUserRepository.findByIdWithAllDetails(3L)).thenReturn(Optional.of(testHeadmaster));

        when(daycareGroupService.findSingleGroupByIdWithAllDetails(1L)).thenReturn(testDaycareGroup1);
        when(daycareGroupService.findSingleGroupByIdWithAllDetails(not(eq(1L)))).thenThrow(EntityNotFoundException.class);
    }

    @Test
    void test_findSingleAppUserByIdWithAllDetails_whenAppUserNotFound() {
        assertThrows(EntityNotFoundException.class, () -> appUserService.findSingleAppUserByIdWithAllDetails(4L));

        verify(appUserRepository, times(1)).findByIdWithAllDetails(4L);
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_findSingleAppUserByIdWithAllDetails_whenAppUserFound() {
        assertEquals(testGroupSupervisor1, appUserService.findSingleAppUserByIdWithAllDetails(1L));

        verify(appUserRepository, times(1)).findByIdWithAllDetails(1L);
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_findSingleAppUserByUsername_whenAppUserNotFound() {
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> appUserService.findSingleAppUserByUsername("someUsername"));

        verify(appUserRepository, times(1)).findByUsername("someUsername");
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_findSingleAppUserByUsername_whenAppUserFound() {
        when(appUserRepository.findByUsername("mfarcik")).thenReturn(Optional.of(testGroupSupervisor1));

        assertEquals(testGroupSupervisor1, appUserService.findSingleAppUserByUsername("mfarcik"));

        verify(appUserRepository, times(1)).findByUsername("mfarcik");
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_findAllAppUsers() {
        List<AppUser> appUsers = Arrays.asList(testHeadmaster, testGroupSupervisor1, testGroupSupervisor2);

        when(appUserRepository.findAll()).thenReturn(appUsers);

        assertEquals(appUsers, appUserService.findAllAppUsers());

        verify(appUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_findAllAppUsersByDaycareRole() {
        List<AppUser> groupSupervisors = Collections.singletonList(testHeadmaster);

        when(appUserRepository.findAllByDaycareRoleOrderByLastNameAscFirstNameAsc(DaycareRole.HEADMASTER)).thenReturn(groupSupervisors);

        assertEquals(groupSupervisors, appUserService.findAllAppUsersByDaycareRole(DaycareRole.HEADMASTER));

        verify(appUserRepository, times(1)).findAllByDaycareRoleOrderByLastNameAscFirstNameAsc(DaycareRole.HEADMASTER);
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_createNewGroupSupervisorAccount_whenAppUserNotGroupSupervisor() {
        AppUser appUserToSave = new AppUser(
                "Tom",
                "Brown",
                "tbrown",
                "testPassword",
                DaycareRole.HEADMASTER
        );

        assertThrows(IllegalArgumentException.class, () -> appUserService.createNewGroupSupervisorAccount(appUserToSave));

        verifyZeroInteractions(appUserRepository);
    }

    @Test
    void test_createNewGroupSupervisorAccount_whenAppUserIsGroupSupervisortButUsernameTaken() {
        AppUser appUserToSave = new AppUser(
                "Tom",
                "Brown",
                "tbrown",
                "testPassword",
                DaycareRole.GROUP_SUPERVISOR
        );

        when(appUserRepository.existsByUsernameIgnoreCase("tbrown")).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> appUserService.createNewGroupSupervisorAccount(appUserToSave));

        verify(appUserRepository, times(1)).existsByUsernameIgnoreCase("tbrown");
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_createNewGroupSupervisorAccount_whenAppUserIsGroupSupervisorAndUsernameNotTaken() {
        AppUser appUserToSave = new AppUser(
                "Tom",
                "Brown",
                "tbrown",
                "testPassword",
                DaycareRole.GROUP_SUPERVISOR
        );

        when(appUserRepository.existsByUsernameIgnoreCase("tbrown")).thenReturn(false);
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(save -> save.getArguments()[0]);

        AppUser newGroupSupervisor = appUserService.createNewGroupSupervisorAccount(appUserToSave);
        assertAll(
                "Created appUser must have the same properties as method argument, except for password, which has been encoded.",
                () -> assertEquals("Tom", newGroupSupervisor.getFirstName()),
                () -> assertEquals("Brown", newGroupSupervisor.getLastName()),
                () -> assertEquals("tbrown", newGroupSupervisor.getUsername()),
                () -> assertTrue(passwordEncoder.matches(
                        "testPassword", newGroupSupervisor.getPassword())),
                () -> assertEquals(DaycareRole.GROUP_SUPERVISOR, newGroupSupervisor.getDaycareRole())
        );

        verify(appUserRepository, times(1)).existsByUsernameIgnoreCase("tbrown");
        verify(appUserRepository, times(1)).save(any(AppUser.class));
        verifyNoMoreInteractions(appUserRepository);

    }

    @Test
    void test_assignDaycareGroupToGroupSupervisor_whenDaycareGroupNotFound() {
        assertThrows(EntityNotFoundException.class, () -> appUserService.assignDaycareGroupToGroupSupervisor(2L, 1L));

        verify(daycareGroupService, times(1)).findSingleGroupByIdWithAllDetails(2L);
        verifyNoMoreInteractions(daycareGroupService, appUserRepository);
    }

    @Test
    void test_assignDaycareGroupToGroupSupervisor_whenDaycareGroupFoundButGroupSupervisorNotFound() {
        assertThrows(EntityNotFoundException.class, () -> appUserService.assignDaycareGroupToGroupSupervisor(1L, 4L));

        verify(daycareGroupService, times(1)).findSingleGroupByIdWithAllDetails(1L);
        verify(appUserRepository, times(1)).findByIdWithAllDetails(4L);
        verifyNoMoreInteractions(daycareGroupService, appUserRepository);
    }

    @Test
    void test_assignDaycareGroupToGroupSupervisor_whenBothFoundButGroupAlreadyAssignedToSomeGroupSupervisor() {
        testDaycareGroup1.setGroupSupervisor(testGroupSupervisor2);

        assertThrows(IllegalArgumentException.class, () -> appUserService.assignDaycareGroupToGroupSupervisor(1L, 1L));

        verify(daycareGroupService, times(1)).findSingleGroupByIdWithAllDetails(1L);
        verify(appUserRepository, times(1)).findByIdWithAllDetails(1L);
        verifyNoMoreInteractions(daycareGroupService, appUserRepository);
    }

    @Test
    void test_assignDaycareGroupToGroupSupervisor_whenBothFoundButGroupSupervisorAlreadyHasSomeGroupAssigned() {
        testGroupSupervisor1.setDaycareGroup(testDaycareGroup2);

        assertThrows(IllegalArgumentException.class, () -> appUserService.assignDaycareGroupToGroupSupervisor(1L, 1L));

        verify(daycareGroupService, times(1)).findSingleGroupByIdWithAllDetails(1L);
        verify(appUserRepository, times(1)).findByIdWithAllDetails(1L);
        verifyNoMoreInteractions(daycareGroupService, appUserRepository);
    }

    @Test
    void test_assignDaycareGroupToGroupSupervisor_whenBothFoundAndFreeToBeAssigned() {
        appUserService.assignDaycareGroupToGroupSupervisor(1, 1);
        assertAll("Daycare group has been assigned to user with group supervisor role.",
                () -> assertEquals(testDaycareGroup1, testGroupSupervisor1.getDaycareGroup()),
                () -> assertEquals(testGroupSupervisor1, testDaycareGroup1.getGroupSupervisor()),
                () -> assertEquals(DaycareRole.GROUP_SUPERVISOR, testGroupSupervisor1.getDaycareRole())
        );

        verify(daycareGroupService, times(1)).findSingleGroupByIdWithAllDetails(1L);
        verify(appUserRepository, times(1)).findByIdWithAllDetails(1L);
        verifyNoMoreInteractions(daycareGroupService, appUserRepository);
    }

    @Test
    void test_removeAssignedGroupFromGroupSupervisor_whenDaycareGroupNotFoundOrNotAssignedToThisGroupSupervisor() {
        when(daycareGroupService.findSingleGroupByIdAndGroupSupervisorId(1L, 1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> appUserService.removeAssignedGroupFromGroupSupervisor(1L, 1L));

        verify(daycareGroupService, times(1)).findSingleGroupByIdAndGroupSupervisorId(1L, 1L);
        verifyNoMoreInteractions(daycareGroupService, appUserRepository);
    }

    @Test
    void test_removeAssignedGroupFromGroupSupervisor_whenDaycareGroupFoundAndIsAssignedToThisSupervisor() {
        testGroupSupervisor1.setDaycareGroup(testDaycareGroup1);
        testDaycareGroup1.setGroupSupervisor(testGroupSupervisor1);

        when(daycareGroupService.findSingleGroupByIdAndGroupSupervisorId(1L, 1L)).thenReturn(testDaycareGroup1);

        appUserService.removeAssignedGroupFromGroupSupervisor(1L, 1L);
        assertAll("Daycare group assignment has been revoked from group supervisor.",
                () -> assertNull(testDaycareGroup1.getGroupSupervisor()),
                () -> assertNull(testGroupSupervisor1.getDaycareGroup())
        );

        verify(daycareGroupService, times(1)).findSingleGroupByIdAndGroupSupervisorId(1L, 1L);
        verifyNoMoreInteractions(daycareGroupService, appUserRepository);
    }

    @Test
    void test_deleteGroupSupervisorAccount_whenGroupSupervisorNotFound() {
        assertThrows(EntityNotFoundException.class, () -> appUserService.deleteGroupSupervisorAccount(4L));

        verify(appUserRepository, times(1)).findByIdWithAllDetails(4L);
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_deleteGroupSupervisorAccount_whenGroupSupervisorFound_andConfirmGroupSupervisorAssociationIsRemovedFromDaycareGroup() {
        testGroupSupervisor1.setDaycareGroup(testDaycareGroup1);

        appUserService.deleteGroupSupervisorAccount(1L);
        assertNull(testDaycareGroup1.getGroupSupervisor());

        verify(appUserRepository, times(1)).findByIdWithAllDetails(1L);
        verify(appUserRepository, times(1)).delete(testGroupSupervisor1);
        verifyNoMoreInteractions(appUserRepository);
    }

}