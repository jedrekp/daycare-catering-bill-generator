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
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private DaycareGroupService daycareGroupService;
    @InjectMocks
    private AppUserService appUserService;

    private AppUser testAppUser1;
    private AppUser testAppUser2;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        buildAppUsersForTesting();

        when(appUserRepository.findByIdWithAllDetails(anyLong())).thenReturn(Optional.empty());
        when(appUserRepository.findByIdWithAllDetails(1L)).thenReturn(Optional.of(testAppUser1));
        when(appUserRepository.findByIdWithAllDetails(2L)).thenReturn(Optional.of(testAppUser2));

        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(appUserRepository.findByUsername("jlocke")).thenReturn(Optional.of(testAppUser1));
        when(appUserRepository.findByUsername("mfarcik")).thenReturn(Optional.of(testAppUser2));
    }

    @Test
    void test_findSingleAppUserByIdWithAllDetails() {
        assertAll(
                () -> assertEquals(testAppUser1, appUserService.findSingleAppUserByIdWithAllDetails(1L)),
                () -> assertEquals(testAppUser2, appUserService.findSingleAppUserByIdWithAllDetails(2L)),
                () -> assertThrows(EntityNotFoundException.class,
                        () -> appUserService.findSingleAppUserByIdWithAllDetails(3L))
        );
    }

    @Test
    void test_findSingleAppUserByUsername() {
        assertAll(
                () -> assertEquals(testAppUser1, appUserService.findSingleAppUserByUsername("jlocke")),
                () -> assertEquals(testAppUser2, appUserService.findSingleAppUserByUsername("mfarcik")),
                () -> assertThrows(EntityNotFoundException.class,
                        () -> appUserService.findSingleAppUserByUsername("differentUsername"))
        );
    }

    @Test
    void test_findAllAppUsers() {
        List<AppUser> appUsers = Arrays.asList(testAppUser1, testAppUser2);
        when(appUserRepository.findAll()).thenReturn(appUsers);
        assertEquals(appUsers, appUserService.findAllAppUsers());
    }

    @Test
    void test_findAllAppUsersByDaycareRole() {
        List<AppUser> groupSupervisors = Collections.singletonList(testAppUser2);
        when(appUserRepository.findAllByDaycareRoleOrderByLastNameAscFirstNameAsc(DaycareRole.GROUP_SUPERVISOR)).thenReturn(groupSupervisors);
        assertEquals(groupSupervisors, appUserService.findAllAppUsersByDaycareRole(DaycareRole.GROUP_SUPERVISOR));
    }

    @Test
    void test_createNewGroupSupervisorAccount_whenUserWithInvalidDaycareRolePassedAsArgument() {
        assertThrows(IllegalArgumentException.class, () -> appUserService.createNewGroupSupervisorAccount(testAppUser1));
        verifyZeroInteractions(appUserRepository);
    }

    @Test
    void test_createNewGroupSupervisorAccount_whenUsernameAlreadyTaken() {
        when(appUserRepository.existsByUsernameIgnoreCase("mfarcik")).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> appUserService.createNewGroupSupervisorAccount(testAppUser2));
        verify(appUserRepository, times(1)).existsByUsernameIgnoreCase("mfarcik");
        verifyNoMoreInteractions(appUserRepository);
    }

    private void buildAppUsersForTesting() {
        testAppUser1 = new AppUser(
                1L,
                "John",
                "Locke",
                "jlocke",
                passwordEncoder.encode("headmaster1"),
                DaycareRole.HEADMASTER,
                null);

        testAppUser2 = new AppUser(
                2L,
                "Marcin",
                "Farcik",
                "mfarcik",
                passwordEncoder.encode("supervisor1"),
                DaycareRole.GROUP_SUPERVISOR,
                null);
    }

}