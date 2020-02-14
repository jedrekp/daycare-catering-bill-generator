package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.entity.AppUser;
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

    private AppUser testHeadmaster;
    private AppUser testGroupSupervisor1;
    private AppUser getTestGroupSupervisor2;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        buildAppUsersForTesting();

        when(appUserRepository.findByIdWithAllDetails(anyLong())).thenReturn(Optional.empty());
        when(appUserRepository.findByIdWithAllDetails(1L)).thenReturn(Optional.of(testHeadmaster));
        when(appUserRepository.findByIdWithAllDetails(2L)).thenReturn(Optional.of(testGroupSupervisor1));
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(appUserRepository.findByUsername("jlocke")).thenReturn(Optional.of(testHeadmaster));
        when(appUserRepository.findByUsername("mfarcik")).thenReturn(Optional.of(testGroupSupervisor1));
    }

    @Test
    void test_findSingleAppUserByIdWithAllDetails_whenAppUserExists() {
        assertEquals(testHeadmaster, appUserService.findSingleAppUserByIdWithAllDetails(1L));

        verify(appUserRepository, times(1)).findByIdWithAllDetails(1L);
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_findSingleAppUserByIdWithAllDetails_whenAppUserNotFound() {
        assertThrows(EntityNotFoundException.class, () -> appUserService.findSingleAppUserByIdWithAllDetails(3L));

        verify(appUserRepository, times(1)).findByIdWithAllDetails(3L);
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_findSingleAppUserByUsername_whenAppUserExists() {
        assertEquals(testGroupSupervisor1, appUserService.findSingleAppUserByUsername("mfarcik"));

        verify(appUserRepository, times(1)).findByUsername("mfarcik");
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_findSingleAppUserByUsername_whenAppUserNotFound() {
        assertThrows(EntityNotFoundException.class, () -> appUserService.findSingleAppUserByUsername("someUsername"));

        verify(appUserRepository, times(1)).findByUsername("someUsername");
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_findAllAppUsers() {
        List<AppUser> appUsers = Arrays.asList(testHeadmaster, testGroupSupervisor1);
        when(appUserRepository.findAll()).thenReturn(appUsers);

        assertEquals(appUsers, appUserService.findAllAppUsers());

        verify(appUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_findAllAppUsersByDaycareRole() {
        List<AppUser> groupSupervisors = Collections.singletonList(testGroupSupervisor1);
        when(appUserRepository.findAllByDaycareRoleOrderByLastNameAscFirstNameAsc(DaycareRole.GROUP_SUPERVISOR)).thenReturn(groupSupervisors);

        assertEquals(groupSupervisors, appUserService.findAllAppUsersByDaycareRole(DaycareRole.GROUP_SUPERVISOR));

        verify(appUserRepository, times(1)).findAllByDaycareRoleOrderByLastNameAscFirstNameAsc(DaycareRole.GROUP_SUPERVISOR);
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    void test_createNewGroupSupervisorAccount_whenUserWithInvalidDaycareRolePassedAsArgument() {
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
    void test_createNewGroupSupervisorAccount_whenUserWithValidDaycareRolePassedAsArgumentButUsernameIsTaken() {
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
    void test_createNewGroupSupervisorAccount_whenUserWithValidDaycareRolePassedAsArgumentAndUsernameNotTaken() {
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

    private void buildAppUsersForTesting() {
        testHeadmaster = new AppUser(
                "John",
                "Locke",
                "jlocke",
                passwordEncoder.encode("headmaster1"),
                DaycareRole.HEADMASTER);

        testGroupSupervisor1 = new AppUser(
                "Marcin",
                "Farcik",
                "mfarcik",
                passwordEncoder.encode("supervisor1"),
                DaycareRole.GROUP_SUPERVISOR);
    }

}